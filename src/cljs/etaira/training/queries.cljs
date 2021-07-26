(ns etaira.training.queries
  (:require
   ["dexie" :refer [Dexie]]
   [oops.core :refer [oget+ oget ocall]]
   [etaira.interop.async :refer [async await await-all]]))

(defn query-trade-result! [db timestamp filter-fn]
  (async
   (-> db
       (.where "timestamp")
       (.above timestamp)
       (.and filter-fn)
       (.first)
       await
       (js->clj :keywordize-keys true))))

(defn query-trade-result [db stop-profit stop-loss {:keys [timestamp close]}]
  (async
   (let [result (atom 0)]
     (let [profit (await (query-trade-result! db timestamp (fn [item]
                                                             (>= (.-close item) (+ close stop-profit)))))
           loss (await (query-trade-result! db timestamp (fn [item]
                                                           (<= (.-close item) (- close stop-loss)))))]
       (reset! result (if (< (:timestamp profit) (:timestamp loss)) 1 0)))
     (let [profit (await (query-trade-result! db timestamp (fn [item]
                                                             (<= (.-close item) (- close stop-profit)))))
           loss (await (query-trade-result! db timestamp (fn [item]
                                                           (>= (.-close item) (+ close stop-loss)))))]
       (reset! result (if (< (:timestamp profit) (:timestamp loss)) -1 0))))))

(defn query-trade-results [db stop-profit stop-loss data]
  (async
   (await-all
    (vec
     (for [features data]
       (query-trade-result db stop-profit stop-loss features))))))