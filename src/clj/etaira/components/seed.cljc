(ns etaira.components.seed
  (:require
   [com.fulcrologic.guardrails.core :refer [>defn =>]]
   [com.fulcrologic.rad.ids :refer [new-uuid]]
   [com.fulcrologic.rad.attributes :as attr]
   [com.fulcrologic.rad.database-adapters.key-value.write :as kv-write]
   [com.fulcrologic.rad.database-adapters.key-value :as key-value]
   [com.fulcrologic.rad.database-adapters.key-value.key-store :as kv-key-store]
   [com.fulcrologic.rad.type-support.date-time :as dt]))

(defn new-account
  "Seed helper. Uses name as db/id (tempid)."
  [id name email password & {:as extras}]
  (let [salt (attr/gen-salt)
        table :account/id
        value (merge
               {:account/id            id
                :account/email         email
                :account/name          name
                :password/hashed-value (attr/encrypt password salt 100)
                :password/salt         salt
                :password/iterations   100
                :account/role          :account.role/user
                :account/active?       true}
               extras)]
    [table id value]))

(defn all-tables!
  "All the tables that we are going to have entities of. This information is in the RAD registry, we just haven't gone
  that far yet"
  []
  [:account/id])

(defn all-entities!
  "There is no check done on the integrity of this data. But when putting it together the functions ident-of and value-of are
  supposed to help. Just make sure that for every ident-of there is at least one value-of of the same entity"
  []
  (let [devadmin (new-account (new-uuid 100) "Dev Admin" "devadmin@etaira.com" "stand gift land cheer prodigy"
                              :time-zone/zone-id :time-zone.zone-id/America-Los_Angeles)
        devuser (new-account (new-uuid 101) "Dev User" "devuser@etaira.com" "stand gift land cheer prodigy")]
    [devadmin devuser]))

(>defn seed!
       "Get rid of all data in the database then build it again from the data structure at all-entities"
       [{::kv-key-store/keys [instance-name] :as key-store}]
       [::key-value/key-store => any?]
       (dt/set-timezone! "America/Los_Angeles")
       (println "SEEDING data (Starting fresh). For" instance-name)
       (let [tables (all-tables!)
             entities (all-entities!)]
         (kv-write/import key-store tables entities)))


