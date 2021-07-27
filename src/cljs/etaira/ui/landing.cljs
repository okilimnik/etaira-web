(ns etaira.ui.landing
  (:require
   [com.fulcrologic.fulcro.dom :as dom]
   [etaira.ui-ico :refer [ui-main-page MainPage]]
   [com.fulcrologic.rad.routing :as rroute]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(defsc LandingPage [this props]
  {:query         ['*]
   :ident         (fn [] [:component/id ::LandingPage])
   :initial-state (fn [params] (comp/get-initial-state MainPage {}))
   :route-segment ["landing-page"]}
  #_(println "LandingPage props: " props)
  (dom/div (ui-main-page props)))