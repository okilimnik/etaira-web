(ns etaira.ui-ico-segments.buy-section
  (:require
   [com.fulcrologic.fulcro.dom :as dom :refer [div span br button h4 p a h2 img i label input select option]]
   [com.fulcrologic.semantic-ui.elements.input.ui-input :refer [ui-input]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown :refer [ui-dropdown]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-item :refer [ui-dropdown-item]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-search-input :refer [ui-dropdown-search-input]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-menu :refer [ui-dropdown-menu]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-divider :refer [ui-dropdown-divider]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.mutations :refer [defmutation]]
   [com.fulcrologic.rad.application :as rad-app]
   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [oops.core :refer [oget]]
   [com.fulcrologic.semantic-ui.elements.button.ui-button :refer [ui-button]]
   [com.fulcrologic.semantic-ui.elements.button.ui-button-content :refer [ui-button-content]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid :refer [ui-grid]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-column :refer [ui-grid-column]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-row :refer [ui-grid-row]]
   [com.fulcrologic.semantic-ui.elements.image.ui-image :refer [ui-image]]))

(defmutation bump-number [ignored]
  (action [{:keys [state]}]
          (swap! state update :rom/numb inc)))

(defsc Rom [this {:rom/keys [numb] :as props} {:keys [visible?]}]
  {:query         [:rom/numb]
   :initial-state {:rom/numb 0}
;;    even we put initial state :rom/numb 2, it is starting with 0. why?????????
   :ident               (fn [] [:component/id ::Rom])}
  (dom/div
   (dom/h4 "This is an example.")
   (dom/button {:onClick #(comp/transact! this [(bump-number {})])}
               "You've clicked this button " numb " times.")))

 (def ui-rom (comp/factory Rom))

(defsc SomeApp [this props]
  
  (div
   (ui-rom {:rom/numb 1})))

(def ui-some-app (comp/factory SomeApp))



(defonce pair (atom {:ara 1000
                     :aeth 1}))





(defsc Buy [this {:buy/keys [ra eth]}]
  (div :.ui.compact.menu
       (div :.ui.simple.dropdown.item
            "To buy tokens now"
            (i :.dropdown.icon)
            (div :.menu
                 (div :.ui.action.labeled.input
                      (label)
                      (input
                       {:type "number"
                        :placeholder eth
                        })
                      (select :.ui.compact.dropdown
                              (option {:value "wei"} "wei")
                              (option {:value "gwei"} "gwei")
                              (option {:value "ether"
                                       :selected true} "ether"))
                      (div :.ui.button "Confirm"))
                 (div :.ui.dropdown.horizontal.divider "or")
                 (div :.ui.action.input
                      (input
                       {:type "number"
                        :placeholder ra
                        })


                                        ;; (input
                                        ;;  {:type "number"
                                        ;;   :value 44
                                        ;;     :on-change #(reset! ara (-> % .-target .-value)
                                        ;;                       ;;  {:ra (-> % .-target .-value)
                                        ;;                       ;;       :eth (:ra @pair)}
                                        ;;                         )
                                        ;;   })
                                        ;;  (input
                                        ;;   {:type "number"
                                        ;;    :placeholder "amount RA to buy"})
                      (select :.ui.compact.dropdown
                                                ;;  (option {:value "weiRA"} "weiRA")
                                                ;;  (option {:value "gweiRA"} "gweiRA")
                              (option {:value "RA"} "RA"))
                      (div :.ui.button "Confirm"))))))

(def ui-buy (comp/factory Buy))

(defsc NBuySection [this props]
  (div
   (ui-buy {:buy/ra 2000 :buy/eth 3})))

(def ui-n-buy-section (comp/factory NBuySection))


(defn ra-input [val]
  (ui-input
   {:type "number"
    :transparent true
    :placeholder (:ara @val)
    :value (:ara @val)
    :onChange #(reset! val {:ara (-> % .-target .-value)
                            :aeth (/ (-> % .-target .-value) 1000)})}))

(defn eth-input [value]
  (ui-input
   {:type "number"
    :placeholder (:aeth @value)
    :value (:aeth @value)
    :onChange #(reset! value {:aeth (-> % .-target .-value)
                              :ara (* (-> % .-target .-value) 1000)})}))

(defsc buy-section [this props]
  ;; (let [pair (atom {:ara 1000
  ;;                   :aeth 1})]
  (div {:style {:backgroundColor "#00ffff"
                :width "100%"
                :display "block"
                :padding "50px 0"}}

       (ui-grid
        {:stackable true}
        (ui-grid-row
         {:columns 4
          :divided true
          :centered true
          :textAlign "center"}
         (ui-grid-column
          {:width 4
           :textAlign "center"
           :children (div :.ui.compact.menu
                          (div :.ui.simple.dropdown.item
                               "To buy tokens now"
                               (i :.dropdown.icon)
                               (div :.menu
                                    (div :.ui.action.labeled.input
                                         (div :.ui.label
                                              {:textAlign "center"}
                                              "ETH for send")
                                         (eth-input pair)
                                         (select :.ui.compact.dropdown
                                                 (option {:value "wei"} "wei")
                                                 (option {:value "gwei"} "gwei")
                                                 (option {:value "ether"
                                                          :selected true} "ether"))
                                         (div :.ui.button "Confirm"))
                                    (div :.ui.dropdown.horizontal.divider "or")
                                    (div :.ui.action.labeled.input
                                         (div :.ui.label
                                              "RA for buy")
                                         (ra-input pair)


                                        ;; (input
                                        ;;  {:type "number"
                                        ;;   :value 44
                                        ;;     :on-change #(reset! ara (-> % .-target .-value)
                                        ;;                       ;;  {:ra (-> % .-target .-value)
                                        ;;                       ;;       :eth (:ra @pair)}
                                        ;;                         )
                                        ;;   })
                                        ;;  (input
                                        ;;   {:type "number"
                                        ;;    :placeholder "RA to buy"})
                                         (select :.ui.compact.dropdown
                                                ;;  (option {:value "weiRA"} "weiRA")
                                                ;;  (option {:value "gweiRA"} "gweiRA")
                                                 (option {:value "RA"} "RA"))
                                         (div :.ui.button "Confirm"))
                                    (div :.ui.dropdown.horizontal.divider "(1ETH = 1000RA)"))))})
         (ui-grid-column
          {:width 4
           :textAlign "center"
           :children (ui-n-buy-section)})
         (ui-grid-column
          {:children (ui-rom)})
         (ui-grid-column
          {:width 4
           :textAlign "center"
           :children (div
                      (p "ra " (:ara @pair))
                      (p "eth " (:aeth @pair)))})))))
                                  ;;  )

(def ui-buy-section (comp/factory buy-section))