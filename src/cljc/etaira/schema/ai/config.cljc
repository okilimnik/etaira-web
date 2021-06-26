(ns etaira.schema.ai.config
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
   [com.fulcrologic.rad.form-options :as fo]))

(defattr id :ai-config/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :ai-config/name :string
  {ao/identities #{:ai-config/id}
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

(defattr learning-rate :ai-config/learning-rate :enum
  {ao/identities        #{:ai-config/id}
   ao/enumerated-values (keys learning-rates)
   ao/enumerated-labels learning-rates
   ao/schema            :production
   fo/default-value     "0.003"})

(def activations
  {:relu "ReLU"
   :tanh "Tanh"
   :sigmoid "Sigmoid"
   :linear "Linear"})

(defattr activation :ai-config/activation :enum
  {ao/identities        #{:ai-config/id}
   ao/enumerated-values (keys activations)
   ao/enumerated-labels activations
   ao/schema            :production
   fo/default-value     :tanh})

(def regularizations
  {:none "None"
   :l1 "L1"
   :l2 "L2"})

(defattr regularization :ai-config/regularization :enum
  {ao/identities        #{:ai-config/id}
   ao/enumerated-values (keys regularizations)
   ao/enumerated-labels regularizations
   ao/schema            :production
   fo/default-value     :none})

(def problems
  {:classification "Classification"
   :regression "Regression"})

(defattr problem :ai-config/problem :enum
  {ao/identities        #{:ai-config/id}
   ao/enumerated-values (keys problems)
   ao/enumerated-labels problems
   ao/schema            :production
   fo/default-value     :classification})

(defattr layers :ai-config/layers :ref
  {ao/target      :ai-config/id
   ao/cardinality :many
   ao/identities  #{:ai-config/id}
   ao/schema      :production})

(defattr all-ai-configs :ai-config/all-ai-configs :ref
  {ao/target     :ai-config/id
   ao/pc-output  [{:ai-config/all-ai-configs [:ai-config/id
                                              :ai-config/name]}]
   ao/pc-resolve (fn [{:keys [query-params] :as env} e]
                   #?(:clj
                      {:ai-config/all-ai-configs (queries/get-all-ai-configs env query-params)}))})

#?(:cljs (defmutation delete-ai-config
           "Mutation: Delete the ai-config with `:ai-config/id` from the list with `:list/id`"
           [{list-id   :list/id
             ai-config-id :ai-config/id}]
           (action [{:keys [state]}]
                   (swap! state merge/remove-ident* [:ai-config/id ai-config-id] [:list/id list-id :list/ai-configs]))))

(def attributes [id name learning-rate activation regularization problem layers all-ai-configs])