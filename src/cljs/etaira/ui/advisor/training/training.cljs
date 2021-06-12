(ns etaira.ui.advisor.training.training
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   ["dexie" :refer [Dexie]]
   ["@tensorflow/tfjs" :as tfjs]
   [oops.core :refer [oget+ oget ocall]]
   [cljs.core.async :refer [<! timeout put! chan]]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [etaira.app :refer [etaira-app]]
   [etaira.interop.async :refer [async await await-all]]
   [com.fulcrologic.fulcro.components :refer [defsc]]
   #_[etaira.indicators.talib.api :as talib]))

(def db (atom nil))
(def dense (.. tfjs -layers -dense))

(defn fetch-ohlcv [model exchange symbol timeframe date-from date-to]
  (go
    (reset! db (Dexie. (:neural-network-model/id model)))
    (-> (.version @db 1)
        (.stores #js {:dataset "++id, timestamp, close, volume"}))
    (let [ch (chan)]
      (loop [date-from! (.getTime date-from)]
        (<! (timeout (oget exchange :rateLimit)))
        (when (and date-from! (< date-from! (.getTime date-to)))
          (-> (ocall exchange :fetchOHLCV symbol timeframe date-from! 500)
              (.then (fn [data]
                       (put! ch (js->clj data)))))
          (let [data (<! ch)
                new-date-from (js/Date. (first (last data)))]
            (when (seq data)
              (-> (.-dataset @db)
                  (.bulkPut
                   (clj->js
                    (vec
                     (for [row data]
                       {:timestamp (get row 0)
                        :close     (js/parseFloat (get row 4))
                        :volume    (js/parseFloat (get row 5))}))))
                  (.then #(put! ch 1))
                  (.catch js/console.error))
              (<! ch))
            (when (= (count data) 500)
              (recur new-date-from))))))
    (println "Finished history download")
    true))

(defn download-history-data [model]
  (go
    (let [{:dataset/keys [exchange cryptopair date-from date-to interval]} (:neural-network-model/dataset model)
          exchange-class (oget+ js/ccxt exchange)
          exchange-obj (exchange-class.)]
      (if (oget exchange-obj "has.fetchOHLCV")
        (<! (fetch-ohlcv model exchange-obj cryptopair interval date-from date-to))
        (println "exchange doesn't provide history data")))))

(defsc NeuralNetworkModelDataForTraining [_ props]
  {:query [:neural-network-model/id
           {:neural-network-model/config [:neural-network-config/id
                                          :neural-network-config/learning-rate
                                          :neural-network-config/activation
                                          :neural-network-config/regularization
                                          :neural-network-config/problem
                                          {:neural-network-config/layers [:neural-network-layer/id
                                                                          :neural-network-layer/number
                                                                          :neural-network-layer/number-of-neurons
                                                                          :neural-network-layer/type]}]}
           {:neural-network-model/dataset [:dataset/id
                                           :dataset/exchange
                                           :dataset/cryptopair
                                           :dataset/date-from
                                           :dataset/date-to
                                           :dataset/interval
                                           :dataset/indicators]}]
   :ident (fn [] [:neural-network-model/id (:neural-network-model/id props)])})

(defn generate-layers [layers]
  (for [{:neural-network-layer/keys [number-of-neurons type]} layers]
    (dense #js {:units number-of-neurons})))

(defn build-model [{:neural-network-config/keys [problem layers]} num-features]
  (let [model (.sequential tfjs)]
    (.add model (dense #js {:units 20
                            :inputShape #js [1 num-features]}))
    (doseq [layer (generate-layers layers)]
      (.add model layer))
    (case problem
      :classification (.add model (dense #js {:units 1 :activation "sigmoid"}))
      :regression (.add model (dense #js {:units 1 :activation "sigmoid"}))
      (.add model (dense #js {:units 1 :activation "sigmoid"})))
    (.compile model #js {:loss "binaryCrossentropy" :optimizer "adam"})
    (.summary model)
    model))

(def EPOCHS 100)
(def BATCH-SIZE 20)

(def STOP-LOSS 20)
(def STOP-PROFIT 20)

(defn query-trade-result! [timestamp filter-fn]
  (async
   (-> (.-dataset @db)
       (.where "timestamp")
       (.above timestamp)
       (.and filter-fn)
       (.first)
       await
       (js->clj :keywordize-keys true))))

(defn query-trade-result [{:keys [timestamp close]}]
  (async
    (let [result (atom 0)]
      (let [profit (await (query-trade-result! timestamp (fn [item]
                                                           (>= (.-close item) (+ close STOP-PROFIT)))))
            loss (await (query-trade-result! timestamp (fn [item]
                                                         (<= (.-close item) (- close STOP-LOSS)))))]
        (reset! result (if (< (:timestamp profit) (:timestamp loss)) 1 0)))
      (let [profit (await (query-trade-result! timestamp (fn [item]
                                                           (<= (.-close item) (- close STOP-PROFIT)))))
            loss (await (query-trade-result! timestamp (fn [item]
                                                         (>= (.-close item) (+ close STOP-LOSS)))))]
        (reset! result (if (< (:timestamp profit) (:timestamp loss)) -1 0))))))

(defn get-trade-results [data]
  (async
   (await-all
    (vec
     (for [features data]
       (query-trade-result features))))))

(def talib-inited? (atom false))

(defn get-indicators-data [data indicators]
  #_(async
   (when-not @talib-inited?
     (await (.init talib)))
   (-> (for [indicator indicators]
         {indicator (ocall talib indicator (clj->js data))})
       (mapv ))))

(def batch-number (atom 1))
(defn get-next-batch-fn [indicators feature-length training-total batches-per-epoch validation?]
  (let [initial-offset (if validation? training-total 0)]
    #js {:next
         (fn []
           (async
            (let [offset (+ initial-offset (* BATCH-SIZE (dec @batch-number)))
                  limit BATCH-SIZE
                  data (mapv #(dissoc % :id)
                             (-> (.-dataset @db)
                                 (.offset offset)
                                 (.limit limit)
                                 (.toArray)
                                 await
                                 (js->clj :keywordize-keys true)))
                  samples (ocall tfjs :buffer #js [BATCH-SIZE 1 feature-length])
                  targets (ocall tfjs :buffer #js [BATCH-SIZE 1 1])
                  epoch-end? (= batches-per-epoch (dec @batch-number))
                  indicators-data (await (get-indicators-data data indicators))
                  data (vec (map-indexed (fn [idx itm] (merge itm (get indicators-data idx))) data))
                  keys-indexed (map-indexed vector (sort (keys (first data))))
                  trade-results (await (get-trade-results data))]
              (when-not epoch-end?
                (swap! batch-number inc))
              (doseq [[item-index item] (map-indexed (fn [idx itm] [idx itm]) data)]
                (doseq [[key-index key!] keys-indexed]
                  (let [v (key! item)]
                    (ocall samples :set v item-index 0 key-index)))
                (ocall targets :set (get trade-results item-index) item-index 0 0))
              #js {:value #js {:xs (ocall samples :toTensor)
                               :ys (ocall targets :toTensor)}
                   :done  epoch-end?})))}))

(defn train [{:keys [id config dataset]}]
  (async
    (let [
          num-features (+ (count (-> (.-dataset @db)
                                     (.get #js {:id 1})
                                     await
                                     (js->clj :keywordize-keys true)
                                     (dissoc :id)
                                     keys))
                          (count (:dataset/indicators dataset)))
          model (build-model config num-features)
          training-total (int (* (-> (.-dataset @db)
                                     (.count)
                                     await) 0.7))
          batches-per-epoch (dec (dec (int (/ training-total BATCH-SIZE))))
          train-dataset (-> (.-data tfjs)
                            (.generator (fn [] (get-next-batch-fn (:dataset/indicators dataset) num-features training-total batches-per-epoch false))))
          validation-dataset (-> (.-data tfjs)
                                 (.generator (fn [] (get-next-batch-fn (:dataset/indicators dataset) num-features training-total batches-per-epoch true))))]
      (.fitDataset model train-dataset #js {:batchesPerEpoch batches-per-epoch
                                            :epochs          EPOCHS
                                            :callbacks       #js {:onTrainBegin (fn [logs] false)
                                                                  :onTrainEnd   (fn [logs]
                                                                                  (println "train end."))
                                                                  :onEpochEnd   (fn [epoch logs]
                                                                                  (js/console.log epoch)
                                                                                  (js/console.log logs)
                                                                                  (reset! batch-number 1))
                                                                  :onBatchEnd   (fn [batch logs] 
                                                                                   (js/console.log logs))}
                                            :validationData  validation-dataset}))))

(defn train! [model-id]
  ;(println "talib/api: " talib/api)
  (df/load etaira-app [:neural-network-model/id model-id] NeuralNetworkModelDataForTraining
           {:ok-action (fn [{:keys [result]}]
                         (let [model (first (vals (:body result)))]
                           (go
                             (<! (download-history-data model))
                             (train model))))}))

(defn stop! [model-id]
  true)