(ns etaira.components.seeded-connection
  (:require
   [clojure.string :refer [trim-newline]]
   [mount.core :refer [defstate]]
   [konserve-firebase.core :refer [new-firebase-store]]
   [clojure.core.async :refer [<!!]]
   [etaira.components.config :refer [config]]
   [com.fulcrologic.rad.database-adapters.key-value :refer [make-key-store]])
  (:import (com.google.firebase FirebaseApp FirebaseOptions)
           (com.google.firebase.database FirebaseDatabase)
           (com.google.auth.oauth2 GoogleCredentials)
           (java.io ByteArrayInputStream)))

(defstate kv-connections
          "The connection to the database that has just been freshly populated"
          :start
          {:main
           (do (FirebaseApp/initializeApp (.build (doto (FirebaseOptions/builder)
                                                    (.setCredentials (GoogleCredentials/fromStream (ByteArrayInputStream. (.getBytes (trim-newline (:firebase/config config))))))
                                                    (.setDatabaseUrl (:firebase/database-url config)))))
               (make-key-store (<!! (new-firebase-store {:db   (FirebaseDatabase/getInstance)
                                                         :root "/hetaira-test"}))
                               "hetaira-web" {}))})
