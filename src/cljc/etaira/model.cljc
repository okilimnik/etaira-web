(ns etaira.model
  (:require
   [etaira.model.timezone :as timezone]
   [etaira.model.account :as account]
   [etaira.model.neural-network-config :as neural-network-config]
   [etaira.model.neural-network-layer :as neural-network-layer]
   [etaira.model.neural-network-model :as neural-network-model]
   [etaira.model.dataset :as dataset]
   [etaira.model.indicator.indicator :as indicator]
   [etaira.model.indicator.group :as indicator-group]
   [etaira.model.indicator.input :as indicator-input]
   [etaira.model.indicator.option-range :as indicator-option-range]
   [etaira.model.indicator.option :as indicator-option]
   [etaira.model.indicator.output :as indicator-output]
   [com.fulcrologic.rad.attributes :as attr]))

(def all-attributes (vec (concat
                          account/attributes
                          neural-network-config/attributes
                          neural-network-layer/attributes
                          neural-network-model/attributes
                          dataset/attributes
                          timezone/attributes
                          indicator/attributes
                          indicator-group/attributes
                          indicator-input/attributes
                          indicator-option-range/attributes
                          indicator-option/attributes
                          indicator-output/attributes
                          )))

(def all-attribute-validator (attr/make-attribute-validator all-attributes))
