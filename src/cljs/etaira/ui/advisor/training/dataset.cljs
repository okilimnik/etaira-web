(ns etaira.ui.advisor.training.dataset
  (:require
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.model.dataset :as dataset]
   [com.fulcrologic.fulcro.ui-state-machines :as uism]
   [oops.core :refer [oget ocall]]
   ["ccxt/dist/ccxt.browser"]
   [etaira.interop.async :refer [async await]]))

(defn fetch-symbols [exchange-id callback]
  (let [exchange-class (oget js/ccxt exchange-id)
        exchange (exchange-class.)]
    (-> (ocall exchange :loadMarkets)
        (.then callback))))

(defn on-change-exchange [exchange app-ish form-class props attribute]
  (fetch-symbols exchange #(picker-options/load-options! app-ish form-class props attribute %))s)

(form/defsc-form DatasetForm [this props]
  {fo/id             dataset/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [dataset/name
                      dataset/exchange
                      dataset/symbols]
   fo/layout         [[:dataset/name]
                      [:dataset/exchange]
                      [:dataset/symbols]]
   fo/triggers        {:on-change (fn [{::uism/keys [state-map] :as uism-env} form-ident k _ new-value]
                                    (case k
                                      :dataset/exchange
                                      (on-change-exchange new-value this DatasetForm props :dataset/symbols)
                                      "do nothing")
                                    uism-env)}
   fo/route-prefix   "dataset"
   fo/title          "Edit Dataset"})

(report/defsc-report DatasetList [this props]
  {ro/title               "All Datasets"
   ro/source-attribute    :dataset/all-datasets
   ro/row-pk              dataset/id
   ro/columns             [dataset/name]
   ro/column-headings     {:dataset/name "Name"}
   ro/controls            {::new-dataset {:label  "New Dataset"
                                          :type   :button
                                          :action (fn [this] (form/create! this DatasetForm))}}
   ro/control-layout      {:action-buttons [::new-dataset]}
   ro/row-actions         [{:label  "Delete"
                            :action (fn [this {:dataset/keys [id]}] (form/delete! this :dataset/id id))}
                           {:label  "Edit"
                            :action (fn [this {:dataset/keys [id]}] (form/edit! this DatasetForm id))}]
   ro/run-on-mount?       true
   ro/route               "datasets"})
