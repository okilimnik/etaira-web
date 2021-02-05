(ns etaira.ui.nn.top-controls
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div button i span label select
                                               option h4 p canvas input]]
   [etaira.nn.dataset :refer [shuffle!]]))

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

(def activations
  [{:value "relu" :label "ReLU"}
   {:value "tanh" :label "Tanh"}
   {:value "sigmoid" :label "Sigmoid"}
   {:value "linear" :label "Linear"}])

(def regularizations
  [{:value "none" :label "None"}
   {:value "L1" :label "L1"}
   {:value "L2" :label "L2"}])

(def problems
  [{:value "classification" :label "Classification"}
   {:value "regression" :label "Regression"}])

(defn top-controls []
  (js/console.log "d3:" js/d3)
  (println (shuffle! [1 2 3 4 5]))
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
                              (for [{:keys [value label]} learning-rates]
                                (option {:value value} label)))))
            (div :.control.ui-activation
                 (label {:for "activations"} "Activation")
                 (div :.select
                      (select :#activations
                              (for [{:keys [value label]} activations]
                                (option {:value value} label)))))
            (div :.control.ui-regularization
                 (label {:for "regularizations"} "Regularization")
                 (div :.select
                      (select :#regularizations
                              (for [{:keys [value label]} regularizations]
                                (option {:value value} label)))))
            (div :.control.ui-problem
                 (label {:for "problem"} "Problem type")
                 (div :.select
                      (select :#problem
                              (for [{:keys [value label]} problems]
                                (option {:value value} label))))))))