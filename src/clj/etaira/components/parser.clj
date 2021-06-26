(ns etaira.components.parser
  (:require
   [etaira.components.auto-resolvers :refer [automatic-resolvers]]
   [etaira.components.seeded-connection :refer [kv-connections]]
   [etaira.components.config :as config]
   [etaira.components.delete-middleware :as delete]
   [etaira.components.save-middleware :as save]
   [etaira.schema :refer [all-attributes]]
   [etaira.schema.account :as account]
   [etaira.schema.timezone :as timezone]
   [etaira.schema.ai.model :as ai-model]
   [com.fulcrologic.rad.attributes :as attr]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.pathom :as pathom]
   [mount.core :refer [defstate]]
   [com.wsscode.pathom.core :as p]
   [com.fulcrologic.rad.type-support.date-time :as dt]
   [com.fulcrologic.rad.database-adapters.key-value.pathom :as kv-pathom]
   [taoensso.timbre :as log]))
 
(defstate parser
  :start
  (pathom/new-parser
   config/config
   [(attr/pathom-plugin all-attributes)
    (form/pathom-plugin save/middleware delete/middleware)
    (kv-pathom/pathom-plugin (fn [env]
                               {:production (:main kv-connections)}))
    {::p/wrap-parser
     (fn transform-parser-out-plugin-external [parser]
       (fn transform-parser-out-plugin-internal [env tx]
         (dt/with-timezone "America/Los_Angeles"
           (if (and (map? env) (seq tx))
             (parser env tx)
             {}))))}]
   [automatic-resolvers
    form/resolvers
    account/resolvers
    timezone/resolvers
    ai-model/resolvers]))
