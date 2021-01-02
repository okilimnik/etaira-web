(ns etaira.components.seeded-connection
  (:require
   [mount.core :refer [defstate]]
   [konserve-firebase.core :refer [new-firebase-store]]
   [clojure.core.async :refer [<!!]]
   [com.fulcrologic.rad.database-adapters.key-value :refer [make-key-store]])
  (:import (com.google.firebase FirebaseApp)
           (com.google.firebase.database FirebaseDatabase)))

(defstate kv-connections
          "The connection to the database that has just been freshly populated"
          :start
          {:main
           (do (FirebaseApp/initializeApp)
               (make-key-store (<!! (new-firebase-store {:db   (FirebaseDatabase/getInstance)
                                                         :root "/hetaira-test"}))
                               "hetaira-web" {}))})
