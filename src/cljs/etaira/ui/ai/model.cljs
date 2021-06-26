(ns etaira.ui.ai.model
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.rad.picker-options :as picker-options]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.schema.ai.model :as ai-model]
   [etaira.training.core :as model-training]))

(defsc DatasetQuery [_ _]
  {:query [:dataset/id :dataset/name]
   :ident :dataset/id})

(defsc AIConfigQuery [_ _]
  {:query [:ai-config/id :ai-config/name]
   :ident :ai-config/id})

(form/defsc-form AIModelForm [this props]
  {fo/id             ai-model/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [ai-model/name
                      ai-model/config
                      ai-model/dataset]
   fo/layout         [[:ai-model/name]
                      [:ai-model/config]
                      [:ai-model/dataset]]
   fo/field-styles   {:ai-model/dataset :pick-one
                      :ai-model/config :pick-one}
   fo/field-options  {:ai-model/dataset {::picker-options/query-key       :dataset/all-datasets
                                         ::picker-options/query-component DatasetQuery
                                         ::picker-options/options-xform   (fn [_ options] (mapv
                                                                                           (fn [{:dataset/keys [id name]}]
                                                                                             {:text name :value [:dataset/id id]})
                                                                                           (sort-by :dataset/name options)))
                                         ::picker-options/cache-time-ms   30000}
                      :ai-model/config {::picker-options/query-key       :ai-config/all-ai-configs
                                        ::picker-options/query-component AIConfigQuery
                                        ::picker-options/options-xform   (fn [_ options]
                                                                           (mapv
                                                                            (fn [{:ai-config/keys [id name]}]
                                                                              {:text name :value [:ai-config/id id]})
                                                                            (sort-by :ai-config/name options)))
                                        ::picker-options/cache-time-ms   30000}}
   fo/route-prefix   "ai-model"
   fo/title          "Edit AI Model"})

(report/defsc-report AIModelList [this props]
  {ro/title               "All AI Models"
   ro/source-attribute    :ai-model/all-ai-models
   ro/row-pk              ai-model/id
   ro/columns             [ai-model/name
                           ai-model/state]
   ro/column-formatters   {:ai-model/state (fn [this v] (get ai-model/states v ""))}
   ro/column-headings     {:ai-model/name "Name"}
   ro/controls            {::new-ai-model {:label  "New AI Model"
                                           :type   :button
                                           :action (fn [this] (form/create! this AIModelForm))}
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

   ro/control-layout      {:action-buttons [::new-ai-model]
                           ;:inputs         [[::filter-name ::search! :_]]
                           }


   ro/row-actions         [{:label "Train"
                            :action    (fn [report-instance {:ai-model/keys [id]}]
                                         (comp/transact! report-instance [(ai-model/set-state {:ai-model/id id
                                                                                               :ai-model/state :training})])
                                         (model-training/train! id))
                            :visible?  (fn [_ row-props] (not (contains? #{:trained :training} (:ai-model/state row-props))))
                            ;:disabled? (fn [_ row-props] (contains? #{"trained" "training"} (:ai-model/state row-props)))
                            }
                           {:label "Stop"
                            :action    (fn [report-instance {:ai-model/keys [id] :as row-props}]
                                         (comp/transact! report-instance [(ai-model/set-state {:ai-model/id id
                                                                                                           :ai-model/state :not-trained})])
                                         (model-training/stop! id))
                            :visible?  (fn [_ row-props] (contains? #{:trained :training} (:ai-model/state row-props)))
                            ;:disabled? (fn [_ row-props] (not (contains? #{"trained" "training"} (:ai-model/state row-props))))
                            }

                           {:label  "Delete"
                            :action (fn [this {:ai-model/keys [id] :as row}] (form/delete! this :ai-model/id id))}
                           {:label  "Edit"
                            :action (fn [this {:ai-model/keys [id] :as row}] (form/edit! this AIModelForm id))}]

   ro/run-on-mount?       true
   ro/route               "ai-models"})
