(ns etaira.ui.home
  (:require
   [com.fulcrologic.fulcro.dom :as dom :refer [div]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(defsc HomePage [this props]
  {:query         ['*]
   :ident         (fn [] [:component/id ::HomePage])
   :initial-state {}
   :route-segment ["home-page"]}
  (div
   (div "Welcome to Etaira.")))