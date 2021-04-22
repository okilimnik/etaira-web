(ns etaira.ui.advisor.training.dataset
  (:require
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.model.dataset :as dataset]))

(defn fetch-symbols [exchange]
  )

(defn on-change-exchange [exchange]
  (reset! dataset/symbols (fetch-symbols exchange)))

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
   fo/triggers        {:on-change (fn [{::uism/keys [state-map] :as uism-env} _ k _ new-value]
                                    (case k
                                      :dataset/symbols
                                      (let [dataset-symbols  (get-in state-map (conj new-value :dataset/symbols))]
                                        (on-change-exchange dataset-symbols))))}
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
