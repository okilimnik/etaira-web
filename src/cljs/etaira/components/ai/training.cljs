(ns etaira.ui.advisor.training.training
  (:require
   ["dexie" :refer [Dexie]]
   [oops.core :refer [oget+ oget]]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [etaira.app :refer [etaira-app]]
   [etaira.interop.async :refer [async await]]
   [com.fulcrologic.fulcro.components :refer [defsc]]
   [etaira.ui.advisor.training.exchange :refer [download-history-data!]]
   [etaira.ui.advisor.training.model :refer [train!]]))

(def history (atom {}))

(defn download-history-data-from-exchange! [model]
  (async
   (let [{:dataset/keys [exchange cryptopair date-from date-to interval]} (:neural-network-model/dataset model)
         exchange-class (oget+ js/ccxt exchange)
         exchange-obj (exchange-class.)
         model-id (:neural-network-model/id model)]
     (if (oget exchange-obj "has.fetchOHLCV")
       (do (swap! history assoc model-id (Dexie. model-id))
           (await (download-history-data! (get @history model-id) exchange-obj cryptopair interval date-from date-to)))
       (println "exchange doesn't provide history data")))))

(defsc NeuralNetworkModelDataForTraining [_ props]
  {:query [:neural-network-model/id
           {:neural-network-model/config [:neural-network-config/id
                                          :neural-network-config/learning-rate
                                          :neural-network-config/activation
                                          :neural-network-config/regularization
                                          :neural-network-config/problem
                                          {:neural-network-config/layers [:neural-network-layer/id
                                                                          :neural-network-layer/number
                                                                          :neural-network-layer/number-of-neurons
                                                                          :neural-network-layer/type]}]}
           {:neural-network-model/dataset [:dataset/id
                                           :dataset/exchange
                                           :dataset/cryptopair
                                           :dataset/date-from
                                           :dataset/date-to
                                           :dataset/interval
                                           :dataset/indicators]}]
   :ident (fn [] [:neural-network-model/id (:neural-network-model/id props)])})

(defn query-model-from-database! [model-id]
  (js/Promise.
   (fn [resolve]
     (df/load etaira-app [:neural-network-model/id model-id] NeuralNetworkModelDataForTraining
              {:ok-action (fn [{:keys [result]}]
                            (let [model (first (vals (:body result)))]
                              (resolve model)))}))))

(defn get-dataset-db [model-id]
  (-> @history
      (get model-id)
      (oget :dataset)))

(defn train! [model-id]
  (async
   (let [model (await (query-model-from-database! model-id))]
     (await (download-history-data-from-exchange! model))
     (await (train! model (get-dataset-db model-id))))))

(defn stop! [model-id]
  true)