(ns etaira.interop.async
  (:require-macros [etaira.interop.async]))

(defn sleep [ms]
  (js/Promise. (fn [resolve]
                 (js/setTimeout resolve ms))))
