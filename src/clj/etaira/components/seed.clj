(ns etaira.components.seed
  (:require
   [clojure.core.async :refer [<!!]]
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [konserve.core :as k]
   [com.fulcrologic.guardrails.core :refer [>defn =>]]
   [com.fulcrologic.rad.ids :refer [new-uuid]]
   [com.fulcrologic.rad.attributes :as attr]
   [com.fulcrologic.rad.database-adapters.key-value.write :as kv-write]
   [com.fulcrologic.rad.database-adapters.key-value :as key-value]
   [com.fulcrologic.rad.database-adapters.key-value.key-store :as kv-key-store]))

(defn new-account
  "Account creation helper."
  [id name email password & {:as extras}]
  (let [salt (attr/gen-salt)]
    (merge
      {:account/id            id
       :account/email         email
       :account/name          name
       :password/hashed-value (attr/encrypt password salt 100)
       :password/salt         salt
       :password/iterations   100
       :account/role          :account.role/user
       :account/active?       true}
      extras)))

(defn first-accounts
  "First system accounts."
  []
  (let [devadmin (new-account (new-uuid 100) "Dev Admin" "devadmin@etaira.com" "stand gift land cheer prodigy")
        devuser (new-account (new-uuid 101) "Dev User" "devuser@etaira.com" "stand gift land cheer prodigy")]
    [devadmin devuser]))

(defn default-indicators []
  (let [indicators (json/parse-string (slurp (io/resource "talib/api.json")) true)
        indicator-groups (mapv :group indicators)
        groups-tx (for [group indicator-groups]
                    {:indicator-group/id (new-uuid 103)
                     :indicator-group/name group})
        indicators-tx (vec
                       (concat
                        groups-tx
                        (for [indicator indicators]
                          (merge {:indicator/id (new-uuid 102)
                                  :indicator/name (:name indicator)
                                  :indicator/description (:description indicator)
                                  :indicator/group [:indicator-group/id (:indicator-group/id (first (filter #(= (:indicator-group/name %) (:group indicator)) groups-tx)))]
                                  :indicator/inputs (mapv (fn [input]
                                                            {:indicator-input/id (new-uuid 106)
                                                             :indicator-input/name (:name input)
                                                             :indicator-input/type (:type input)})
                                                          (:inputs indicator))
                                  :indicator/outputs (mapv (fn [output]
                                                             {:indicator-output/id (new-uuid 107)
                                                              :indicator-output/name (:name output)
                                                              :indicator-output/type (:type output)})
                                                           (:outputs indicator))}
                                 (when (seq (:indicator/options indicator))
                                   {:indicator/options (mapv (fn [option]
                                                               (merge {:indicator-option/id (new-uuid 104)
                                                                       :indicator-option/name (:name option)
                                                                       :indicator-option/display-name (:displayName option)
                                                                       :indicator-option/hint (:hint option)
                                                                       :indicator-option/type (:type option)}
                                                                      (when (:range option)
                                                                        {:indicator-option/range {:indicator-option-range/id (new-uuid 105)
                                                                                                  :indicator-option-range/min (str (-> option :range :min))
                                                                                                  :indicator-option-range/max (str (-> option :range :max))}})
                                                                      (when (:defaultValue option)
                                                                        {:indicator-option/default-value (str (:defaultValue option))}))) 
                                                             (:options indicator))})))))]
    indicators-tx))

(defn query-applied-migrations-names
  "Queries migrations that were already run."
  [{::kv-key-store/keys [store]}]
  (set (mapv :migration/name (vals (<!! (k/get-in store [:migration/id]))))))

(defn new-migration
  "New migration entity creation."
  [name]
  {:migration/id   (new-uuid)
   :migration/name name})

(defn migrations
  "The list of all migrations."
  []
  [{:name     "initial"
    :entities (first-accounts)}
   {:name     "default indicators"
    :entities (default-indicators)}])

(>defn migrate!
       "Database migrations launcher."
       [{::kv-key-store/keys [instance-name] :as key-store}]
       [::key-value/key-store => any?]
       (println "Running migrations for " instance-name)
       (let [applied-migrations (query-applied-migrations-names key-store)
             new-migrations (filterv #(not (contains? applied-migrations (:name %))) (migrations))]
         (doseq [migration new-migrations]
           (doseq [entity (:entities migration)]
             (kv-write/write-tree key-store entity))
           (kv-write/write-tree key-store (new-migration (:name migration))))))


