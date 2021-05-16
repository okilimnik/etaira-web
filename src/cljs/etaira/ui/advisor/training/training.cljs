(ns etaira.ui.advisor.training.training
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [oops.core :refer [oget+ oget ocall]]
   [etaira.interop.async :refer [async await]]
   [konserve.indexeddb :refer [new-indexeddb-store]]
   [konserve.protocols :refer [PEDNAsyncKeyValueStore -exists? -get -update-in -assoc-in -get-meta
                               PBinaryAsyncKeyValueStore -bget -bassoc
                               PStoreSerializer -serialize -deserialize]]
   [cljs.core.async :as async :refer [<!]]
   [com.wsscode.pathom.connect :as pc]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [etaira.app :refer [etaira-app]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(def training-models (atom {}))

(def sleep #(js/Promise. (fn [resolve] (js/setTimeout resolve %))))

(def db (atom nil))

#_(defn fetch-ohlcv [model exchange symbol timeframe date-from date-to]
  (async
   (go
     (reset! db (<! (new-indexeddb-store (str "history-data-" (:neural-network-model/id model)))))
     (-assoc-in @db ["rows"] [] [])
     (loop [date-from! date-from]
       (await (sleep (oget exchange :rateLimit)))
       (when (< date-from! date-to)
         (let [data (await (ocall exchange :fetchOHLCV symbol timeframe date-from 500))]
           (-update-in @db ["rows"] conj data [])
           (println data)
           (recur date-from!)))))))

(defn download-history-data [model]
  (let [{:dataset/keys [exchange cryptopair date-from date-to interval]} (:neural-network-model/dataset model)
        exchange-class (oget+ js/ccxt exchange)
        exchange-obj (exchange-class.)]
    (if (oget exchange-obj "has.fetchOHLCV")
      ;(fetch-ohlcv model exchange-obj cryptopair interval date-from date-to)
      (println "exchange doesn't provide history data"))))


(defsc NeuralNetworkModelDataForTraining [this {:neural-network-model/keys [id] :as props}]
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

(defn train! [model-id]
  (df/load etaira-app [:neural-network-model/id model-id] NeuralNetworkModelDataForTraining
           {:ok-action (fn [{:keys [result]}]
                         (let [model (first (vals (:body result)))]
                           (println "model: " model)
                           (download-history-data model)))}))

(defn stop! [model-id]
  true)