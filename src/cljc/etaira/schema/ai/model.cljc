(ns etaira.schema.ai.model
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

(defattr id :ai-model/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :ai-model/name :string
  {ao/identities #{:ai-model/id}
   ao/required?  true
   ao/schema     :production})

(defattr config :ai-model/config :ref
  {ao/target      :tf-config/id
   ao/identities #{:ai-model/id}
   ao/required?  true
   ao/schema     :production})

(defattr dataset :ai-model/dataset :ref
  {ao/target      :dataset/id
   ao/identities #{:ai-model/id}
   ao/required?  true
   ao/schema     :production})

(def states
  {:training    "Training"
   :trained     "Trained"
   :not-trained "Not trained"})

(defattr state :ai-model/state :enum
  {ao/identities        #{:ai-model/id}
   ao/enumerated-values (keys states)
   ao/enumerated-labels states
   ao/schema            :production
   fo/default-value     :not-trained})

(defattr all-ai-models :ai-model/all-ai-models :ref
  {ao/target     :ai-model/id
   ao/pc-output  [{:ai-model/all-ai-models [:ai-model/id
                                            :ai-model/name
                                            :ai-model/state]}]
   ao/pc-resolve (fn [{:keys [query-params] :as env} _]
                   #?(:clj
                      {:ai-model/all-ai-models (queries/get-all-ai-models env query-params)}))})

#?(:cljs (defmutation delete-ai-model
           "Mutation: Delete the ai-model with `:ai-model/id` from the list with `:list/id`"
           [{list-id   :list/id
             ai-model-id :ai-model/id}]
           (action [{:keys [state]}]
                   (swap! state merge/remove-ident* [:ai-model/id ai-model-id] [:list/id list-id :list/ai-models]))))

#?(:clj
   (defmutation set-state [env {:ai-model/keys [id] :as props}]
     {::pc/params #{:ai-model/id}
      ::pc/output [:ai-model/id]}
     (form/save-form* env {::form/id        id
                           ::form/master-pk :ai-model/id
                           ::form/delta     {[:ai-model/id id] {:ai-model/state {:before :not-trained :after (:ai-model/state props)}}}}))
   :cljs
   (defmutation set-state [{:ai-model/keys [id] :as props}]

     (action [{:keys [state]}]
             (swap! state assoc-in [:ai-model/id id :ai-model/state] (:ai-model/state props)))
     (remote [_] true)))

(def attributes [id name config dataset all-ai-models set-state])

#?(:clj
   (def resolvers [set-state]))