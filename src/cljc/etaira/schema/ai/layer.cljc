(ns etaira.schema.ai.layer
  (:refer-clojure :exclude [type])
  (:require
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]
   [com.fulcrologic.rad.form-options :as fo]))

(defattr id :ai-layer/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr number :ai-layer/number :int
  {ao/identities #{:ai-layer/id}
   ao/required?  true
   ao/schema     :production})

(defattr number-of-neurons :ai-layer/number-of-neurons :int
  {ao/identities    #{:ai-layer/id}
   ao/required?     true
   ao/schema        :production
   fo/default-value 1})

(def ai-layer-types
  {:dense "Dense"
   :rnn "RNN"
   :lstm "LSTM"
   :gru "GRU"
   :conv-1d "Conv1D"
   :conv-2d "Conv2D"
   :conv-3d "Conv3D"
   :conv-lstm-2d "ConvLSTM2D"
   :dropout "Dropout"})

(defattr type :ai-layer/type :enum
  {ao/identities    #{:ai-layer/id}
   ao/enumerated-values (keys ai-layer-types)
   ao/enumerated-labels ai-layer-types
   ao/required?     true
   ao/schema        :production
   fo/default-value :dense})

(def attributes [id number number-of-neurons type])
