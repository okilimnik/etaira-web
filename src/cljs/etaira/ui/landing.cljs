(ns etaira.ui.landing
  (:require
   [com.fulcrologic.fulcro.dom :as dom]
   [etaira.ui-ico :refer [ui-main-page]]
   [com.fulcrologic.rad.routing :as rroute]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(defsc LandingPage [this props]
  {:query         ['*]
   :ident         (fn [] [:component/id ::LandingPage])
   :initial-state {}
   :route-segment ["landing-page"]}
  (dom/div (ui-main-page)))