(ns etaira.components.database-queries
  "`server-queries` would be a better name but constrained by no alterations to model allowed, so we can always easily
  copy over the latest RAD Demo"
  (:require
   [etaira.components.queries :as queries]
   [clojure.core.async :refer [<!!]]
   [com.fulcrologic.rad.database-adapters.key-value.pathom :as kv-pathom]))

(defn get-all-accounts
  [env query-params]
  (when-let [key-store (kv-pathom/env->key-store env)]
    (<!!
      (queries/get-all-accounts key-store query-params))))

(defn get-all-ai-configs
  [env query-params]
  (when-let [key-store (kv-pathom/env->key-store env)]
    (<!!
     (queries/get-all-ai-configs key-store query-params))))

(defn get-all-ai-models
  [env query-params]
  (when-let [key-store (kv-pathom/env->key-store env)]
    (<!!
     (queries/get-all-ai-models key-store query-params))))

(defn get-all-indicators
  [env query-params]
  (when-let [key-store (kv-pathom/env->key-store env)]
    (<!!
     (queries/get-all-indicators key-store query-params))))

(defn get-all-indicator-groups
  [env query-params]
  (when-let [key-store (kv-pathom/env->key-store env)]
    (<!!
     (queries/get-all-indicator-groups key-store query-params))))

(defn get-all-datasets
  [env query-params]
  (when-let [key-store (kv-pathom/env->key-store env)]
    (<!!
     (queries/get-all-datasets key-store query-params))))

(defn get-login-info
  "Get the account name, time zone, and password info via a username (email)."
  [env username]
  (when-let [key-store (kv-pathom/env->key-store env)]
    (<!!
     (queries/get-login-info key-store username))))