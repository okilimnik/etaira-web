(ns etaira.ui-ico
  (:require
   [etaira.ui-ico-segments.use-cases :refer [use-cases]]
   [etaira.ui-ico-segments.buy-section :refer [ui-buy-section ui-rom Rom]]
   [etaira.ui-ico-segments.tokenomics :refer [tokenomics]]
   [etaira.ui-ico-segments.smart-page :refer [smart-page]]
   [etaira.ui-ico-segments.whitepaper :refer [white-paper-b]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :refer [div]]))

(defsc MainPage [this props]
  {:query         ['*]
   :ident         (fn [] [:component/id ::MainPage])
   :initial-state (fn [params] (comp/get-initial-state Rom {}))}
  (div
   (ui-rom props)
   (ui-buy-section)
   (white-paper-b)
   (use-cases)
   (smart-page)
   (tokenomics)))

(def ui-main-page (comp/factory MainPage))
