(ns etaira.schema.indicator.output
  (:refer-clojure :exclude [name type])
  (:require
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]
   [com.fulcrologic.rad.form-options :as fo]))

(defattr id :indicator-output/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :indicator-output/name :string
  {ao/identities #{:indicator-output/id}
   ao/required?  true
   ao/schema     :production})

(def types
  {"Double[]" "Double[]"
   "Integer[]" "Integer[]"})

(defattr type :indicator-output/type :enum
  {ao/identities    #{:indicator-output/id}
   ao/required?     true
   ao/enumerated-values (keys types)
   ao/enumerated-labels types
   ao/schema        :production
   fo/default-value "Double[]"})

(def attributes [id name type])