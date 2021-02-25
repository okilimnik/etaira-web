(ns etaira.nn.heatmap
  (:require
   [oops.core :refer [oget ocall]]
   ["d3" :as d3]))

(def heat-map-settings {:show-axes false :no-svg false})

(def NUM_SHADES 30)



(defn heat-map [width num-samples x-domain y-domain container & [user-settings]]
  (let [heat-map []


        num-samples num-samples
        settings (if user-settings
                   user-settings
                   heat-map-settings)
        height width
        padding (if (get user-settings :show-axes)
                  20
                  0)
        x-scale (-> d3
                    (ocall :scaleLinear)
                    (ocall :domain x-domain)
                    (ocall :range #js [0 (- width (* 2 padding))]))
        y-scale (-> d3
                    (ocall :scaleLinear)
                    (ocall :domain y-domain)
                    (ocall :range #js [(- height (* 2 padding)) 0]))
        tmp-scale (-> d3
                      (ocall :range #js ["#f59322" "#e8eaeb" "#0877bd"])
                      (ocall :clamp true))
        colors (-> d3
                   (ocall :range 0 (+ 1 1E-9) (/ 1 NUM_SHADES))
                   (ocall :map (fn [a] (tmp-scale a))))

        color  (-> d3
                   (ocall :scaleQuantize)
                   (ocall :domain #js [-1 1])
                   (ocall :range colors))

        container (-> container
                      (ocall :append "div")
                      (ocall :style #js {:width (str width "px")
                                         :height (str height "px")
                                         :psition "relative"
                                         :top (str (- padding) "px")
                                         :left (str (- padding) "px")}))
        canvas (-> container
                   (ocall :append "canvas")
                   (ocall :attr "width" num-samples)
                   (ocall :attr "height" num-samples)
                   (ocall :style "width" (str (- width (* 2 padding)) "px"))
                   (ocall :style "height" (str (- height (* 2 padding)) "px"))
                   (ocall :style "position" "absolute")
                   (ocall :style "top" (str padding "px"))
                   (ocall :style "left" (str padding "px")))
        svg     (when-not (get settings :no-svg)
                  (-> container
                      (ocall :append "svg")
                      (ocall :attr #js {:width width
                                        :height height})
                      (ocall :style #js {:position "absolute"
                                         :left 0
                                         :top 0})
                      (ocall :append "g")
                      (ocall :attr "transform" (str "translate(" padding "," padding ")"))))
        _        (when svg
                   (-> svg
                       (ocall :append "g")
                       (ocall :attr "class" "train"))
                   (-> svg
                       (ocall :append "g")
                       (ocall :attr "class" "test")))
        _        (if (get settings :show-axes)
                   (let [x-axis (-> d3
                                    (ocall :svg)
                                    (ocall :axis)
                                    (ocall :scale x-scale)
                                    (ocall :orient "bottom"))
                         y-axis (-> d3
                                    (ocall :svg)
                                    (ocall :axis)
                                    (ocall :scale y-scale)
                                    (ocall :orient "right"))]
                     (when svg
                       (-> svg
                           (ocall :append "g")
                           (ocall :attr "class" "x axis")
                           (ocall :attr "transform" (str "translate(" 0 "," (- height (* 2 padding)) ")"))
                           (ocall :call x-axis))
                       (-> svg
                           (ocall :append "g")
                           (ocall :attr "class" "y axis")
                           (ocall :attr "transform" (str "translate(" (- width (* 2 padding)) ",0)"))
                           (ocall :call y-axis))))
                   ())

         update-circles (fn [container points]
          (let [x-domain (-> x-scale
                             (ocall :domain))
                y-domain (-> y-scale
                             (ocall :domain))
                points (-> points
                           (ocall :filter (fn [p]
                                            (and (>= (get p :x) (aget x-domain 0)) (<= (get p :x) (aget x-domain 1))
                                                 (>= (get p :y) (aget y-domain 0)) (<= p (aget y-domain 1))))))
                selection (-> container
                              (ocall :selectAll "circle")
                              (ocall :data points))
                _       (-> selection
                            (ocall :enter)
                            (ocall :append "circle")
                            (ocall :attr "r" 3))
                _       (-> selection
                            (ocall :attr #js {:cx (fn [d]
                                                    (x-scale (oget d :x)))
                                              :cy (fn [d]
                                                    (y-scale (oget d :y)))})
                            (ocall :style "fill" (fn [d]
                                                   (color (oget d :label)))))
                _        (-> selection
                             (ocall :exit)
                             (ocall :remove))]))

        update-test-points (fn [points]
                             (if (get settings :no-svg)
                               (throw (js/Error. "Can't add points since noSvg=true"))
                               (update-circles (-> svg
                                                   (ocall :select "g.test")) points)))
        update-points (fn [points]
                        (if (get settings :no-svg)
                          (throw (js/Error. "Can't add points since noSvg=true"))
                          (update-circles (-> svg
                                              (ocall :select "g.train")) points)))
        update-background (fn [data discretize]
                            (let [dx (count (get data 0))
                                  dy (count data)]
                              (if (or (not (= dx num-samples)) (not (= dy num-samples)))
                                (throw (js/Error. (str "The provided data matrix must be of size "
                                                       "numSamples X numSamples")))
                                (let [context (-> canvas
                                                  (ocall :node)
                                                  (ocall :getContext "2d"))
                                      image (-> context
                                                (ocall :createImageData dx dy))]
                                  (loop [y 0
                                         data (atom [])]
                                    (loop [x 0
                                           result (atom [])]
                                      (let [value (atom (get-in data [x y]))]
                                        (if discretize
                                          (if (>= @value 0)
                                            (reset! value 1)
                                            (reset! value -1))
                                          ())
                                        (let [c (-> d3
                                                    (ocall :rgb (color value)))]
                                          (if (< x dx)
                                            (recur (inc x)
                                                   (swap! result conj (oget c :r) (oget c :g) (oget c :b) 160))
                                            (if (< y dy)
                                              (recur (inc y)
                                                     (swap! data conj @result))
                                              (let [data-js (clj->js data)]
                                                (set! (.-data image) data-js))))))))
                                  _    (-> context
                                           (ocall :putImageData image 0 0))))))]
    (conj heat-map {:settings settings
                    :x-scale x-scale
                    :y-scale y-scale
                    :num-samples num-samples
                    :color color
                    :canvas canvas
                    :svg svg
                    :update-test-points update-test-points
                    :update-points update-points
                    :update-background update-background})))


  
(defn reduce-matrix [matrix factor]
  (if-not (= (count matrix) (count (get matrix 0)))
    (throw (js/Error. "The provided matrix must be a square matrix"))
    (if-not (= (rem (count matrix) factor) 0)
      (throw (js/Error. (str "The width/height of the matrix must be divisible by "
                             "the reduction factor")))
      (loop [i 0
             result []]
        (loop [j 0
               result-i []]
          (let [avg (atom 0)]
            (loop [k 0]
              (loop [l 0]
                (reset! avg (+ @avg (get-in matrix [(+ i k) (+ j l)])))
                (if (< l factor)
                  (recur (inc l))
                  (if (< k factor)
                    (recur (inc k))
                    (reset! avg ((/ @avg (* factor factor))))))))
            (if (< j (count matrix))
              (recur (+ j factor)
                     (conj result-i @avg))
              (if (< i (count matrix))
                (recur (+ i factor)
                       (conj result result-i))
                result))))))))

