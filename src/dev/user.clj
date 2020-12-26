(ns user
  (:require
   [clojure.spec.alpha :as s]
   [expound.alpha :as expound]
   [etaira.components.server :refer [-main]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))
(-main)