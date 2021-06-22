(ns etaira.ui-ico-segments.smart-page
  (:require
   [com.fulcrologic.fulcro.dom :as dom :refer [div p img h3]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid :refer [ui-grid]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-column :refer [ui-grid-column]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-row :refer [ui-grid-row]]
   [com.fulcrologic.semantic-ui.elements.image.ui-image :refer [ui-image]]))

(defn smart-page []
  (div :.stackable.three.columns.ui.grid
       {:style {:backgroundColor "#00ffff"
                :padding "100px 0"
                :display "flex"
                :position "relative"
                :justifyContent "space-between"
                :width "100%"}}
       (div :.stretched.row
            (div :.eight.wide.column
                 (div :.segment
                      (img
                       {:src "/css/themes/default/assets/images/smart.png"
                        :style {:width "100%"
                                :position "relative"


                                :padding "50px 25px"}})))
            (div :.four.wide.column {:style {:padding "0 25px"
                                             :position "relative"

                                             :width "100%"
                                             :display "flex"}}
                 (div :.segment
                      (h3 {:style
                           {:color "#013220"
                            :fontSize "250%"}}
                          "Supply Based Stability"))
                 (div :.segment
                      (p {:style {:color "#013220"
                                  :fontSize "130%"}}
                         "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi conse"))
                 (div :.segment
                      (p {:style {:color "#013220"
                                  :fontSize "130%"}}
                         "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. ")))
            (div :.four.wide.column
                 (div :.segment
                      (img
                       {:src "/css/themes/default/assets/images/nn.png"
                        :style {:width "100%"
                                :position "relative"
                                :opacity 0.3
                                :padding "50px 25px"}}))))))

#_(defn smart-page-b []
  (div {:style {:backgroundColor "#00ffff"
                :padding "100px 0"
                :display "block"
                ;;:position "relative"
                ;;:justifyContent "space-between"
                :width "100%"
                }}
       (ui-grid {:stackable true
                 :relaxed "very"}
        (ui-grid-row {:columns 3
                      :textAlign "center"}
                     (ui-grid-column {:children (ui-image {:src "/css/themes/default/assets/images/smart.png"
                                                           :centered true
                                                           :style {:width "2000px"}})
                                      :width 8})
                     (ui-grid-column {:children (div (h3 {:style
                                                          {:color "#013220"
                                                           :fontSize "250%"}}
                                                         "Supply Based Stability"))
                                      :width 4})
                     (ui-grid-column {:children (div (h3 {:style
                                                          {:color "#013220"
                                                           :fontSize "250%"}}
                                                         "Supply Based Stability"))
                                      :width 4})))))