(ns etaira.model.indicator.group
  (:refer-clojure :exclude [name])
  (:require
   #?@(:clj
       [[com.wsscode.pathom.connect :as pc :refer [defmutation]]
        [etaira.components.database-queries :as queries]]
       :cljs
       [[com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]])
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]))

(defattr id :indicator-group/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :indicator-group/name :string
  {ao/identities #{:indicator-group/id}
   ao/required?  true
   ao/schema     :production})

(defattr all-indicator-groups :indicator-group/all-indicator-groups :ref
  {ao/target     :indicator-group/id
   ao/pc-output  [{:indicator-group/all-indicator-groups [:indicator-group/id]}]
   ao/pc-resolve (fn [{:keys [query-params] :as env} _]
                   #?(:clj
                      {:indicator-group/all-indicator-groups (queries/get-all-indicator-groups env query-params)}))})

(def attributes [id name all-indicator-groups])