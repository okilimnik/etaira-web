(ns etaira.ui.landing
  (:require
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(defsc LandingPage [this props]
  {:query         ['*]
   :ident         (fn [] [:component/id ::LandingPage])
   :initial-state {}
   :route-segment ["landing-page"]}
  (dom/div "Welcome to Etaira. Please log in."))