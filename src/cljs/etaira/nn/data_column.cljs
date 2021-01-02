(ns etaira.ui.nn.data-column
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div button i span label select
                                               option h4 p canvas input]]))

(def datasets
  [{:title "Circle" :dataset "circle"}
   {:title "Exclusive or" :dataset "xor"}
   {:title "Gaussian" :dataset "gauss"}
   {:title "Spiral" :dataset "spiral"}
   {:title "Plane" :dataset "reg-plane"}
   {:title "Multi gaussian" :dataset "reg-gauss"}])

(defn data-column []
  (div :.column.data
       (h4 (span "Data"))
       (div :.ui-dataset
            (p "Which dataset do you want to use?")
            (div :.dataset-list
                 (for [{:keys [title dataset]} datasets]
                   (div :.dataset {:title title}
                        (canvas :.data-thumbnail {:data-dataset dataset})))))
       (div
        (div :.ui-percTrainData
             (label {:for "percTrainData"} "Ratio of training to test data:  "
                    (span :.value "XX") "%")
             (p :.slider
                (input :#percTrainData.mdl-slider.mdl-js-slider
                       {:type "range"
                        :min "10"
                        :max "90"
                        :step "10"})))
        (div :.ui-noise
             (label {:for "noise"} "Noise:  "
                    (span :.value "XX"))
             (p :.slider
                (input :#noise.mdl-slider.mdl-js-slider
                       {:type "range"
                        :min "0"
                        :max "50"
                        :step "5"})))
        (div :.ui-batchSize
             (label {:for "batchSize"} "Batch size:  "
                    (span :.value "XX"))
             (p :.slider
                (input :#batchSize.mdl-slider.mdl-js-slider
                       {:type "range"
                        :min "1"
                        :max "30"
                        :step "1"})))
        (button :#data-regen-button.basic-button
                {:title "Regenerate data"} "Regenerate"))))