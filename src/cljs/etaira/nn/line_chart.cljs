(ns etaira.nn.line-chart
  (:require
   [oops.core :refer [oget ocall]]
   ["d3" :as d3]))

(comment 

         (def min-y (atom js/Number.MAX_VALUE))

         (def max-y (atom js/Number.MIN_VALUE))

         (def margin {:top 2
                      :right 0
                      :bottom 2
                      :left 2})

         (def x-scale (-> d3
                          (ocall :scaleLinear)
                          (ocall :domain #js [0 0])
                          (ocall :range #js [0 width])))

         (defn appending-line-chart [container line-colors]
           (let [num-lines (count line-colors)
                 node (-> container
                          (ocall :node))
                 total-width (-> node
                                 (ocall :offsetWidth))
                 total-height (-> node
                                  (ocall :offsetHeight))
                 margin {:top 2
                         :right 0
                         :bottom 2
                         :left 2}
                 width (- total-width (get margin :left) (get margin :right))
                 height (- total-height (get margin :top) (get margin :bottom))
                 x-scale (-> d3
                             (ocall :scaleLinear)
                             (ocall :domain #js [0 0])
                             (ocall :range #js [0 width]))
                 y-scale (-> d3
                             (ocall :domain #js [0 0])
                             (ocall :range #js [height 0]))
                 svg (-> container
                         (ocall :append "svg")
                         (ocall :attr "width" (+ width (get margin :left) (get margin :right)))
                         (ocall :attr "height" (+ height (get margin :top) (get margin :bottom)))
                         (ocall :append "g")
                         (ocall :attr "transform" (str "translate(" (get margin :left) ", " (get margin :top) ")")))]
             (loop [i 0
                    paths []]
               (let [path (-> svg
                              (ocall :append "path")
                              (ocall :attr "class" "line")
                              (ocall :style #js {"fill" "none" "stroke" (get line-colors i) "stroke-width" "1.5px"}))]
                 (if (< i num-lines)
                   (recur (inc i)
                          (conj paths path))
                   paths)))))

         (defn reset []
           (let [data []
                 min-y js/Number.MAX_VALUE
                 max-y js/Number.MIN_VALUE]
             (redraw)))

         (defn add-data-point [data-point]
           (if-not (= (count data-point) num-lines)
             (throw (js/Error. "Length of dataPoint must equal number of lines"))
             (loop [i 0
                    data []]
               (let [min-y (js/Math.min min-y (get data-point i))
                     max-y (js/Math.max max-y (get data-point i))]
                 (if (< i (count data-point))
                   (recur (inc i)
                          (conj data {:x (+ (count data) 1) :y (get data-point i)}))
                   (redraw))))))

         (defn redraw []
           (let [_ (-> x-scale
                       (ocall :domain #js [1 (count data)]))
                 _ (-> y-scale
                       (ocall :domain #js [min-y max-y]))
                 get-path-map (fn [line-index]
                                (-> d3
                                    (ocall :svg)
                                    (ocall :line #js {:x (fn [d]
                                                           (x-scale (oget d :x)))
                                                      :y (fn [d]
                                                           (y-scale (oget d :y line-index)))})))]
             (loop [i 0]
               (-> (oget paths i)
                   (ocall :datum data)
                   (ocall :attr "d" (aget get-path-map i)))
               (if (< i num-lines)
                 (recur (inc i))
                 ())))))




