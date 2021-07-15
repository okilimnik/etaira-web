(ns etaira.ui-ico-segments.buy-section
  (:require
   [com.fulcrologic.fulcro.dom :as dom :refer [div span br button h3 p a h2 img i input select option]]
   [com.fulcrologic.semantic-ui.elements.input.ui-input :refer [ui-input]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown :refer [ui-dropdown]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-item :refer [ui-dropdown-item]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-search-input :refer [ui-dropdown-search-input]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-menu :refer [ui-dropdown-menu]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-divider :refer [ui-dropdown-divider]]

   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [oops.core :refer [oget]]
   [com.fulcrologic.semantic-ui.elements.button.ui-button :refer [ui-button]]
   [com.fulcrologic.semantic-ui.elements.button.ui-button-content :refer [ui-button-content]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid :refer [ui-grid]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-column :refer [ui-grid-column]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-row :refer [ui-grid-row]]
   [com.fulcrologic.semantic-ui.elements.image.ui-image :refer [ui-image]]))



(defn buy-section []
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
                                    (div :.ui.action.input
                                         (input 
                                          {:type "number"
                                           :placeholder "amount ETH to send"})
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
                                           :placeholder "amount RA to buy"})
                                         (select :.ui.compact.dropdown
                                                ;;  (option {:value "weiRA"} "weiRA")
                                                ;;  (option {:value "gweiRA"} "gweiRA")
                                                 (option {:value "RA"} "RA"))
                                         (div :.ui.button "Confirm"))
                                    )))
           
           }
          
          )
         (ui-grid-column)
         (ui-grid-column)
         (ui-grid-column
          {:width 4
           :textAlign "center"
           :children (ui-dropdown
                      {:icon {:name "hand point left"
                              :className "green large"}
                       :text "buy tokens"}
                      (ui-dropdown-menu
                       {:direction "left"}
                       (ui-dropdown-search-input
                        {:type "number"
                         :className "big"
                         :placeholder "amount RA to buy"})

                       (ui-dropdown-divider
                        {:as "string"
                         :className "ui horizontal divider"}
                        (span
                         

                         "OR"))
                       (ui-dropdown-search-input
                        {:type "number"
                         :placeholder "amount ETH to send"})
                       (ui-dropdown-item
                        {:text "confirm"
                         :style {:textAlign "center"
                                 :color "#013220"
                                 :backgroundColor "green"

                                 :fontSize "150%"}})))})))
                                 
                                 
                                 
                                 ))