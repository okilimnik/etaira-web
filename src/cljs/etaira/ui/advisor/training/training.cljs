(ns etaira.ui.advisor.training.training
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   ["@tensorflow/tfjs" :as tf]
   [oops.core :refer [oget+ oget ocall]]
   [konserve.indexeddb :refer [new-indexeddb-store]]
   [konserve.protocols :refer [PEDNAsyncKeyValueStore -exists? -get -update-in -assoc-in -get-meta
                               PBinaryAsyncKeyValueStore -bget -bassoc
                               PStoreSerializer -serialize -deserialize]]
   [cljs.core.async :as async :refer [<! timeout put! chan]]
   [com.wsscode.pathom.connect :as pc]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [etaira.app :refer [etaira-app]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(def training-models (atom {}))
(def tf-layers (.-layers tf))

(def db (atom nil))

(defn fetch-ohlcv [model exchange symbol timeframe date-from date-to]
  (go
    (reset! db (<! (new-indexeddb-store (str "history-data-" (:neural-network-model/id model)))))
    (<! (-assoc-in @db [(:neural-network-model/id model)] (fn [] {:meta "META"}) []))
    ;(-assoc-in @db ["rows"] [] [])
    (loop [date-from! date-from]
      (<! (timeout (oget exchange :rateLimit)))
      (when (and date-from! (= -1 (compare date-from! date-to)))
        (println "date-from: " date-from!)
        (let [ch (chan)]
          (-> (ocall exchange :fetchOHLCV symbol timeframe (.getTime date-from) 500)
              (.then (fn [data]
                       (put! ch (js->clj data)))))
          (let [data (<! ch)
                new-date-from (js/Date. (first (last data)))]
            (println "data: " data)
            (<! (-update-in @db [(:neural-network-model/id model)] (fn [] {:meta "META"}) #(vec (concat % data)) []))
            (when (= (count data) 500)
              (recur new-date-from))))))
    (println "Finished history download")
    (println "from indexeddb: " (<! (-get @db (:neural-network-model/id model))))))

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

(defn get-trade-results [data-id batch-index])

(def batch-number (atom {}))
(defn get-next-batch-fn [data-id feature-length batches-per-epoch training-total validation?]
  (swap! batch-number assoc data-id 1)
  (let [initial-offset (if validation? (int (/ training-total 500)) 0)]
    #js {:next #(let [data (<! (-get @db (str data-id "-" (+ (dec @batch-number) initial-offset))))
                      samples (ocall tf :buffer #js [BATCH-SIZE 1 feature-length])
                      targets (ocall tf :buffer #js [BATCH-SIZE 1])
                      epoch-end? (= batches-per-epoch (dec @batch-number))
                      trade-results (get-trade-results data-id (+ (dec @batch-number) initial-offset))]
                  (when-not epoch-end?
                    (swap! batch-number update data-id inc))
                  (doseq [[sample-index features] (map-indexed (fn [idx itm] [idx itm]) data)]
                    (dotimes [i (count features)]
                      (let [feature-value (get features i)]
                        (ocall samples :set feature-value sample-index 0 i)))
                    (ocall targets :set (get trade-results sample-index) sample-index 0))
                  #js {:value #js {:xs (ocall samples :toTensor)
                                   :ys (ocall targets :toTensor)}
                       :done epoch-end?})}))

(defn train [{:keys [id config dataset]}]
  (go
    (let [num-features (+ (count (first (<! (-get @db (str id "-0")))))
                          (count (:dataset/indicators dataset)))
          model (<! (build-model config num-features))
          training-total (int (* (<! (-get @db (str id "-total"))) 0.7))
          batches-per-epoch (dec (dec (int (/ training-total BATCH-SIZE))))
          train-dataset (-> (oget tf :data)
                            (ocall :generator (fn [] (get-next-batch-fn id num-features training-total batches-per-epoch false))))
          validation-dataset (-> (oget tf :data)
                                 (ocall :generator (fn [] (get-next-batch-fn id num-features training-total batches-per-epoch true))))]
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