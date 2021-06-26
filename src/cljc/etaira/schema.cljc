(ns etaira.schema
  (:require
   [etaira.schema.timezone :as timezone]
   [etaira.schema.account :as account]
   [etaira.schema.ai.config :as ai-config]
   [etaira.schema.ai.layer :as ai-layer]
   [etaira.schema.ai.model :as ai-model]
   [etaira.schema.dataset :as dataset]
   [etaira.schema.indicator :as indicator]
   [etaira.schema.indicator.group :as indicator-group]
   [etaira.schema.indicator.input :as indicator-input]
   [etaira.schema.indicator.option-range :as indicator-option-range]
   [etaira.schema.indicator.option :as indicator-option]
   [etaira.schema.indicator.output :as indicator-output]
   [com.fulcrologic.rad.attributes :as attr]))

(def all-attributes (vec (concat
                          account/attributes
                          ai-config/attributes
                          ai-layer/attributes
                          ai-model/attributes
                          dataset/attributes
                          timezone/attributes
                          indicator/attributes
                          indicator-group/attributes
                          indicator-input/attributes
                          indicator-option-range/attributes
                          indicator-option/attributes
                          indicator-output/attributes)))

(def all-attribute-validator (attr/make-attribute-validator all-attributes))
