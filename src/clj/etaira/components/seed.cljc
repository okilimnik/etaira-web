(ns etaira.components.seed
  (:require
   [com.fulcrologic.guardrails.core :refer [>defn =>]]
   [com.fulcrologic.rad.ids :refer [new-uuid]]
   [com.fulcrologic.rad.attributes :as attr]
   [konserve.core :as k]
   [clojure.core.async :refer [<!!]]
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

(defn query-applied-migrations-names
  "Queries migrations that were already run."
  [store]
  (set (mapv :migration/name (vals (<!! (k/get-in store [:migration/id]))))))

(defn new-migration
  "New migration entity creation."
  [name]
  {:migration/id (new-uuid)
   :migration/name name})

(defn migrations
  "The list of all migrations."
  []
  [{:name "initial"
    :entities (first-accounts)}])

(>defn migrate!
       "Database migrations launcher."
       [{::kv-key-store/keys [instance-name] :as key-store}]
       [::key-value/key-store => any?]
       (println "Running migrations for " instance-name)
       (let [applied-migrations (query-applied-migrations-names key-store)
             new-migrations (filterv #(not (contains? applied-migrations (:name %))) migrations)]
         (doseq [migration new-migrations]
           (doseq [entity (:entities migration)]
             (kv-write/write-tree key-store entity))
           (kv-write/write-tree key-store (new-migration (:name migration))))))


