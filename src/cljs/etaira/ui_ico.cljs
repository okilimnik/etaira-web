(ns etaira.ui-ico
  (:require
   [etaira.ui-ico-segments.use-cases :refer [use-cases]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :refer [div]]))

(defsc MainPage [this props]
    (div
     (use-cases)))

(def ui-main-page (comp/factory MainPage))