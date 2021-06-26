(ns etaira.ui.ico
  (:require
   [etaira.ui.ico.use-cases :refer [use-cases]]
   [etaira.ui.ico.tokenomics :refer [tokenomics]]
   [etaira.ui.ico.smart-page :refer [smart-page]]
   [etaira.ui.ico.whitepaper :refer [white-paper]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :refer [div]]))

(defsc IcoPage [this props]
  (div
   (white-paper)
   (use-cases)
   (smart-page)
   (tokenomics)))

(def ui-ico-page (comp/factory IcoPage))
