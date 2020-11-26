(ns com.example.model.cryptopair
  (:refer-clojure :exclude [name])
  (:require
   #?@(:clj
       [[com.wsscode.pathom.connect :as pc :refer [defmutation]]
        [com.example.model.authorization :as exauth]
        [com.example.components.database-queries :as queries]]
       :cljs
       [[com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]])
   [clojure.string :as str]
   [com.example.model.timezone :as timezone]
   [com.wsscode.pathom.connect :as pc]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]
   [com.fulcrologic.rad.authorization :as auth]
   [com.fulcrologic.rad.middleware.save-middleware :as save-middleware]
   [com.fulcrologic.rad.blob :as blob]
   [taoensso.timbre :as log]
   [com.fulcrologic.fulcro.ui-state-machines :as uism]
   [com.fulcrologic.rad.type-support.date-time :as datetime]))

(defattr id :cryptopair/id :uuid
  {ao/identity?                                     true
   ;; NOTE: These are spelled out so we don't have to have either on classpath, which allows
   ;; independent experimentation. In a normal project you'd use ns aliasing.
   ao/schema                                        :production})

(defattr active? :cryptopair/active? :boolean
  {ao/identities                                          #{:cryptopair/id}
   ao/schema                                              :production
   fo/default-value                                       false})

(defattr name :cryptopair/name :string
  {fo/field-label "Name"
   ;::report/field-formatter (fn [v] (str "ATTR" v))
   ao/identities  #{:cryptopair/id}
   ;ao/valid?      (fn [v] (str/starts-with? v "Bruce"))
   ;::attr/validation-message                                 (fn [v] "Your name's not Bruce then??? How 'bout we just call you Bruce?")
   ao/schema      :production

   ao/required?   true})

(defattr fetch-url :cryptopair/fetch-url :string
  {fo/field-label "Fetch Url"
   ;::report/field-formatter (fn [v] (str "ATTR" v))
   ao/identities  #{:cryptopair/id}
   ;ao/valid?      (fn [v] (str/starts-with? v "Bruce"))
   ;::attr/validation-message                                 (fn [v] "Your name's not Bruce then??? How 'bout we just call you Bruce?")
   ao/schema      :production

   ao/required?   true})

(defattr all-cryptopairs :cryptopair/all-cryptopairs :ref
  {ao/target     :cryptopair/id
   ao/pc-output  [{:cryptopair/all-cryptopairs [:cryptopair/id]}]
   ao/pc-resolve (fn [{:keys [query-params] :as env} _]
                   #?(:clj
                      {:cryptopair/all-cryptopairs (queries/get-all-cryptopairs env query-params)}))})

#?(:clj
   (defmethod save-middleware/rewrite-value :cryptopair/id
     [env [_ id] value]
     value))

#?(:clj
   (defmutation set-cryptopair-active [env {:cryptopair/keys [id active?]}]
     {::pc/params #{:cryptopair/id}
      ::pc/output [:cryptopair/id]}
     (form/save-form* env {::form/id        id
                           ::form/master-pk :cryptopair/id
                           ::form/delta     {[:cryptopair/id id] {:cryptopair/active? {:before (not active?) :after (boolean active?)}}}}))
   :cljs
   (defmutation set-cryptopair-active [{:cryptopair/keys [id active?]}]
     (action [{:keys [state]}]
             (swap! state assoc-in [:cryptopair/id id :cryptopair/active?] active?))
     (remote [_] true)))

(def attributes [id name fetch-url active? all-cryptopairs])

#?(:clj
   (def resolvers [set-cryptopair-active]))
