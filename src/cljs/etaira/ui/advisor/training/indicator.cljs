(ns etaira.ui.advisor.training.indicator
  (:require
   [clojure.walk]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.rad.picker-options :as picker-options]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.model.indicator.indicator :as indicator]
   [com.fulcrologic.fulcro.ui-state-machines :as uism]))

(defsc IndicatorGroupQuery [_ _]
  {:query [:indicator-group/id :indicator-group/name]
   :ident :indicator-group/id})

(form/defsc-form IndicatorForm [this props]
  {fo/id             indicator/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [indicator/name
                      indicator/description]
   fo/layout         [[:indicator/name]
                      [:indicator/description]]

   fo/route-prefix   "indicator"
   fo/title          "Edit Indicator"})

(report/defsc-report IndicatorList [this props]
  {ro/title               "All Indicators"
   ro/source-attribute    :indicator/all-indicators
   ro/row-pk              indicator/id
   ro/columns             [indicator/name]
   ro/column-headings     {:indicator/name "Name"}
   ro/run-on-mount?       true
   ro/route               "indicators"})
