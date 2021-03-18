(ns etaira.ui.neural-network
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]
   [etaira.model.neural-network-model :as neural-network-model]))

(defsc NeuralNetworkForm [this {:neural-network/keys [name] :as props} {:keys [onDelete]}]
  {:query         [:neural-network/id :neural-network/name]
   :ident         (fn [] [:neural-network/id (:neural-network/id props)])
   :initial-state {:neural-network/name "dsfsf" :neural-network/id 1}}
  (dom/div {:className "a" :id "id" :style {:color "red"}}
           (dom/p (str "name: " name))
           (dom/button {:onClick #(onDelete name)} "X")))

(def ui-neural-network (comp/factory NeuralNetworkForm {:keyfn :neural-network/id}))

(defsc NeuralNetworkList [this {:list/keys [id label neural-networks] :as props}]
  {:query [:list/id :list/label {:list/neural-networks (comp/get-query NeuralNetworkForm)}] ; (5)
   :ident (fn [] [:list/id (:list/id props)])
   :initial-state
   {:list/id     1
    :list/label  "label"
    :list/neural-networks [{:id 1 :name "Sally"}]}}
  (let [delete-neural-network (fn [item-id] (comp/transact! this [(neural-network-model/delete-neural-network {:list id :item item-id})]))] ; (4)
    (dom/div
     (dom/h4 label)
     (dom/ul
      (map #(ui-neural-network (comp/computed % {:onDelete delete-neural-network})) neural-networks)))))

(def ui-neural-network-list (comp/factory NeuralNetworkList))
