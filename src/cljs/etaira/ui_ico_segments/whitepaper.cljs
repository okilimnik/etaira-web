(ns etaira.ui-ico-segments.whitepaper
  (:require
   [com.fulcrologic.fulcro.dom :as dom :refer [div span br button h3 h2 img]]
   [com.fulcrologic.semantic-ui.modules.accordion.ui-accordion :refer [ui-accordion]]
   ["victory" :refer [VictoryPie]]
   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [oops.core :refer [oget]]
   ["react" :refer [React]]
   ["semantic-ui-react" :refer [Grid Image]]
   [com.fulcrologic.semantic-ui.elements.button.ui-button :refer [ui-button]]
   [com.fulcrologic.semantic-ui.elements.button.ui-button-content :refer [ui-button-content]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid :refer [ui-grid]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-column :refer [ui-grid-column]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-row :refer [ui-grid-row]]))

(defn white-paper []
  (div :.ui.grid
       {:style {:background-color "#00ffff"
                :width "100%"
                     ;;:min-height "800px"
                :text-align "center"
                     ;;:display "flex"
                     ;;:justify-content "space-between"
                     ;;:position "relative"
                :padding "100px 0"}}
       (div :.five.wide.centered.column


            (div :.one.column.centered.row
                 (img {:src "/css/themes/default/assets/images/hetaira2.jpg"

                       :style {:width "70%"
                          ;;:position "relative"
                               :opacity 0.4

                               :padding "50px 25px"}}))
            (div :.one.column.centered.row
                 (h2 {:style
                      {:color "#013220"
                       :font-size "250%"}}
                     "HETAIRA"))
            (div :.four.column.centered.row
                 
                 (div :.column 
                      (ui-button {:animated "true"
                             :circular "true"
                             :fluid "true"
                             }
                         (ui-button-content {:content "visible"
                                             :visible "true"})
                         (ui-button-content {:content "hidden"
                                             :hidden "true"})))
                 
                 (div :.column 
                      (ui-button {:animated "true"
                             :circular "true"
                             :fluid "true"
                                                              }
                            (ui-button-content {:content "visible"
                                                :visible "true"})
                            (ui-button-content {:content "hidden"
                                                :hidden "true"})))
                 
                 )
            ))
  )

#_(defn white-paper []
  (div   {:style {:background-color "#00ffff"
                  :width "100%"
                     ;;:min-height "800px"
                  :text-align "center"
                     ;;:display "flex"
                     ;;:justify-content "space-between"
                     ;;:position "relative"
                  :padding "100px 0"}}
         (ui-grid
          (ui-grid-row
           (img {:src "/css/themes/default/assets/images/hetaira2.jpg"

                 :style {:width "70%"
                          ;;:position "relative"
                         :opacity 0.4

                         :padding "50px 25px"}}))
          (ui-grid-row
           (h2 {:style
                {:color "#013220"
                 :font-size "250%"}}
               "HETAIRA"))
          (ui-grid-row
           (ui-grid-column
            (ui-button {:animated "true"
                        :circular "true"
                        :fluid "true"}
                       (ui-button-content {:content "visible"
                                           :visible "true"})
                       (ui-button-content {:content "hidden"
                                           :hidden "true"})))
           (ui-grid-column
            (ui-button {:animated "true"
                        :circular "true"
                        :fluid "true"}
                       (ui-button-content {:content "visible"
                                           :visible "true"})
                       (ui-button-content {:content "hidden"
                                           :hidden "true"})))))))
