(ns etaira.model
  (:require
   [etaira.model.timezone :as timezone]
   [etaira.model.account :as account]
   [etaira.model.neural-network :as neural-network]
   [etaira.model.neural-network-layer :as neural-network-layer]
   [etaira.model.dataset :as dataset]
   [etaira.model.cryptopair :as cryptopair]
   [com.fulcrologic.rad.attributes :as attr]))

(def all-attributes (vec (concat
                          account/attributes
                          neural-network/attributes
                          neural-network-layer/attributes
                          dataset/attributes
                          cryptopair/attributes
                          timezone/attributes)))

(def all-attribute-validator (attr/make-attribute-validator all-attributes))
