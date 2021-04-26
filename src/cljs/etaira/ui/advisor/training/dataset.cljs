(ns etaira.ui.advisor.training.dataset
  (:require
   [clojure.walk]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.rad.picker-options :as picker-options]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]
   [etaira.model.dataset :as dataset]
   [etaira.model.cryptopair :as cryptopair]
   [com.fulcrologic.fulcro.ui-state-machines :as uism]
   [com.fulcrologic.fulcro.algorithms.normalized-state :as fns]
   [oops.core :refer [oget+ ocall]]
   ["ccxt/dist/ccxt.browser"]
   [etaira.interop.async :refer [async await]]))

(defn fetch-symbols [exchange-id callback]
  (let [exchange-class (oget+ js/ccxt exchange-id)
        exchange (exchange-class.)]
    (-> (ocall exchange :loadMarkets)
        (.then callback))))

(form/defsc-form CryptopairForm [this props]
  {fo/id            cryptopair/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes    [cryptopair/name]
   fo/route-prefix  "cryptopair"
   fo/title         "Edit Cryptopair"})

(form/defsc-form DatasetForm [this props]
  {fo/id             dataset/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [dataset/name
                      dataset/exchange
                      dataset/cryptopair]
   fo/layout         [[:dataset/name]
                      [:dataset/exchange]
                      [:dataset/cryptopair]]
   fo/triggers        {:on-change (fn [{::uism/keys [state-map fulcro-app] :as uism-env} form-ident k old-value new-value]
                                    (let [cls (comp/ident->any fulcro-app form-ident)
                                          exchange-name new-value]
                                      (case k
                                        :dataset/exchange
                                        (fetch-symbols exchange-name
                                                       #(let [cryptopairs (mapv (fn [x]
                                                                                  {:text x
                                                                                   :value {:cryptopair/id x}}) (keys (js->clj %)))]
                                                          ;(println "cryptopair: " cryptopairs)
                                                          (comp/transact! cls [(cryptopair/load-cryptopairs {:cryptopairs cryptopairs})])))
                                        "do nothing"))
                                    uism-env)}
   fo/field-styles  {:dataset/cryptopair :pick-one}
   fo/field-options {:dataset/cryptopair {::picker-options/query-key        :cryptopair/all-cryptopairs
                                          ::picker-options/query-component  CryptopairForm
                                          ::picker-options/options-xform    (fn [_ options]
                                                                              (mapv
                                                                               (fn [{:cryptopair/keys [id name]}]
                                                                                 {:text name :value {:cryptopair/id id
                                                                                                     :cryptopair/name name}})
                                                                               (sort-by :cryptopair/name options)))
                                          ::picker-options/query-parameters (fn [app form-class props]
                                                                              (let [cryptopair (get props :dataset/cryptopair)]
                                                                                cryptopair))
                                          ::picker-options/cache-time-ms    6000000}}
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
