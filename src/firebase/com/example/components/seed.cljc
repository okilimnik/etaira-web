(ns com.example.components.seed
  (:require
   [com.fulcrologic.guardrails.core :refer [>defn => ?]]
   [com.fulcrologic.rad.type-support.decimal :as math]
   [com.fulcrologic.rad.ids :refer [new-uuid]]
   [com.fulcrologic.rad.attributes :as attr]
   [com.fulcrologic.rad.database-adapters.key-value.write :as kv-write :refer [ident-of value-of]]
   [com.fulcrologic.rad.database-adapters.key-value :as key-value]
   [com.fulcrologic.rad.database-adapters.key-value.key-store :as kv-key-store]
   [com.fulcrologic.rad.type-support.date-time :as dt]))

(defn new-account
  "Seed helper. Uses name as db/id (tempid)."
  [id name email password & {:as extras}]
  (let [salt #?(:clj (attr/gen-salt) :cljs 0)
        table :account/id
        value (merge
                {:account/id            id
                 :account/email         email
                 :account/name          name
                 :password/hashed-value #?(:clj (attr/encrypt password salt 100) :cljs 0)
                 :password/salt         salt
                 :password/iterations   100
                 :account/role          :account.role/user
                 :account/active?       true}
                extras)]
    [table id value]))

(defn new-address
  "Seed helper. Uses street as db/id for tempid purposes."
  [id street & {:as extras}]
  (let [table :address/id
        value (merge
                {:address/id     id
                 :address/street street
                 :address/city   "Sacramento"
                 :address/state  :address.state/CA
                 :address/zip    "99999"}
                extras)]
    [table id value]))

(defn new-category
  "Seed helper. Uses label for tempid purposes."
  [id label & {:as extras}]
  (let [table :category/id
        value (merge
                {:category/id    id
                 :category/label label}
                extras)]
    [table id value]))

(defn new-item
  "Seed helper. Uses street at db/id for tempid purposes."
  [id name price & {:as extras}]
  (let [table :item/id
        value (merge
                {:item/id    id
                 :item/name  name
                 :item/price (math/numeric price)}
                extras)]
    [table id value]))

(defn new-line-item [item quantity price & {:as extras}]
  (let [id (get extras :line-item/id (new-uuid))
        table :line-item/id
        value (merge
                {:line-item/id           id
                 :line-item/item         item
                 :line-item/quantity     quantity
                 :line-item/quoted-price (math/numeric price)
                 :line-item/subtotal     (math/* quantity price)}
                extras)]
    [table id value]))

(defn new-invoice [date customer line-items & {:as extras}]
  (let [table :invoice/id
        id (new-uuid)
        value (merge
                {:invoice/id         id
                 :invoice/customer   customer
                 :invoice/line-items line-items
                 :invoice/total      (reduce
                                       (fn [total {:line-item/keys [subtotal]}]
                                         (math/+ total subtotal))
                                       (math/zero)
                                       line-items)
                 :invoice/date       date}
                extras)]
    [table id value]))

(defn all-tables!
  "All the tables that we are going to have entities of. This information is in the RAD registry, we just haven't gone
  that far yet"
  []
  [:account/id :item/id :invoice/id :line-item/id :category/id :address/id])

(defn all-entities!
  "There is no check done on the integrity of this data. But when putting it together the functions ident-of and value-of are
  supposed to help. Just make sure that for every ident-of there is at least one value-of of the same entity"
  []
  (let [devadmin (new-account (new-uuid 100) "Dev Admin" "devadmin@etaira.com" "stand gift land cheer prodigy"
                              :account/addresses [(ident-of (new-address (new-uuid 1) "111 Main St."))]
                              :account/primary-address (value-of (new-address (new-uuid 300) "222 Other"))
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


