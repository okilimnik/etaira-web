(ns etaira.model.neural-network-layer
  (:require
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]
   [com.fulcrologic.rad.form-options :as fo]))

(defattr id :neural-network-layer/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr number :neural-network-layer/number :int
  {ao/identities #{:neural-network-layer/id}
   ao/required?  true
   ao/schema     :production})

(defattr number-of-neurons :neural-network-layer/number-of-neurons :int
  {ao/identities    #{:neural-network-layer/id}
   ao/required?     true
   ao/schema        :production
   fo/default-value 1})

(def neural-network-layer-types
  ["dense"
   "rnn"
   "lstm"
   "gru"
   "conv1d"
   "conv2d"
   "conv3d"
   "convLstm2d"
   "dropout"])

(defattr type :neural-network-layer/type :enum
  {ao/identities    #{:neural-network-layer/id}
   ao/enumerated-values neural-network-layer-types
   ao/enumerated-labels neural-network-layer-types
   ao/required?     true
   ao/schema        :production
   fo/default-value "dense"})

(def attributes [id number number-of-neurons type])
