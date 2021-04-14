(ns etaira.ui-ico-segments.use-cases
  (:require
   [com.fulcrologic.fulcro.dom :refer [div button a p img i svg span h3]]
   [com.fulcrologic.semantic-ui.elements.icon.ui-icon :refer [ui-icon]]))

(def use-cases-content
  [{:icon "green huge universal access"
    :head "Sed ut perspiciatis unde omnis iste natus error sit "
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}
   {:icon "green huge edit outline"
    :head "But I must explain to you how all this mistaken idea"
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}
   {:icon "green huge share square outline"
    :head "At vero eos et accusamus et iusto odio dignissimos ducimus "
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}
   {:icon "green huge balance scale"
    :head "On the other hand, we denounce with righteous indignation"
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}])

(defn use-cases []
  (div :.stackable.ui.grid
       {:style {:background-color "#00ffff"
               :width "100%"
               :min-height "800px"
               :text-align "center"
               :display "flex"
               :justify-content "space-between"
               :position "relative"
               :padding "100px 0"}}
       (div :.sixteen.wide.column
            (h3 {:style
                 {:color "#013220"
                  :font-size "250%"}}
                "Use Cases"))

       (for [item use-cases-content]
         (div :.six.wide.centered.column
              (div :.two.column.ui.grid
                   (div :.right.aligned.four.wide.column
                        (ui-icon {:name (:icon item)}))
                   (div :.left.aligned.twelve.wide.column
                        (p {:style {:font-size "170%"}}
                           (:head item)))
                   (div :.right.aligned.four.wide.column
                        (p ""))
                   (div :.left.aligned.twelve.wide.column
                        (p {:style {:font-size "130%"}}
                           (:text item))))))))
