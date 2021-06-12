(ns etaira.model.indicator.option
  (:refer-clojure :exclude [name type range])
  (:require
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]))

(defattr id :indicator-option/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :indicator-option/name :string
  {ao/identities #{:indicator-option/id}
   ao/required?  true
   ao/schema     :production})

(defattr display-name :indicator-option/display-name :string
  {ao/identities #{:indicator-option/id}
   ao/schema     :production})

(defattr hint :indicator-option/hint :string
  {ao/identities #{:indicator-option/id}
   ao/schema     :production})

(def types
  {"Integer" "Integer"
   "float" "float"})

(defattr type :indicator-option/type :enum
  {ao/identities #{:indicator-option/id}
   ao/required?     true
   ao/enumerated-values (keys types)
   ao/enumerated-labels types
   ao/schema     :production})

;; parsing function depends on :indicator-option/type
(defattr default-value :indicator-option/default-value :string
  {ao/identities #{:indicator-option/id}
   ao/schema     :production})

(defattr range :indicator-option/range :ref
  {ao/target      :indicator-option-range/id
   ao/identities #{:indicator-option/id}
   ao/schema     :production})

(def attributes [id name display-name hint type default-value range])