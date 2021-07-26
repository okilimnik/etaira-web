(ns etaira.training.batch
  (:require
   ["@tensorflow/tfjs" :as tfjs]
   [oops.core :refer [oget+ oget ocall]]
   [etaira.interop.async :refer [async await await-all]]
   [etaira.training.queries :refer [query-trade-results]]
   [etaira.training.indicators :refer [calc-indicators-data] :as indicators]))

(defn get-next-batch-fn [dataset-db indicators feature-length training-total batches-per-epoch batch-number batch-size stop-profit stop-loss validation?]
  (let [initial-offset (if validation? training-total 0)]
    (indicators/init)
    #js {:next
         (fn []
           (async
            (let [offset (+ initial-offset (* batch-size (dec @batch-number)))
                  limit batch-size
                  data (mapv #(dissoc % :id)
                             (-> dataset-db
                                 (.offset offset)
                                 (.limit limit)
                                 (.toArray)
                                 await
                                 (js->clj :keywordize-keys true)))
                  samples (ocall tfjs :buffer #js [batch-size 1 feature-length])
                  targets (ocall tfjs :buffer #js [batch-size 1 1])
                  epoch-end? (= batches-per-epoch (dec @batch-number))
                  indicators-data (await (calc-indicators-data data indicators))
                  data (vec (map-indexed (fn [idx itm] (merge itm (get indicators-data idx))) data))
                  keys-indexed (map-indexed vector (sort (keys (first data))))
                  trade-results (await (query-trade-results dataset-db stop-profit stop-loss data))]
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