(ns etaira.components.ai.dataset.dataset
  (:require
   ["dexie" :refer [Dexie]]
   [oops.core :refer [oget+ oget ocall]]
   [etaira.interop.async :refer [async await]]))

(def talib-inited? (atom false))

(defn get-indicators-data [data indicators]
  #_(async
     (when-not @talib-inited?
       (await (.init talib)))
     (-> (for [indicator indicators]
           {indicator (ocall talib indicator (clj->js data))})
         (mapv))))

(defn get-dataset [id]
  (-> @datasets
      (get id)
      (oget :dataset)))