(ns com.example.ui.cryptopairs-forms
  (:require
   [clojure.string :as str]
   [com.example.model :as model]
   [com.example.model.account :as account]
   [com.example.model.cryptopair :as cryptopair]
   [com.example.model.timezone :as timezone]
   [com.example.ui.address-forms :refer [AddressForm]]
   [com.example.ui.file-forms :refer [FileForm]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.mutations :refer [defmutation]]
   [com.fulcrologic.fulcro.algorithms.form-state :as fs]
   #?(:clj  [com.fulcrologic.fulcro.dom-server :as dom :refer [div label input]]
      :cljs [com.fulcrologic.fulcro.dom :as dom :refer [div label input]])
   [com.fulcrologic.rad.control :as control]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.form-options :as fo]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.report-options :as ro]))

(form/defsc-form CryptopairForm [this props]
  {fo/id                  cryptopair/id
   ;   ::form/read-only?          true
   fo/attributes          [cryptopair/name
                           cryptopair/fetch-url
                           cryptopair/active?]

   fo/default-values      {:cryptopair/active?    false}
   
   fo/route-prefix        "cryptopair"
   fo/title               "Edit Cryptopair"})

(defsc CptopairListItem [this
                        {:cryptopair/keys [id name fetch-url active?] :as props}
                        {:keys [report-instance row-class ::report/idx]}]
  {:query [:cryptopair/id :cryptopair/name :cryptopair/fetch-url :cryptopair/active?]
   :ident :cryptopair/id}
  (let [{:keys [edit-form entity-id]} (report/form-link report-instance props :cryptopair/name)]
    (dom/div :.item
             (dom/i :.large.github.middle.aligned.icon)
             (div :.content
                  (if edit-form
                    (dom/a :.header {:onClick (fn [] (form/edit! this edit-form entity-id))} name)
                    (dom/div :.header name))
                  (dom/div :.description
                           (str (if active? "Active" "Inactive"))))))
  #_(dom/tr
     (dom/td :.right.aligned name)
     (dom/td (str active?))))

(report/defsc-report CryptopairList [this props]
  {ro/title               "All Cryptopairs"
   ;::report/layout-style             :list
   ;::report/row-style                :list
   ;::report/BodyItem                 CryptopairListItem
   ro/form-links          {cryptopair/name CryptopairForm}
   ro/field-formatters    {:cryptopair/name (fn [this v] v)}
   ro/column-headings     {:cryptopair/name "Cryptopair Name"}
   ro/columns             [cryptopair/name cryptopair/active?]
   ro/row-pk              cryptopair/id
   ro/source-attribute    :cryptopair/all-cryptopairs
   ro/run-on-mount?       true

   ro/initial-sort-params {:sort-by          :cryptopair/name
                           :ascending?       false
                           :sortable-columns #{:cryptopair/name}}

   ro/controls            {::new-cryptopair   {:type   :button
                                            :local? true
                                            :label  "New Cryptopair"
                                            :action (fn [this _] (form/create! this CryptopairForm))}
                           :show-inactive? {:type          :boolean
                                            :local?        true
                                            :style         :toggle
                                            :default-value false
                                            :onChange      (fn [this _] (control/run! this))
                                            :label         "Show Inactive Cryptopairs?"}}

   ro/control-layout      {:action-buttons [::new-cryptopair]
                           :inputs         [[:show-inactive?]]}

   ro/row-actions         [{:label     "Enable" 
                            :action    (fn [report-instance {:cryptopair/keys [id]}]
                                         #?(:cljs
                                            (comp/transact! report-instance [(cryptopair/set-cryptopair-active {:cryptopair/id      id
                                                                                                          :cryptopair/active? true})])))
                            ;:visible?  (fn [_ row-props] (not (:cryptopair/active? row-props)))
                            :disabled? (fn [_ row-props] (:cryptopair/active? row-props))}
                           {:label     "Disable"
                            :action    (fn [report-instance {:cryptopair/keys [id]}]
                                         #?(:cljs
                                            (comp/transact! report-instance [(cryptopair/set-cryptopair-active {:cryptopair/id      id
                                                                                                          :cryptopair/active? false})])))
                            ;:visible?  (fn [_ row-props] (:cryptopair/active? row-props))
                            :disabled? (fn [_ row-props] (not (:cryptopair/active? row-props)))}]

   ro/route               "cryptopairs"})

(comment

  (comp/get-query CryptopairList-Row))
