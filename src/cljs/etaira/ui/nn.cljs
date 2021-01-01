(ns etaira.ui.nn
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div button i span label select]]))

(def learning-rates 
  [{:value "0.00001" :label "0.00001"}
   {:value "0.0001" :label "0.0001"}
   {:value "0.001" :label "0.001"}
   {:value "0.003" :label "0.003"}
   {:value "0.01" :label "0.01"}
   {:value "0.03" :label "0.03"}
   {:value "0.1" :label "0.1"}
   {:value "0.3" :label "0.3"}
   {:value "1" :label "1"}
   {:value "3" :label "3"}
   {:value "10" :label "10"}])

(defsc NeuralNetwork [this {}]
  {}
  (div :#top-controls
       (div :.container.l--page
            (div :.timeline-controls
                 (button :#reset-button.mdl-button.mdl-js-button.mdl-button--icon.ui-resetButton
                         {:title "Reset the network"}
                         (i :.material-icons "replay"))
                 (button :#play-pause-button.mdl-button.mdl-js-button.mdl-button--fab.mdl-button--colored.ui-playButton
                         {:title "Run/Pause"}
                         (i :.material-icons "play_arrow")
                         (i :.material-icons "pause"))
                 (button :#next-step-button.mdl-button.mdl-js-button.mdl-button--icon.ui-stepButton
                         {:title "Step"}
                         (i :.material-icons "skip_next")))
            (div :.control
                 (span :.label "Epoch")
                 (span :#iter-number.value))
            (div :.control.ui-learningRate
                 (label {:for "learningRate"} "Learning rate")
                 (div :.select
                      (select :#learningRate
                              {:options learning-rates}))))))

(def ui-neural-network (comp/factory NeuralNetwork {}))