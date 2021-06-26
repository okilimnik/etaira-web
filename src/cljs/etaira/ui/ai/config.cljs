(ns etaira.ui.ai.config
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.rad.picker-options :as picker-options]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.schema.ai.config :as ai-config]
   [etaira.schema.ai.layer :as ai-layer]))

(form/defsc-form AILayerForm [this props]
  {fo/id           ai-layer/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes    [ai-layer/number-of-neurons ai-layer/type]
   fo/route-prefix  "ai-layer"
   fo/title         "Hidden Layers"
   fo/layout        [[:ai-layer/number-of-neurons :ai-layer/type]]})

(form/defsc-form AIConfigForm [this props]
  {fo/id             ai-config/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [ai-config/name
                      ai-config/learning-rate
                      ai-config/activation
                      ai-config/regularization
                      ai-config/problem
                      ai-config/layers]
   fo/layout         [[:ai-config/name]
                      [:ai-config/learning-rate]
                      [:ai-config/activation]
                      [:ai-config/regularization]
                      [:ai-config/problem]
                      [:ai-config/layers]]
   fo/subforms       {:ai-config/layers {fo/ui          AILayerForm
                                         fo/can-delete? (fn [_ _] true)
                                         fo/can-add?    (fn [_ _] true)}}
   fo/route-prefix   "ai-config"
   fo/title          "Edit AI Config"})

(report/defsc-report AIConfigList [this props]
  {ro/title               "All AI Configs"
   ro/source-attribute    :ai-config/all-ai-configs
   ro/row-pk              ai-config/id
   ro/columns             [ai-config/name]

   ;ro/row-query-inclusion [:account/id]
   ;   

   ro/column-headings     {:ai-config/name "Name"}

   ro/controls            {::ai-config {:label  "New AI Config"
                                        :type   :button
                                        :action (fn [this] (form/create! this AIConfigForm))}}

   ro/control-layout      {:action-buttons [::ai-config]}


   ro/row-actions         [{:label  "Delete"
                            :action (fn [this {:ai-config/keys [id] :as row}] (form/delete! this :ai-config/id id))}
                           {:label  "Edit"
                            :action (fn [this {:ai-config/keys [id] :as row}] (form/edit! this AIConfigForm id))}]

   ro/run-on-mount?       true
   ro/route               "ai-configs"})
