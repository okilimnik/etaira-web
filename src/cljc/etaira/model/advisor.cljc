(ns etaira.model.advisor
  (:require
   [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
   [com.fulcrologic.fulcro.algorithms.merge :as merge]))

(defmutation delete-advisor
  "Mutation: Delete the advisor with `:advisor/id` from the list with `:list/id`"
  [{list-id   :list/id
    advisor-id :advisor/id}]
  (action [{:keys [state]}]
          (swap! state merge/remove-ident* [:advisor/id advisor-id] [:list/id list-id :list/advisors])))

(defmutation add-advisor
  "Mutation: Add an advisor with `:advisor/id` to the list with `:list/id`"
  [{list-id   :list/id
    advisor-id :advisor/id}]
  (action [{:keys [state]}]
          (swap! state merge/remove-ident* [:advisor/id advisor-id] [:list/id list-id :list/advisors])))