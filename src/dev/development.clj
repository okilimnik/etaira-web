(ns development
  (:require
   [clojure.tools.namespace.repl :as tools-ns :refer [set-refresh-dirs]]
   [etaira.components.seeded-connection :refer [kv-connections]]
   [etaira.components.ring-middleware]
   [etaira.components.server]
   [etaira.components.seed :as seed]
   [konserve.protocols]
   [mount.core :as mount]
   [taoensso.timbre :as log]
   [com.fulcrologic.rad.type-support.date-time :as dt]))

(set-refresh-dirs "src/clj" "src/cljc" "src/dev")

(defn seed! []
  (dt/set-timezone! "America/Los_Angeles")
  (let [connection (:main kv-connections)]
    (when connection
      (seed/migrate! connection))))

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

