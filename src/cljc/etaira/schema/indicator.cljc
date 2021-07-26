(ns etaira.schema.indicator
  (:refer-clojure :exclude [name type])
  (:require
   #?@(:clj
       [[com.wsscode.pathom.connect :as pc :refer [defmutation]]
        [etaira.components.database-queries :as queries]]
       :cljs
       [[com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]])
   [com.fulcrologic.fulcro.algorithms.merge :as merge]
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]
   [com.wsscode.pathom.connect :as pc]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]))

(defattr id :indicator/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :indicator/name :string
  {ao/identities #{:indicator/id}
   ao/required?  true
   ao/read-only? true
   ao/schema     :production})

(defattr description :indicator/description :string
  {ao/identities #{:indicator/id}
   ao/read-only? true
   ao/schema     :production})

(defattr group :indicator/group :ref
  {ao/target      :indicator-group/id
   ao/identities #{:indicator/id}
   ao/read-only? true
   ao/schema     :production})

(defattr options :indicator/options :ref
  {ao/target      :indicator-option/id
   ao/cardinality :many
   ao/identities #{:indicator/id}
   ao/schema     :production})

(defattr inputs :indicator/inputs :ref
  {ao/target      :indicator-input/id
   ao/cardinality :many
   ao/identities #{:indicator/id}
   ao/schema     :production})

(defattr outputs :indicator/outputs :ref
  {ao/target      :indicator-output/id
   ao/cardinality :many
   ao/identities #{:indicator/id}
   ao/schema     :production})

(defattr all-indicators :indicator/all-indicators :ref
  {ao/target     :indicator/id
   ao/pc-output  [{:indicator/all-indicators [:indicator/id
                                              :indicator/name
                                              {:indicator/options [:indicator-option/id
                                                                   :indicator-option/name
                                                                   :indicator-option/display-name
                                                                   :indicator-option/hint
                                                                   :indicator-option/default-value]
                                               :indicator/inputs [:indicator-input/id
                                                                  :indicator-input/name]
                                               :indicator/outputs [:indicator-output/id
                                                                   :indicator-output/name]}]}]
   ao/pc-resolve (fn [{:keys [query-params] :as env} _]
                   #?(:clj
                      {:indicator/all-indicators (queries/get-all-indicators env query-params)}))})

(def attributes [id name description group options inputs outputs all-indicators])