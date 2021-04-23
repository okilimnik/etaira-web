(ns etaira.ui-ico
  (:require
   [etaira.ui-ico-segments.use-cases :refer [use-cases]]
   [etaira.ui-ico-segments.tokenomics :refer [tokenomics]]
   [etaira.ui-ico-segments.smart-page :refer [smart-page]]
   [etaira.ui-ico-segments.whitepaper :refer [white-paper-b]]
   [com.fulcrologic.fulcro.dom :refer [div]]))

(defn MainPage []
  (div
   (white-paper-b)
   (use-cases)
   (tokenomics)
   (smart-page)
   )
  )