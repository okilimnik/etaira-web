(ns etaira.ui-ico
  (:require
   [etaira.ui-ico-segments.use-cases :refer [use-cases]]
   [etaira.ui-ico-segments.tokenomics :refer [tokenomics]]
   [etaira.ui-ico-segments.smart-page :refer [smart-page]]
<<<<<<< HEAD
   [etaira.ui-ico-segments.whitepaper :refer [white-paper-b]]
=======
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
>>>>>>> d1ba9d21d68d665e5ffee22d65f149161c6bfd82
   [com.fulcrologic.fulcro.dom :refer [div]]))

(defsc MainPage [this props]
  (div
<<<<<<< HEAD
   (white-paper-b)
   (use-cases)
   (tokenomics)
=======
   (use-cases)
>>>>>>> d1ba9d21d68d665e5ffee22d65f149161c6bfd82
   (smart-page)
   (tokenomics)))

(def ui-main-page (comp/factory MainPage))
