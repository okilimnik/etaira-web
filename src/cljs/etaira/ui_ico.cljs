(ns etaira.ui-ico
  (:require
   [etaira.ui-ico-segments.use-cases :refer [use-cases]]
   [etaira.ui-ico-segments.tokenomics :refer [tokenomics]]
   [com.fulcrologic.fulcro.dom :refer [div button a p img i svg span h3]]
   [com.fulcrologic.semantic-ui.elements.icon.ui-icon :refer [ui-icon]]
   [com.fulcrologic.fulcro.components :as comp]))

(defn MainPage []
  (div
   ;;(comp/factory Root)
   (use-cases)
   ;;(chart)
   (tokenomics)
   ;;(victor)
   )
  )