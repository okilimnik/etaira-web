(ns etaira.components.queries
  (:require
    [com.fulcrologic.rad.database-adapters.key-value.key-store :as kv-key-store]
    [taoensso.timbre :as log]
    [konserve.core :as k]
    [clojure.core.async :refer [<! <!! go]]))

(defn get-all-accounts
  [{::kv-key-store/keys [store]}
   query-params]
  (go
    (if (:show-inactive? query-params)
      (->> (keys (<! (k/get-in store [:account/id])))
           (mapv (fn [id] {:account/id id})))
      (->> (vals (<! (k/get-in store [:account/id])))
           (filter :account/active?)
           (mapv #(select-keys % [:account/id]))))))

(defn get-all-neural-networks
  [{::kv-key-store/keys [store]}
   query-params]
  (go
    (->> (keys (<! (k/get-in store [:neural-network/id])))
         (mapv (fn [id] {:neural-network/id id})))))

(defn get-all-datasets
  [{::kv-key-store/keys [store]}
   query-params]
  (go
    (->> (keys (<! (k/get-in store [:dataset/id])))
         (mapv (fn [id] {:dataset/id id})))))

(defn get-login-info
  "Get the account name, time zone, and password info via a username (email)."
  [{::kv-key-store/keys [store]}
   username]
  (go
    (let [res (<! (k/get-in store [:account/id]))]
      (when res
        (let [account (->> (vals res)
                           (filter #(= username (:account/email %)))
                           first)]
          (log/warn "account (TZ is key and s/be string)" (:time-zone/zone-id account))
          account)))))