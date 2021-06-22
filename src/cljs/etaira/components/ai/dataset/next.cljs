(ns etaira.components.ai.dataset.next)

(def batch-number (atom 1))
(defn get-next-batch-fn [dataset-db indicators feature-length training-total batches-per-epoch validation?]
  (let [initial-offset (if validation? training-total 0)]
    #js {:next
         (fn []
           (async
            (let [offset (+ initial-offset (* BATCH-SIZE (dec @batch-number)))
                  limit BATCH-SIZE
                  data (mapv #(dissoc % :id)
                             (-> dataset-db
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