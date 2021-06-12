(ns etaira.model.indicator.option-range
  (:refer-clojure :exclude [min max])
  (:require
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]))

(defattr id :indicator-option-range/id :uuid
  {ao/identity? true
   ao/schema    :production})

;; parsing function depends on :indicator-option/type
(defattr min :indicator-option-range/min :string
  {ao/identities #{:indicator-option-range/id}
   ao/required?  true
   ao/schema     :production})

;; parsing function depends on :indicator-option/type
(defattr max :indicator-option-range/max :string
  {ao/identities #{:indicator-option-range/id}
   ao/required?  true
   ao/schema     :production})

(def attributes [id min max])