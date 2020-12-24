(ns etaira.model
  (:require
   [etaira.model.timezone :as timezone]
   [etaira.model.account :as account]
   [com.fulcrologic.rad.attributes :as attr]))

(def all-attributes (vec (concat
                          account/attributes
                          timezone/attributes)))

(def all-attribute-validator (attr/make-attribute-validator all-attributes))
