(ns etaira.ui.ico.use-cases
(:require
   [com.fulcrologic.fulcro.dom :refer [div p h3]]
   [com.fulcrologic.semantic-ui.elements.icon.ui-icon :refer [ui-icon]]))

(def use-cases-content
  [{:key "Sed ut perspiciatis unde omnis iste natus error sit "
    :icon "universal access"
    :head "Sed ut perspiciatis unde omnis iste natus error sit "
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}
   {:key "But I must explain to you how all this mistaken idea"
    :icon "edit outline"
    :head "But I must explain to you how all this mistaken idea"
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}
   {:key "At vero eos et accusamus et iusto odio dignissimos ducimus "
    :icon "share square outline"
    :head "At vero eos et accusamus et iusto odio dignissimos ducimus "
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}
   {:key "On the other hand, we denounce with righteous indignation"
    :icon "balance scale"
    :head "On the other hand, we denounce with righteous indignation"
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}])

;;green huge

(defn use-cases []
  (div :.stackable.ui.grid
       {:style {:backgroundColor "#00ffff"
               :width "100%"
               :minHeight "800px"
               :textAlign "center"
               :display "flex"
               :justifyContent "space-between"
               :position "relative"
               :padding "100px 0"}}
       (div :.sixteen.wide.column
            (h3 {:style
                 {:color "#013220"
                  :fontSize "250%"}}
                "Use Cases"))

       (for [item use-cases-content]
         (div :.six.wide.centered.column {:key (:key item)}
              (div :.two.column.ui.grid
                   (div :.right.aligned.four.wide.column
                        
                             (ui-icon {:name (get item :icon)
                                       :className "green huge"})
                        )
                   (div :.left.aligned.twelve.wide.column
                        (p {:style {:fontSize "170%"}}
                           (get item :head)))
                   (div :.right.aligned.four.wide.column
                        (p ""))
                   (div :.left.aligned.twelve.wide.column
                        (p {:style {:fontSize "130%"}}
                         (get item :text))))))))
