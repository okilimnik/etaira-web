(ns etaira.model.neural-network
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
  {"0.00001" "0.00001"
   "0.0001" "0.0001"
   "0.001" "0.001"
   "0.003" "0.003"
   "0.01" "0.01"
   "0.03" "0.03"
   "0.1" "0.1"
   "0.3" "0.3"
   "1" "1"
   "3" "3"
   "10" "10"})

(defattr learning-rate :neural-network/learning-rate :enum
  {ao/identities        #{:neural-network/id}
   ao/enumerated-values (keys learning-rates)
   ao/enumerated-labels learning-rates
   ao/schema            :production
   fo/default-value     "0.003"})

(def activations
  {:relu "ReLU"
   :tanh "Tanh"
   :sigmoid "Sigmoid"
   :linear "Linear"})

(defattr activation :neural-network/activation :enum
  {ao/identities        #{:neural-network/id}
   ao/enumerated-values (keys activations)
   ao/enumerated-labels activations
   ao/schema            :production
   fo/default-value     :tanh})

(def regularizations
  {:none "None"
   :l1 "L1"
   :l2 "L2"})

(defattr regularization :neural-network/regularization :enum
  {ao/identities        #{:neural-network/id}
   ao/enumerated-values (keys regularizations)
   ao/enumerated-labels regularizations
   ao/schema            :production
   fo/default-value     :none})

(def problems
  {:classification "Classification"
   :regression "Regression"})

(defattr problem :neural-network/problem :enum
  {ao/identities        #{:neural-network/id}
   ao/enumerated-values (keys problems)
   ao/enumerated-labels problems
   ao/schema            :production
   fo/default-value     :classification})

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

#?(:cljs (defmutation delete-neural-network
           "Mutation: Delete the neural-network with `:neural-network/id` from the list with `:list/id`"
           [{list-id   :list/id
             neural-network-id :neural-network/id}]
           (action [{:keys [state]}]
                   (swap! state merge/remove-ident* [:neural-network/id neural-network-id] [:list/id list-id :list/neural-networks]))))

#_(defmutation add-neural-network
  "Mutation: Add a neural-network with `:neural-network/id` to the list with `:list/id`"
  [{list-id   :list/id
    neural-network-id :neural-network/id}]
  (action [{:keys [state]}]
          (swap! state merge/remove-ident* [:neural-network/id neural-network-id] [:list/id list-id :list/neural-networks])))

(def attributes [id name learning-rate activation regularization problem layers all-neural-networks])