(ns etaira.training.core
  (:require
   ["dexie" :refer [Dexie]]
   [oops.core :refer [oget+ oget]]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [etaira.app :refer [etaira-app]]
   [etaira.interop.async :refer [async await]]
   [com.fulcrologic.fulcro.components :refer [defsc]]
   [etaira.training.dataset :refer [download-history-data!]]
   [etaira.training.model :as model]))

(def history (atom {}))

(defn download-history-data-from-exchange! [model]
  (async
   (let [{:dataset/keys [exchange cryptopair date-from date-to interval]} (:ai-model/dataset model)
         _ (println "exchange: " exchange)
         exchange-class (oget+ js/ccxt exchange)
         exchange-obj (exchange-class.)
         model-id (:ai-model/id model)]
     (if (oget exchange-obj "has.fetchOHLCV")
       (do (swap! history assoc model-id (Dexie. model-id))
           (await (download-history-data! (get @history model-id) exchange-obj cryptopair interval date-from date-to)))
       (println "exchange doesn't provide history data")))))

(defsc AIModelDataForTraining [_ props]
  {:query [:ai-model/id
           {:ai-model/config [:ai-config/id
                              :ai-config/learning-rate
                              :ai-config/activation
                              :ai-config/regularization
                              :ai-config/problem
                              {:ai-config/layers [:ai-layer/id
                                                  :ai-layer/number
                                                  :ai-layer/number-of-neurons
                                                  :ai-layer/type]}]}
           {:ai-model/dataset [:dataset/id
                                           :dataset/exchange
                                           :dataset/cryptopair
                                           :dataset/date-from
                                           :dataset/date-to
                                           :dataset/interval
                                           :dataset/indicators]}]
   :ident (fn [] [:ai-model/id (:ai-model/id props)])})

(defn query-model-from-database! [model-id]
  (js/Promise.
   (fn [resolve]
     (df/load etaira-app [:ai-model/id model-id] AIModelDataForTraining
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
     (await (model/train! model (get-dataset-db model-id))))))

(defn stop! [model-id]
  true)