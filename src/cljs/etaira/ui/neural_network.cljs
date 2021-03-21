(ns etaira.ui.neural-network
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.rad.picker-options :as picker-options]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.model.neural-network :as neural-network]
   [etaira.model.neural-network-layer :as neural-network-layer]))

(form/defsc-form NeuralNetworkLayerForm [this props]
  {fo/id            neural-network-layer/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes    [neural-network-layer/number-of-neurons neural-network-layer/type]
   fo/route-prefix  "neural-network-layer"
   fo/title         "Neural Network Layer"
   fo/layout        [[:neural-network-layer/number-of-neurons :neural-network-layer/type]]})

(form/defsc-form NeuralNetworkForm [this props]
  {fo/id             neural-network/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [neural-network/name
                      neural-network/learning-rate
                      neural-network/activation
                      neural-network/regularization
                      neural-network/problem
                      neural-network/layers]
   fo/layout         [[:neural-network/name]
                      [:neural-network/learning-rate]
                      [:neural-network/activation]
                      [:neural-network/regularization]
                      [:neural-network/problem]
                      [:neural-network/layers]]
   fo/subforms       {:neural-network/layers {fo/ui          NeuralNetworkLayerForm
                                              fo/can-delete? (fn [_ _] true)
                                              fo/can-add?    (fn [_ _] true)}}
   fo/route-prefix   "neural-network"
   fo/title          "Edit Neural Network"})

(report/defsc-report NeuralNetworkList [this props]
  {ro/title               "All Neural Networks"
   ro/source-attribute    :neural-network/all-neural-networks
   ro/row-pk              neural-network/id
   ro/columns             [neural-network/name]

   ;ro/row-query-inclusion [:account/id]
   ;   

   ro/column-headings     {:neural-network/name "Name"}

   ro/controls            {::new-neural-network {:label  "New Neural Network"
                                                 :type   :button
                                                 :action (fn [this] (form/create! this NeuralNetworkForm))}}

   ro/control-layout      {:action-buttons [::new-neural-network]}

   ro/row-actions         [{:label  "Delete"
                            :action (fn [this {:neural-network/keys [id] :as row}] (form/delete! this :neural-network/id id))}]

  ; ro/form-links          {:invoice/total NeuralNetworkForm}

   ro/run-on-mount?       true
   ro/route               "neural-networks"})
