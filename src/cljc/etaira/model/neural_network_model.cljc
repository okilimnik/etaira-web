(ns etaira.model.neural-network-model
  (:require
   #?@(:clj
       [[com.wsscode.pathom.connect :as pc :refer [defmutation]]
        [etaira.components.database-queries :as queries]]
       :cljs
       [[com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]])
   [com.fulcrologic.fulcro.algorithms.merge :as merge]
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]
   [com.fulcrologic.rad.form-options :as fo]))

(defattr id :neural-network/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :neural-network/name :string
  {ao/identities #{:neural-network/id}
   ao/required?  true
   ao/schema     :production})

(def learning-rates
  [{:value "0.00001" :label "0.00001"}
   {:value "0.0001" :label "0.0001"}
   {:value "0.001" :label "0.001"}
   {:value "0.003" :label "0.003"}
   {:value "0.01" :label "0.01"}
   {:value "0.03" :label "0.03"}
   {:value "0.1" :label "0.1"}
   {:value "0.3" :label "0.3"}
   {:value "1" :label "1"}
   {:value "3" :label "3"}
   {:value "10" :label "10"}])

(defattr learning-rate :neural-network/learning-rate :enum
  {ao/identities        #{:neural-network/id}
   ao/enumerated-values (mapv :value learning-rates)
   ao/enumerated-labels (mapv :label learning-rates)
   ao/schema            :production
   fo/default-value     "0.003"})

(def activations
  [{:value "relu" :label "ReLU"}
   {:value "tanh" :label "Tanh"}
   {:value "sigmoid" :label "Sigmoid"}
   {:value "linear" :label "Linear"}])

(defattr activation :neural-network/activation :enum
  {ao/identities        #{:neural-network/id}
   ao/enumerated-values (mapv :value activations)
   ao/enumerated-labels (mapv :label activations)
   ao/schema            :production
   fo/default-value     "tanh"})

(def regularizations
  [{:value "none" :label "None"}
   {:value "L1" :label "L1"}
   {:value "L2" :label "L2"}])

(defattr regularization :neural-network/regularization :enum
  {ao/identities        #{:neural-network/id}
   ao/enumerated-values (mapv :value regularizations)
   ao/enumerated-labels (mapv :label regularizations)
   ao/schema            :production
   fo/default-value     "none"})

(def problems
  [{:value "classification" :label "Classification"}
   {:value "regression" :label "Regression"}])

(defattr problem :neural-network/problem :enum
  {ao/identities        #{:neural-network/id}
   ao/enumerated-values (mapv :value problems)
   ao/enumerated-labels (mapv :label problems)
   ao/schema            :production
   fo/default-value     "classification"})

(defattr layers :neural-network/layers :ref
  {ao/target      :neural-network-layer/id
   ao/cardinality :many
   ao/identities  #{:neural-network/id}
   ao/schema      :production})

(defattr all-neural-networks :neural-network/all-neural-networks :ref
  {ao/target     :neural-network/id
   ao/pc-output  [{:neural-network/all-neural-networks [:neural-network/id]}]
   ao/pc-resolve (fn [{:keys [query-params] :as env} _]
                   #?(:clj
                      {:neural-network/all-neural-networks (queries/get-all-neural-networks env query-params)}))})

(defmutation delete-neural-network
  "Mutation: Delete the neural-network with `:neural-network/id` from the list with `:list/id`"
  [{list-id   :list/id
    neural-network-id :neural-network/id}]
  (action [{:keys [state]}]
          (swap! state merge/remove-ident* [:neural-network/id neural-network-id] [:list/id list-id :list/neural-networks])))

(defmutation add-neural-network
  "Mutation: Add a neural-network with `:neural-network/id` to the list with `:list/id`"
  [{list-id   :list/id
    neural-network-id :neural-network/id}]
  (action [{:keys [state]}]
          (swap! state merge/remove-ident* [:neural-network/id neural-network-id] [:list/id list-id :list/neural-networks])))

(def attributes [id name learning-rate activation regularization problem layers all-neural-networks])