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
   [etaira.ui.advisor.training.indicator :refer [IndicatorForm]]
   [com.fulcrologic.fulcro.ui-state-machines :as uism]))

#_(defn fetch-symbols [exchange-id callback]
  (let [exchange-class (oget+ js/ccxt exchange-id)
        exchange (exchange-class.)]
    (-> (ocall exchange :loadMarkets)
        (.then callback))))

(defsc IndicatorQuery [_ _]
  {:query [:indicator/id :indicator/name]
   :ident :indicator/id})

(form/defsc-form DatasetForm [this props]
  {fo/id             dataset/id
   ::form/confirm (fn [message]
                    (js/confirm message))
   fo/attributes     [dataset/name
                      dataset/exchange
                      dataset/cryptopair
                      dataset/interval
                      dataset/date-from
                      dataset/date-to
                      dataset/indicators]
   fo/layout         [[:dataset/name]
                      [:dataset/exchange]
                      [:dataset/cryptopair]
                      [:dataset/interval]
                      [:dataset/date-from]
                      [:dataset/date-to]
                      [:dataset/indicators]]
   fo/triggers        {:on-change (fn [{::uism/keys [state-map fulcro-app] :as uism-env} form-ident k old-value new-value]
                                    #_(let [cls (comp/ident->any fulcro-app form-ident)
                                            exchange-name new-value]
                                        (case k
                                          :dataset/exchange
                                          (fetch-symbols exchange-name
                                                         #(let [cryptopairs (mapv (fn [x]
                                                                                    {:text x
                                                                                     :value x}) (keys (js->clj %)))]
                                                          ;(println "cryptopair: " cryptopairs)
                                                            (comp/transact! cls [(cryptopair/load-cryptopairs {:cryptopairs cryptopairs})])))
                                          "do nothing"))
                                    uism-env)}
   fo/field-styles  {:dataset/cryptopair :sorted-set
                     :dataset/indicators :pick-many}
   fo/field-style-configs {:dataset/cryptopair {:sorted-set/valid-values #{"BTC/USDT"}}}
   fo/field-options {:dataset/indicators {::picker-options/query-key       :indicator/all-indicators
                                          ::picker-options/query-component IndicatorQuery
                                          ::picker-options/options-xform   (fn [_ options]
                                                                             (println "all-indicators: " options)
                                                                             (mapv
                                                                              (fn [{:indicator/keys [id name]}]
                                                                                {:text name :value [:indicator/id id]})
                                                                              (sort-by :indicator/name options)))
                                          ::picker-options/cache-time-ms   30000}}
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
