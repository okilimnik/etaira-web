(ns etaira.nn.dataset
  (:require
   [oops.core :refer [oget ocall]]
   ["d3" :as d3]))


(defn shuffle! [arr]
  (let [counter (atom (count arr)) 
        index (atom 0)
        result (atom arr)]
    (while (> @counter 0)
      (reset! index (int (* (rand) @counter)))
      (swap! counter dec)
      (reset! result (replace {(get @result @counter) (get @result @index),
                               (get @result @index) (get @result @counter)} @result) ))
    @result))

(defn rand-uniform [a b]
  (+ (* (js/Math.random) (- b a)) a))

(defn normal-random [& [mean variance]]
  (let [mean (or mean 0)
        variance (or variance 1)
        get-s (fn []
                (loop []
                  (let [result (atom {})
                        v1 (- (* 2 (js/Math.random)) 1)
                        v2 (- (* 2 (js/Math.random)) 1)
                        s (+ (* v1 v2) (* v1 v2))]
                    (if (or (<= s 0) (> s 1))
                      (recur)
                      (reset! result {:v1 v1 :s s})))))
        s (get (get-s) :s)
        v1 (get (get-s) :v1)
        result (* v1 (js/Math.sqrt (/ (* -2 (js/Math.log s)) s)))]
    (+ mean (* (js/Math.sqrt variance) result))))

(defn dist [a b]
  (let [dx (- (:x a) (:x b))
        dy (- (:y a) (:y b))]
    (js/Math.sqrt (+ (* dx dx) (* dy dy)))))

(defn regress-plane [num-samples noise]
  (let [radius 6
        label-scale (-> d3
                        (ocall :scaleLinear)
                        (ocall :domain #js [-10 10])
                        (ocall :range #js [-1 1]))
        get-label (fn [x y] (label-scale (+ x y)))]
    (loop [i 0
           result []]
      (let [x (rand-uniform (- radius) radius)
            y (rand-uniform (- radius) radius)
            noise-x (* (rand-uniform (- radius) radius) noise)
            noise-y (* (rand-uniform (- radius) radius) noise)
            label (get-label (+ x noise-x) (+ y noise-y))]
        (if (< i num-samples)
          (recur (inc i) (conj result {:x x :y y :label label}))
          result)))))


(defn classify-two-gauss-data [num-samples noise]
  (let [variance-scale (-> d3
                           (ocall :scaleLinear)
                           (ocall :domain #js [0 0.5])
                           (ocall :range #js [0.5 4]))
        variance (variance-scale noise)
        gen-gauss (fn [cx cy label points]
                    (loop [i 0
                           result points]
                      (let [x (normal-random cx variance)
                            y (normal-random cy variance)]
                        (if (< i (/ num-samples 2))
                          (recur (inc i) (conj result {:x x :y y :label label}))
                          result))))]
    (->> []
         (gen-gauss 2 2 1)
         (gen-gauss -2 -2 -1))))

(defn regress-gaussian [num-samples noise]
  (let [label-scale (-> d3
                        (ocall :scaleLinear)
                        (ocall :domain #js [0 2])
                        (ocall :range #js [1 0])
                        (ocall :clamp #js ["true"]))
        gaussians [[-4 2.5 1]
                   [0 2.5 -1]
                   [4 2.5 1]
                   [-4 -2.5 -1]
                   [0 -2.5 1]
                   [4 -2.5 -1]]
        get-label (fn [x y]
                    (let [label (atom 0)]
                      (loop [i 0]
                        (let [new-label (* (get-in gaussians [i 2]) (label-scale (dist {:x x :y y} 
                                                                                       {:x (get-in gaussians [i 0])
                                                                                        :y (get-in gaussians [i 1])})))]
                          (if (> (js/Math.abs new-label) (js/Math.abs label))
                            (reset! label new-label)
                            ())
                          (if (< i (count gaussians))
                            (recur (inc i))
                            @label)))))
        radius 6]
        (loop [i 0
               result []]
          (let [x (rand-uniform (- radius) radius)
                y (rand-uniform (- radius) radius)
                noise-x (* (rand-uniform (- radius) radius) noise)
                noise-y (* (rand-uniform (- radius) radius) noise)
                label (get-label (+ x noise-x) (+ y noise-y))]
            (if (< i num-samples)
              (recur (inc i) (conj result {:x x :y y :label label}))
              result)))))

(defn classify-spiral-data [num-samples noise]
  (let [n (/ num-samples 2)
        gen-spiral (fn [delta-t label points]
                     (loop [i 0
                            result points]
                       (let [r (* (/ i n) 5)
                             t (+ (* (* (/ (* 1.75 i) n) 2) js/Math.PI) delta-t)
                             x (+ (* r (js/Math.sin t)) (* (rand-uniform -1 1) noise))
                             y (+ (* r (js/Math.cos t)) (* (rand-uniform -1 1) noise))]
                         (if (< i n)
                           (recur (inc i) (conj result {:x x :y y :label label}))
                           result))))]
    (->> []
         (gen-spiral 0 1)
         (gen-spiral js/Math.PI -1))))

(defn classify-circle-data [num-samples noise]
  (let [radius 5
        get-circle-label (fn [p center]
                           (if (< (dist p center) (* radius 0.5))
                             1
                             -1))
        ;; Generate positive points inside the circle.
        inside-points (for [i (range (/ num-samples 2))]
                        (let [r (rand-uniform 0 (* radius 0.5))
                              angle (rand-uniform 0 (* 2 js/Math.PI))
                              x (* r (js/Math.sin angle))
                              y (* r (js/Math.cos angle))
                              noise-x (* (rand-uniform (- radius) radius) noise)
                              noise-y (* (rand-uniform (- radius) radius) noise)
                              label (get-circle-label {:x (+ x noise-x) :y (+ y noise-y)} {:x 0 :y 0})]
                          {:x x :y y :label label}))
        ;; Generate negative points outside the circle.
        outside-points (for [i (range (/ num-samples 2))]
                         (let [r (rand-uniform (* radius 0.7) radius)
                               angle (rand-uniform 0 (* 2 js/Math.PI))
                               x (* r (js/Math.sin angle))
                               y (* r (js/Math.cos angle))
                               noise-x (* (rand-uniform (- radius) radius) noise)
                               noise-y (* (rand-uniform (- radius) radius) noise)
                               label (get-circle-label {:x (+ x noise-x) :y (+ y noise-y)} {:x 0 :y 0})]
                           {:x x :y y :label label}))]
    (vec (concat inside-points outside-points))))

(defn classify-xor-data [num-samples noise]
  (let [get-xor-label (fn [p]
                          (if (>= (* (get p :x) (get p :y)) 0)
                            1
                            -1))]
    (loop [i 0
           result []]
      (let [x (atom (rand-uniform -5 5))
            padding 0.3
            y (atom (rand-uniform -5 5))
            noise-x (* (rand-uniform -5 5) noise)
            noise-y (* (rand-uniform -5 5) noise)
            label (atom 0)]
        (if (> @x 0)
          (reset! x (+ @x padding))
          (reset! x (- @x padding)))
        (if (> @y 0)
          (reset! y (+ @y padding))
          (reset! y (- @y padding)))
        (reset! label (get-xor-label {:x (+ @x noise-x) :y (+ @y noise-y)}))
        (if (< i num-samples)
          (recur (inc i) (conj result {:x @x :y @y :label @label}))
          result)))))


(def tr 5)
(def rt 1)

(defn testo [x y]
  (let [xy (* x y)]
    xy))
