(ns etaira.model.neural-network-model
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

(defattr id :neural-network-model/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :neural-network-model/name :string
  {ao/identities #{:neural-network-model/id}
   ao/required?  true
   ao/schema     :production})

(defattr config :neural-network-model/config :ref
  {ao/target      :neural-network-config/id
   ao/identities #{:neural-network-model/id}
   ao/required?  true
   ao/schema     :production})

(defattr dataset :neural-network-model/dataset :ref
  {ao/target      :dataset/id
   ao/identities #{:neural-network-model/id}
   ao/required?  true
   ao/schema     :production})

(def states
  {:training    "Training"
   :trained     "Trained"
   :not-trained "Not trained"})

(defattr state :neural-network-model/state :enum
  {ao/identities        #{:neural-network-model/id}
   ao/enumerated-values (keys states)
   ao/enumerated-labels states
   ao/schema            :production
   fo/default-value     :not-trained})

(defattr all-neural-network-models :neural-network-model/all-neural-network-models :ref
  {ao/target     :neural-network-model/id
   ao/pc-output  [{:neural-network-model/all-neural-network-models [:neural-network-model/id]}]
   ao/pc-resolve (fn [{:keys [query-params] :as env} _]
                   #?(:clj
                      {:neural-network-model/all-neural-network-models (queries/get-all-neural-network-models env query-params)}))})

#?(:cljs (defmutation delete-neural-network-model
           "Mutation: Delete the neural-network-model with `:neural-network-model/id` from the list with `:list/id`"
           [{list-id   :list/id
             neural-network-model-id :neural-network-model/id}]
           (action [{:keys [state]}]
                   (swap! state merge/remove-ident* [:neural-network-model/id neural-network-model-id] [:list/id list-id :list/neural-network-models]))))

#?(:clj
   (defmutation set-state [env {:neural-network-model/keys [id] :as props}]
     {::pc/params #{:neural-network-model/id}
      ::pc/output [:neural-network-model/id]}
     (form/save-form* env {::form/id        id
                           ::form/master-pk :neural-network-model/id
                           ::form/delta     {[:neural-network-model/id id] {:neural-network-model/state {:before :not-trained :after (:neural-network-model/state props)}}}}))
   :cljs
   (defmutation set-state [{:neural-network-model/keys [id] :as props}]

     (action [{:keys [state]}]
             (swap! state assoc-in [:neural-network-model/id id :neural-network-model/state] (:neural-network-model/state props)))
     (remote [_] true)))


(def attributes [id name config dataset all-neural-network-models set-state])

#?(:clj
   (def resolvers [set-state]))