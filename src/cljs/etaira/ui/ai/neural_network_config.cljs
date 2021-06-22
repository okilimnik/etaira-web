(ns etaira.ui.advisor.training.neural-network-config
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.rad.picker-options :as picker-options]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.model.neural-network-config :as neural-network-config]
   [etaira.model.neural-network-layer :as neural-network-layer]))

(form/defsc-form NeuralNetworkLayerForm [this props]
  {fo/id            neural-network-layer/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes    [neural-network-layer/number-of-neurons neural-network-layer/type]
   fo/route-prefix  "neural-network-layer"
   fo/title         "Hidden Layers"
   fo/layout        [[:neural-network-layer/number-of-neurons :neural-network-layer/type]]})

(form/defsc-form NeuralNetworkConfigForm [this props]
  {fo/id             neural-network-config/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [neural-network-config/name
                      neural-network-config/learning-rate
                      neural-network-config/activation
                      neural-network-config/regularization
                      neural-network-config/problem
                      neural-network-config/layers]
   fo/layout         [[:neural-network-config/name]
                      [:neural-network-config/learning-rate]
                      [:neural-network-config/activation]
                      [:neural-network-config/regularization]
                      [:neural-network-config/problem]
                      [:neural-network-config/layers]]
   fo/subforms       {:neural-network-config/layers {fo/ui          NeuralNetworkLayerForm
                                                     fo/can-delete? (fn [_ _] true)
                                                     fo/can-add?    (fn [_ _] true)}}
   fo/route-prefix   "neural-network-config"
   fo/title          "Edit Neural Network Config"})

(report/defsc-report NeuralNetworkConfigList [this props]
  {ro/title               "All Neural Network Configs"
   ro/source-attribute    :neural-network-config/all-neural-network-configs
   ro/row-pk              neural-network-config/id
   ro/columns             [neural-network-config/name]

   ;ro/row-query-inclusion [:account/id]
   ;   

   ro/column-headings     {:neural-network-config/name "Name"}

   ro/controls            {::new-neural-network-config {:label  "New Neural Network Config"
                                                        :type   :button
                                                        :action (fn [this] (form/create! this NeuralNetworkConfigForm))}}

   ro/control-layout      {:action-buttons [::new-neural-network-config]}


   ro/row-actions         [{:label  "Delete"
                            :action (fn [this {:neural-network-config/keys [id] :as row}] (form/delete! this :neural-network-config/id id))}
                           {:label  "Edit"
                            :action (fn [this {:neural-network-config/keys [id] :as row}] (form/edit! this NeuralNetworkConfigForm id))}]

   ro/run-on-mount?       true
   ro/route               "neural-network-configs"})
