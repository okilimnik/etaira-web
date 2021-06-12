(ns etaira.model.indicator.input
  (:refer-clojure :exclude [name type])
  (:require
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]
   [com.fulcrologic.rad.form-options :as fo]))

(defattr id :indicator-input/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :indicator-input/name :string
  {ao/identities #{:indicator-input/id}
   ao/required?  true
   ao/schema     :production})

(def types
  {"Double[]" "Double[]"})

(defattr type :indicator-input/type :enum
  {ao/identities    #{:indicator-input/id}
   ao/required?     true
   ao/enumerated-values (keys types)
   ao/enumerated-labels types
   ao/schema        :production
   fo/default-value "Double[]"})

(def attributes [id name type])