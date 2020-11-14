(ns development
  (:require
   [clojure.tools.namespace.repl :as tools-ns :refer [set-refresh-dirs]]
   [com.example.components.seeded-connection :refer [kv-connections]]
   [com.example.components.ring-middleware]
   [com.example.components.server]
   [com.example.components.seed :as seed]
   [com.fulcrologic.rad.ids :refer [new-uuid]]
   [mount.core :as mount]
   [taoensso.timbre :as log]
   [com.fulcrologic.rad.type-support.date-time :as dt]))

(set-refresh-dirs "src/main" "src/firebase" "src/dev" "src/shared")

(comment
  (let [db (d/db (:main kv-connections))]
    (d/pull db '[*] [:account/id (new-uuid 100)])))

(defn seed! []
  (dt/set-timezone! "America/Los_Angeles")
  (let [connection (:main kv-connections)]
    (when connection
      (seed/seed! connection))))

(defn start []
  (mount/start-with-args {:config "config/dev.edn"})
  (seed!)
  :ok)

(defn -main []
  (mount/start-with-args {:config "config/dev.edn"})
  (seed!)
  :ok)

(defn stop
  "Stop the server."
  []
  (mount/stop))

(def go start)

(defn restart
  "Stop, refresh, and restart the server."
  []
  (stop)
  (tools-ns/refresh :after 'development/start))

(def reset #'restart)

