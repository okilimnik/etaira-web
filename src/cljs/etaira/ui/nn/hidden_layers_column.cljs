(ns etaira.ui.nn.hidden-layers-column
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div button i span label select
                                               option h4 p canvas input]]))

(defn hidden-layers-column []
  (div :.column.hidden-layers
       (h4
        (div :.ui-numHiddenLayers
             (button :#add-layers.mdl-button.mdl-js-button.mdl-button--icon
                     (i :.material-icons "add"))
             (button :#remove-layers.mdl-button.mdl-js-button.mdl-button--icon
                     (i :.material-icons "remove")))
        (span :#num-layers)
        (span :#layers-label))
       (div :.bracket))
  )