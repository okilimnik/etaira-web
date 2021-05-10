(ns etaira.model
  (:require
   [etaira.model.timezone :as timezone]
   [etaira.model.account :as account]
   [etaira.model.neural-network-config :as neural-network-config]
   [etaira.model.neural-network-layer :as neural-network-layer]
   [etaira.model.neural-network-model :as neural-network-model]
   [etaira.model.dataset :as dataset]
   [com.fulcrologic.rad.attributes :as attr]))

(def all-attributes (vec (concat
                          account/attributes
                          neural-network-config/attributes
                          neural-network-layer/attributes
                          neural-network-model/attributes
                          dataset/attributes
                          timezone/attributes)))

(def all-attribute-validator (attr/make-attribute-validator all-attributes))
