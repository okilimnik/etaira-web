(ns etaira.ai.indicator
  (:require
   [clojure.walk]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.rad.picker-options :as picker-options]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.schema.indicator :as indicator]
   [etaira.schema.indicator.option :as indicator-option]
   [etaira.schema.indicator.input :as indicator-input]
   [etaira.schema.indicator.output :as indicator-output]))

(defsc IndicatorGroupQuery [_ _]
  {:query [:indicator-group/id :indicator-group/name]
   :ident :indicator-group/id})

(form/defsc-form IndicatorOptionsForm [this props]
  {fo/id             indicator-option/id
   fo/title       "Options"
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [indicator-option/hint
                      indicator-option/default-value]
   fo/layout         [[:indicator-option/hint]
                      [:indicator-option/default-value]]
   fo/field-labels {:indicator-option/hint "Option"}})

(form/defsc-form IndicatorInputsForm [this props]
  {fo/id             indicator-input/id
   fo/title       "Inputs"
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [indicator-input/name]
   fo/layout         [[:indicator-input/name]]})

(form/defsc-form IndicatorOutputsForm [this props]
  {fo/id             indicator-output/id
   fo/title       "Outputs"
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [indicator-output/name]
   fo/layout         [[:indicator-output/name]]})

(form/defsc-form IndicatorForm [this props]
  {fo/id             indicator/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [indicator/name
                      indicator/description
                      indicator/group
                      indicator/options
                      indicator/inputs
                      indicator/outputs]
   fo/layout         [[:indicator/name]
                      [:indicator/description]
                      [:indicator/group]
                      [:indicator/options]
                      [:indicator/inputs]
                      [:indicator/outputs]]
   fo/field-styles  {:indicator/group :pick-one}
   fo/field-options {:indicator/group {::picker-options/query-key       :indicator-group/all-indicator-groups
                                       ::picker-options/query-component IndicatorGroupQuery
                                       ::picker-options/options-xform   (fn [_ options]
                                                                          (mapv
                                                                           (fn [{:indicator-group/keys [id name]}]
                                                                             {:text name :value [:indicator-group/id id]})
                                                                           (sort-by :indicator-group/name options)))
                                       ::picker-options/cache-time-ms   30000}}
   fo/subforms       {:indicator/options {fo/ui IndicatorOptionsForm
                                          fo/can-add?    (fn [_ _] false)}
                      :indicator/inputs {fo/ui         IndicatorInputsForm
                                         fo/can-add?   (fn [_ _] false)}
                      :indicator/outputs {fo/ui         IndicatorOutputsForm
                                          fo/can-add?   (fn [_ _] false)}}
   fo/route-prefix   "indicator"
   fo/title          "Edit Indicator"})

(report/defsc-report IndicatorList [this props]
  {ro/title               "All Indicators"
   ro/source-attribute    :indicator/all-indicators
   ro/row-pk              indicator/id
   ro/columns             [indicator/name]
   ro/column-headings     {:indicator/name "Name"}
   ro/row-actions         [{:label  "Edit"
                            :action (fn [this {:indicator/keys [id]}] (form/edit! this IndicatorForm id))}]
   ro/run-on-mount?       true
   ro/route               "indicators"})
