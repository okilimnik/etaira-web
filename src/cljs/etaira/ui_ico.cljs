(ns etaira.ui-ico
  (:require
   [etaira.ui-ico-segments.use-cases :refer [use-cases]]
   [com.fulcrologic.fulcro.dom :refer [div button a p img i svg span h3]]
   [com.fulcrologic.semantic-ui.elements.icon.ui-icon :refer [ui-icon]]))

(defn MainPage []
    (div (use-cases)))