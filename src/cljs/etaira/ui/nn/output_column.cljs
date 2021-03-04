(ns etaira.ui.nn.output-column
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div button i span label select svg linearGradient stop br rect g defs
                                               option h4 p canvas input]]
   ))

(def stops
  [{:offset "0%" :stop-color "#f59322" :stop-opacity "1"}
   {:offset "50%" :stop-color "#e8eaeb" :stop-opacity "1"}
   {:offset "100%" :stop-color "#0877bd" :stop-opacity "1"}])

(defn output-column []
  (div :.column.output
       (h4 "Output")
       (div :.metrics
            (div :.output-stats.ui-percTrainData
                 (span "Test loss")
                 (div :#loss-test.value))
            (div :.output-stats.train
                 (span "Training loss")
                 (div :#loss-train.value))
            (div :#linechart))
       (div :#heatmap)
       (div {:style
             {:float "left"
              :marginTop "20px"}}
            (div {:style
                  {:display "flex"
                   :alignItems "center"}}
                 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Gradient color scale

                 (div :.label
                      {:style
                       {:width "105px"
                        :marginRight "10px"}}
                      "Colors shows data, neuron and weight values.")
                 (svg :#colormap
                      {:width "150"
                       :height "30"}
                      (defs
                        (linearGradient :#gradient
                                        {:x1 "0%"
                                         :y1 "100%"
                                         :x2 "100%"
                                         :y2 "100%"}
                                        (for [{:keys [offset stop-color stop-opacity]} stops]
                                          (stop {:offset offset} stop-color stop-opacity))))
                      (g :.core
                         {:transform "translate(3, 0)"}
                         (rect {:width "144"
                                :height "10"
                                :style {:fill "url('#gradient')"}}))))
        (br)
        (div {:style
              {:display "flex"}}
             (label :.ui-showTestData.mdl-checkbox.mdl-js-checkbox.mdl-js-ripple-effect
                    {:htmlFor "show-test-data"}
                    (input :#show-test-data.mdl-checkbox__input
                           {:type "checkbox"
                            :checked true})
                    (span :.mdl-checkbox__label.label "Show test data"))
             (label :.ui-discretize.mdl-checkbox.mdl-js-checkbox.mdl-js-ripple-effect
                    {:htmlFor "discretize"}
                    (input :#discretize.mdl-checkbox__input
                           {:type "checkbox"
                            :checked true})
                    (span :.mdl-checkbox__label.label "Discretize output")))))
  )
