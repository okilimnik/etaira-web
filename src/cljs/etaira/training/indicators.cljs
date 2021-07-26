(ns etaira.training.indicators
  (:require
   ["talib.js" :as talib]
   [oops.core :refer [oget+ oget ocall ocall+]]
   [etaira.interop.async :refer [async await await-all]]))

(defn init []
  (async
   (await (ocall talib :init))))

(defn calc-indicators-data [data indicators]
  (async
   (for [indicator indicators]
     (do (println indicator)
         (ocall+ talib indicator (clj->js data))))))