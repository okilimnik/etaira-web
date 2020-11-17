(ns com.example.components.seeded-connection
  (:require
   [mount.core :refer [defstate]]
   [konserve-fire.core :refer [new-fire-store]]
   [clojure.core.async :refer [<!!]]
   [com.fulcrologic.rad.database-adapters.key-value :refer [make-key-store]]))

(defstate kv-connections
  "The connection to the database that has just been freshly populated"
  :start {:main (make-key-store (<!! (new-fire-store "FIREBASE" :root "/hetaira" :db "hetaira-dev"))
                                "hetaira-web" {})})
