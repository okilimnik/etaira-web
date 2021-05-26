(ns etaira.ui.advisor.training.training
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   ["@tensorflow/tfjs" :as tf]
   [oops.core :refer [oget+ oget ocall]]
   ["dexie" :refer [Dexie]]
   [cljs.core.async :as async :refer [<! timeout put! chan]]
   [com.wsscode.pathom.connect :as pc]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [etaira.app :refer [etaira-app]]
   [etaira.interop.async :refer [async await]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(def training-models (atom {}))
(def tf-layers (.-layers tf))

(def db (atom nil))

(defn fetch-ohlcv [model exchange symbol timeframe date-from date-to]
  (go
    (reset! db (Dexie. (:neural-network-model/id model)))
    (-> @db
        (ocall :version 1)
        (ocall :stores #js {:dataset "++id, open-time, open, high, low, close, volume"}))
    (let [ch (chan)]
      (loop [date-from! date-from]
        (<! (timeout (oget exchange :rateLimit)))
        (when (and date-from! (= -1 (compare date-from! date-to)))
          (println "date-from: " date-from!)
          (-> (ocall exchange :fetchOHLCV symbol timeframe (.getTime date-from) 500)
              (.then (fn [data]
                       (put! ch (js->clj data)))))
          (let [data (<! ch)
                new-date-from (js/Date. (first (last data)))]
            (println "data: " data)
            (when (seq data)
              (-> @db
                  (oget :dataset)
                  (ocall :bulkPut
                         (clj->js
                          (vec
                           (for [row data]
                             {:timestamp (get row 0)
                              ;:open (js/parseFloat (get row 1))
                              ;:high (js/parseFloat (get row 2))
                              ;:low (js/parseFloat (get row 3))
                              :close (js/parseFloat (get row 4))
                              :volume (js/parseFloat (get row 5))}))))
                  (.then #(put! ch 1)))
              (<! ch))
            (when (= (count data) 500)
              (recur new-date-from))))))
    (println "Finished history download")))

(defn download-history-data [model]
  (let [{:dataset/keys [exchange cryptopair date-from date-to interval]} (:neural-network-model/dataset model)
        exchange-class (oget+ js/ccxt exchange)
        exchange-obj (exchange-class.)]
    (if (oget exchange-obj "has.fetchOHLCV")
      (fetch-ohlcv model exchange-obj cryptopair interval date-from date-to)
      (println "exchange doesn't provide history data"))))


(defsc NeuralNetworkModelDataForTraining [this {:neural-network-model/keys [id] :as props}]
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
    (ocall tf-layers :dense #js {:units number-of-neurons})))

(defn build-model [{:neural-network-config/keys [problem layers]} num-features]
  (go
    (let [model (ocall tf :sequential)]
      (ocall model :add (ocall tf-layers :dense (clj->js {:inputShape [1 num-features]})))
      (doseq [layer (generate-layers layers)]
        (ocall model :add layer))
      (case problem
        :classification (ocall model :add (ocall tf-layers :dense #js {:units 1 :activation "sigmoid"}))
        :regression (ocall model :add (ocall tf-layers :dense #js {:units 1 :activation "sigmoid"}))
        (ocall model :add (ocall tf-layers :dense #js {:units 1 :activation "sigmoid"})))
      (ocall model :compile #js {:loss "binaryCrossentropy" :optimizer "adam"})
      (ocall model :summary))))

(def EPOCHS 100)
(def BATCH-SIZE 20)

(def STOP-LOSS 20)
(def STOP-PROFIT 20)

(defn query-trade-result [{:keys [timestamp close]}]
  (let [result (atom 0)]
    (let [profit (await (-> @db
                            (oget :dataset)
                            (ocall :where "timestamp")
                            (ocall :above timestamp)
                            (ocall :where "close")
                            (ocall :aboveOrEqual (+ close STOP-PROFIT))
                            (ocall :sortBy timestamp)
                            (ocall :first)))
          loss (await (-> @db
                          (oget :dataset)
                          (ocall :where "timestamp")
                          (ocall :above timestamp)
                          (ocall :where "close")
                          (ocall :belowOrEqual (- close STOP-LOSS))
                          (ocall :sortBy timestamp)
                          (ocall :first)))]
      (reset! result (if (< (:timestamp profit) (:timestamp loss)) 1 0)))
    (let [profit (await (-> @db
                            (oget :dataset)
                            (ocall :where "timestamp")
                            (ocall :above timestamp)
                            (ocall :where "close")
                            (ocall :belowOrEqual (- close STOP-PROFIT))
                            (ocall :sortBy timestamp)
                            (ocall :first)))
          loss (await (-> @db
                          (oget :dataset)
                          (ocall :where "timestamp")
                          (ocall :above timestamp)
                          (ocall :where "close")
                          (ocall :aboveOrEqual (+ close STOP-LOSS))
                          (ocall :sortBy timestamp)
                          (ocall :first)))]
      (reset! result (if (< (:timestamp profit) (:timestamp loss)) -1 0)))))

(defn get-trade-results [data]
  (async
   (vec
    (for [features data]
      (query-trade-result features)))))

(def batch-number (atom {}))
(defn get-next-batch-fn [feature-length training-total batches-per-epoch validation?]
  (let [initial-offset (if validation? training-total 0)]
    #js {:next
         (async
          (fn []
            (let [offset (+ initial-offset (* BATCH-SIZE (dec @batch-number)))
                  limit BATCH-SIZE
                  data (await (-> @db
                                  (oget :dataset)
                                  (ocall :offset offset)
                                  (ocall :limit limit)
                                  (ocall :toArray)))
                  samples (ocall tf :buffer #js [BATCH-SIZE 1 feature-length])
                  targets (ocall tf :buffer #js [BATCH-SIZE 1])
                  keys-indexed (map-indexed (fn [idx itm] [idx itm])
                                            (sort (keys (first data))))
                  epoch-end? (= batches-per-epoch (dec @batch-number))
                  trade-results (get-trade-results data)]
              (when-not epoch-end?
                (swap! batch-number inc))
              (doseq [[item-index item] (map-indexed (fn [idx itm] [idx itm]) data)]
                (doseq [[key-index key!] keys-indexed]
                  (let [v (key! item)]
                    (ocall samples :set v item-index 0 key-index)))
                (ocall targets :set (get trade-results item-index) item-index 0))
              #js {:value #js {:xs (ocall samples :toTensor)
                               :ys (ocall targets :toTensor)}
                   :done epoch-end?})))}))

(defn train [{:keys [id config dataset]}]
  (go
    (let [num-features (+ (count (-> @db
                                     (oget :dataset)
                                     (ocall :first)
                                     js->clj
                                     keys))
                          (count (:dataset/indicators dataset)))
          model (<! (build-model config num-features))
          training-total (int (* (-> @db
                                     (oget :dataset)
                                     (ocall :count)) 0.7))
          batches-per-epoch (dec (dec (int (/ training-total BATCH-SIZE))))
          train-dataset (-> (oget tf :data)
                            (ocall :generator (fn [] (get-next-batch-fn num-features training-total batches-per-epoch false))))
          validation-dataset (-> (oget tf :data)
                                 (ocall :generator (fn [] (get-next-batch-fn num-features training-total batches-per-epoch true))))]
      (ocall model :fitDataset train-dataset (clj->js {:batchesPerEpoch batches-per-epoch
                                                       :epochs          EPOCHS
                                                       :callbacks       {:onTrainBegin (fn [logs] false)
                                                                         :onTrainEnd   (fn [logs]
                                                                                         (println "train end."))
                                                                         :onEpochEnd   (fn [epoch logs] (swap! batch-number assoc id 1))
                                                                         :onBatchEnd   (fn [batch logs] false)}
                                                       :validationData  validation-dataset})))))

(defn train! [model-id]
  (df/load etaira-app [:neural-network-model/id model-id] NeuralNetworkModelDataForTraining
           {:ok-action (fn [{:keys [result]}]
                         (let [model (first (vals (:body result)))]
                           (println "model: " model)
                           (download-history-data model)
                           (train model)))}))

(defn stop! [model-id]
  true)