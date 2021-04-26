(ns etaira.ui-ico-segments.whitepaper
  (:require
   [com.fulcrologic.fulcro.dom :as dom :refer [div span br button h3 p a h2 img]]
   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [oops.core :refer [oget]]
   [com.fulcrologic.semantic-ui.elements.button.ui-button :refer [ui-button]]
   [com.fulcrologic.semantic-ui.elements.button.ui-button-content :refer [ui-button-content]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid :refer [ui-grid]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-column :refer [ui-grid-column]]
   [com.fulcrologic.semantic-ui.collections.grid.ui-grid-row :refer [ui-grid-row]]
   [com.fulcrologic.semantic-ui.elements.image.ui-image :refer [ui-image]]))

#_(defn white-paper []
  (div :.ui.grid
       {:style {:backgroundColor "#00ffff"
                :width "100%"
                     ;;:minHeight "800px"
                :textAlign "center"
                     ;;:display "flex"
                     ;;:justifyContent "space-between"
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
                       :fontSize "250%"}}
                     "HETAIRA"))
            (div :.four.column.centered.row

                 (div :.column
                      (ui-button {:animated "true"
                                  :circular "true"
                                  :fluid "true"}
                                 (ui-button-content {:content "visible"
                                                     :visible "true"})
                                 (ui-button-content {:content "hidden"
                                                     :hidden "true"})))

                 (div :.column
                      (ui-button {:animated "true"
                                  :circular "true"
                                  :fluid "true"}
                                 (ui-button-content {:content "visible"
                                                     :visible "true"})
                                 (ui-button-content {:content "hidden"
                                                     :hidden "true"})))))))

(defn white-paper-b []
  (div   {:style {:backgroundColor "#00ffff"
                  :width "100%"
                  :display "block"
                  :padding "100px 0"}}
         (ui-grid
          {:stackable true}
          (ui-grid-row
           {:columns 1
            :textAlign "center"}
           (ui-grid-column
            {:children
             (ui-image {:src "/css/themes/default/assets/images/hetaira2.jpg"
                        :centered true
                        :style {:width "400px"
                           ;;:minHeight "100%"
                                :opacity 0.4}})}))
          (ui-grid-row
           {:columns 1
            :textAlign "center"}
           (ui-grid-column
            {:children (h2 {:style
                            {:color "#013220"
                             :fontSize "250%"}}
                           "HETAIRA")}))
          (ui-grid-row
           {:columns 2
            :divided true
            :centered true
            :textAlign "center"}
           (ui-grid-column
            {:width 4
             :textAlign "center"
             :children (a {:href "https://google.com"}
                          (ui-button {:animated true
                                      :circular true
                                      :fluid true
                                      :color "green"
                                      :size "huge"}
                                     (ui-button-content {:content (span {:style {:color "#013220"}} "Whitepaper")
                                                         :visible true})
                                     (ui-button-content {:content (span {:style {:color "#013220"}} "Click please")
                                                         :hidden true})))})
           (ui-grid-column
            {:width 4
             :textAlign "center"
             :children (a {:href "https://google.com"}
                          (ui-button {:animated true
                                      :circular true
                                      :fluid true
                                      :color "green"
                                      :size "huge"}
                                     (ui-button-content {:content (span {:style {:color "#013220"}} "Documentation")
                                                         :visible true})
                                     (ui-button-content {:content (span {:style {:color "#013220"}} "Read more")
                                                         :hidden true})))}))
          (ui-grid-row
           {:columns 1
            :centered true
            :textAlign "center"}
           (ui-grid-column
            {:width 4
             :textAlign "center"
             :children (div
                        (a {:href "https://google.com"
                            :style {:padding "0 20px"}}
                           (ui-button {:circular true
                                       :icon "facebook"
                                       :color "facebook"
                                       :size "big"}))
                        (a {:href "https://google.com"
                            :style {:padding "0 20px"}}
                           (ui-button {:circular true
                                       :icon "twitter"
                                       :color "twitter"
                                       :size "big"}))
                        (a {:href "https://google.com"
                            :style {:padding "0 20px"}}
                           (ui-button {:circular true
                                       :icon "linkedin"
                                       :color "linkedin"
                                       :size "big"})))})))))
