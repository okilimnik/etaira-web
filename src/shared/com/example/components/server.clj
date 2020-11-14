(ns com.example.components.server
  (:gen-class)
  (:require
   [aleph.http :refer [start-server]]
   [com.example.components.seeded-connection :refer [kv-connections]]
   [mount.core :refer [defstate] :as mount]
   [taoensso.timbre :as log]
   [com.example.components.seed :as seed]
   [com.example.components.config :refer [config]]
   [com.example.components.ring-middleware :refer [middleware]]))

(defstate http-server
  :start
  (let [cfg     (get config :aleph.server/config)
        server (start-server middleware cfg)]
    (log/info "Starting webserver with config " cfg)
    {:stop #(.close server)})
  :stop
  (let [{:keys [stop]} http-server]
    (when stop
      (stop))))

(defn seed! []
  (let [connection (:main kv-connections)]
    (when connection
      (seed/seed! connection))))

(defn -main [& args]
  (mount/start-with-args {:config "config/dev.edn"})
  (seed!)
  :ok)
