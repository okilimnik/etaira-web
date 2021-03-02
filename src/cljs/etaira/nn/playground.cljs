(ns etaira.nn.playground
  (:require
   [oops.core :refer [ocall]]
   ["d3" :as d3]))

(def ^:const RECT_SIZE 30)
(def ^:const BIAS_SIZE 5)
(def ^:const NUM_SAMPLES_CLASSIFY 500)
(def ^:const NUM_SAMPLES_REGRESS 1200)
(def ^:const DENSITY 100)

(defn scrollTween [offset]
  (fn []
    (let [i (ocall d3 :interpolateNumber
                   (or js/window.pageYOffset
                       js/document.documentElement.scrollTop)
                   offset)]
      #(js/scrollTo 0 (i %)))))