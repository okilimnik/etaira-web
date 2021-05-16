(ns etaira.app
  (:require
   [com.fulcrologic.rad.application :as rad-app]))

(defonce etaira-app (rad-app/fulcro-rad-app {}))