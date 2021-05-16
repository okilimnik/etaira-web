(ns etaira.ui.advisor.training.neural-network-model
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.rad.picker-options :as picker-options]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.model.neural-network-model :as neural-network-model]
   [etaira.ui.advisor.training.training :as model-training]))

(defsc DatasetQuery [_ _]
  {:query [:dataset/id :dataset/name]
   :ident :dataset/id})

(defsc NeuralNetworkConfigQuery [_ _]
  {:query [:neural-network-config/id :neural-network-config/name]
   :ident :neural-network-config/id})

(form/defsc-form NeuralNetworkModelForm [this props]
  {fo/id             neural-network-model/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [neural-network-model/name
                      neural-network-model/config
                      neural-network-model/dataset]
   fo/layout         [[:neural-network-model/name]
                      [:neural-network-model/config]
                      [:neural-network-model/dataset]]
   fo/field-styles   {:neural-network-model/dataset :pick-one
                      :neural-network-model/config :pick-one}
   fo/field-options  {:neural-network-model/dataset {::picker-options/query-key       :dataset/all-datasets
                                                     ::picker-options/query-component DatasetQuery
                                                     ::picker-options/options-xform   (fn [_ options] (mapv
                                                                                                       (fn [{:dataset/keys [id name]}]
                                                                                                         {:text name :value [:dataset/id id]})
                                                                                                       (sort-by :dataset/name options)))
                                                     ::picker-options/cache-time-ms   30000}
                      :neural-network-model/config {::picker-options/query-key       :neural-network-config/all-neural-network-configs
                                                    ::picker-options/query-component NeuralNetworkConfigQuery
                                                    ::picker-options/options-xform   (fn [_ options] (mapv
                                                                                                      (fn [{:neural-network-config/keys [id name]}]
                                                                                                        {:text name :value [:neural-network-config/id id]})
                                                                                                      (sort-by :neural-network-config/name options)))
                                                    ::picker-options/cache-time-ms   30000}}
   fo/route-prefix   "neural-network-model"
   fo/title          "Edit Neural Network Model"})

(report/defsc-report NeuralNetworkModelList [this props]
  {ro/title               "All Neural Network Models"
   ro/source-attribute    :neural-network-model/all-neural-network-models
   ro/row-pk              neural-network-model/id
   ro/columns             [neural-network-model/name
                           neural-network-model/state]
   ro/column-formatters   {:neural-network-model/state (fn [this v] (get neural-network-model/states v ""))}
   ro/column-headings     {:neural-network-model/name "Name"}
   ro/controls            {::new-neural-network-model {:label  "New Neural Network Model"
                                                       :type   :button
                                                       :action (fn [this] (form/create! this NeuralNetworkModelForm))}
                           ;::search!       
                           #_{:type   :button
                              :local? true
                              :label  "Filter"
                              :class "ui basic compact mini red button"
                              :action (fn [this _] (report/filter-rows! this))}
                           ;::filter-name   
                           #_{:type        :string
                              :local?      true
                              :placeholder "Type a partial name and press enter."
                              :onChange    (fn [this _] (report/filter-rows! this))}}

   ro/control-layout      {:action-buttons [::new-neural-network-model]
                           ;:inputs         [[::filter-name ::search! :_]]
                           }


   ro/row-actions         [{:label "Train"
                            :action    (fn [report-instance {:neural-network-model/keys [id]}]
                                         (comp/transact! report-instance [(neural-network-model/set-state {:neural-network-model/id id
                                                                                                           :neural-network-model/state :training})])
                                         (model-training/train! id))
                            :visible?  (fn [_ row-props] (not (contains? #{:trained :training} (:neural-network-model/state row-props))))
                            ;:disabled? (fn [_ row-props] (contains? #{"trained" "training"} (:neural-network-model/state row-props)))
                            }
                           {:label "Stop"
                            :action    (fn [report-instance {:neural-network-model/keys [id] :as row-props}]
                                         (comp/transact! report-instance [(neural-network-model/set-state {:neural-network-model/id id
                                                                                                           :neural-network-model/state :not-trained})])
                                         (model-training/stop! id))
                            :visible?  (fn [_ row-props] (contains? #{:trained :training} (:neural-network-model/state row-props)))
                            ;:disabled? (fn [_ row-props] (not (contains? #{"trained" "training"} (:neural-network-model/state row-props))))
                            }

                           {:label  "Delete"
                            :action (fn [this {:neural-network-model/keys [id] :as row}] (form/delete! this :neural-network-model/id id))}
                           {:label  "Edit"
                            :action (fn [this {:neural-network-model/keys [id] :as row}] (form/edit! this NeuralNetworkModelForm id))}]

   ro/run-on-mount?       true
   ro/route               "neural-network-models"})
