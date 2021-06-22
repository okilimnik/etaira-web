(ns etaira.ui.advisor
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]
   [etaira.model.advisor :as advisor]))

(defsc Advisor [this {:advisor/keys [name] :as props} {:keys [onDelete]}]
  {:query         [:advisor/id :advisor/name]
   :ident         (fn [] [:advisor/id (:advisor/id props)])
   :initial-state {:advisor/name "dsfsf" :advisor/id 1}}
  (dom/div {:className "a" :id "id" :style {:color "red"}}
           (dom/p (str "name: " name))
           (dom/button {:onClick #(onDelete name)} "X")))

(def ui-advisor (comp/factory Advisor {:keyfn :advisor/id}))

(defsc AdvisorList [this {:list/keys [id label advisors] :as props}]
  {:query [:list/id :list/label {:list/advisors (comp/get-query Advisor)}] ; (5)
   :ident (fn [] [:list/id (:list/id props)])
   :initial-state
   {:list/id     1
    :list/label  "label"
    :list/advisors [{:id 1 :name "Sally"}]}}
  (let [delete-advisor (fn [item-id] (comp/transact! this [(advisor/delete-advisor {:list id :item item-id})]))] ; (4)
    (dom/div
     (dom/h4 label)
     (dom/ul
      (map #(ui-advisor (comp/computed % {:onDelete delete-advisor})) advisors)))))

(def ui-advisor-list (comp/factory AdvisorList))