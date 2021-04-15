(ns etaira.ui-ico
  (:require
   [etaira.ui-ico-segments.use-cases :refer [use-cases]]
   [etaira.ui-ico-segments.tokenomics :refer [tokenomics]]
   [com.fulcrologic.fulcro.dom :refer [div]]))

(defn MainPage []
  (div
   ;;(comp/factory Root)
   (use-cases)
   ;;(chart)
   (tokenomics)
   ;;(victor)
   )
  )