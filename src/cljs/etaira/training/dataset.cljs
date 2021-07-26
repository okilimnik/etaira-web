(ns etaira.training.dataset
  (:require
   [oops.core :refer [oget ocall]]
   [etaira.interop.async :refer [sleep async await]]))

(defn fetch-OHLCV [exchange symbol timeframe date-from!]
  (async
   (-> exchange
       (ocall :fetchOHLCV symbol timeframe date-from! 500)
       await
       js->clj)))

(defn init-dataset-db [db]
  (-> (.version db 1)
      (.stores #js {:dataset "++id, timestamp, open, high, low, close, volume"}))
  (oget db :dataset))

(defn exchange->db [data]
  (clj->js
   (vec
    (for [row data]
      {:timestamp (get row 0)
       :open      (js/parseFloat (get row 1))
       :high      (js/parseFloat (get row 2))
       :low       (js/parseFloat (get row 3))
       :close     (js/parseFloat (get row 4))
       :volume    (js/parseFloat (get row 5))}))))

(defn insert-data-into-dataset-db [dataset-db data]
  (async
   (when (seq data)
     (await (ocall dataset-db :bulkPut (exchange->db data))))))

(defn more-data? [data]
  (= (count data) 500))

(defn download-history-data! [db exchange symbol timeframe date-from date-to]
  (async
   (let [dataset-db (init-dataset-db db)]
     (loop [date-from! (.getTime date-from)]
       (await (sleep (oget exchange :rateLimit)))
       (when (and date-from! (< date-from! (.getTime date-to)))
         (let [data (await (fetch-OHLCV exchange symbol timeframe date-from!))
               new-date-from (js/Date. (first (last data)))]
           (println "data: " data)
           (await (insert-data-into-dataset-db dataset-db data))
           (if (more-data? data)
             (recur new-date-from)
             (println "Finished history download"))))))))