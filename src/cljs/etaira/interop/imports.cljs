(ns etaira.interop.imports
  (:require
    ["@tensorflow/tfjs" :as tfjs-js]
    ["dexie" :refer [Dexie]]))

(def tf tfjs-js)
(def tf-layers (.-layers tf))
(def dexie Dexie)
(def ccxt js/ccxt)
