(ns etaira.ui.nn
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div button i span label select
                                               option h4 p canvas input]]
   [etaira.ui.nn.top-controls :refer [top-controls]]
   [etaira.ui.nn.data-column :refer [data-column]]
   [etaira.ui.nn.features-column :refer [features-column]]
   [etaira.ui.nn.hidden-layers-column :refer [hidden-layers-column]]
   [etaira.ui.nn.output-column :refer [output-column]]))

(defn main-view []
  (div :#main-part.l--page
       (data-column)
       (features-column)
       (hidden-layers-column)
       (output-column)))

(defsc NeuralNetwork [this {}]
  {}
  (div
   (top-controls)
   (main-view)))

(def ui-neural-network (comp/factory NeuralNetwork {}))