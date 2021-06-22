(ns etaira.ui-ico-segments.tokenomics
  (:require
   [com.fulcrologic.fulcro.dom :as dom :refer [div span br h3]]
   [com.fulcrologic.semantic-ui.modules.accordion.ui-accordion :refer [ui-accordion]]
   ["victory" :refer [VictoryPie]]
   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [oops.core :refer [oget]]))

(def vpie (interop/react-factory VictoryPie))

(def tokenomics-content
  [{:key "ecosystem"
    :label "ecosystem"
    :fill "blue"
    :title {:content (span {:style {:fontSize "170%"
                                    :color "blue"
                                    :lineHeight 2}} "Ecosystem" (br) "500,000RAM"
                           (br))

            :icon {:name "chart pie"
                   :className "blue large"}}
    :y 500
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "blue"
                      :fontSize "130%"}}}
   {:key "hetaira-team-advisors"
    :label "hetaira-team-advisors"
    :fill "brown"
    :title {:content (span {:style {:fontSize "170%"
                                    :color "brown"
                                    :lineHeight 2}} "Hetaira Team & Advisors" (br) "600,000RAM"
                           (br))
            :icon {:name "chart pie"
                   :className "brown large"}}

    :y 600
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "brown"
                      :fontSize "130%"}}}
   {:key "treasury"
    :label "treasury"
    :fill "green"
    :title {:content (span {:style {:fontSize "170%"
                                    :color "green"
                                    :lineHeight 2}} "Treasury" (br) "300,000RAM"
                           (br))
            :icon {:name "chart pie"
                   :className "green large"}}

    :y 300
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "green"
                      :fontSize "130%"}}}
   {:key "seed-investors"
    :label "seed-investors"
    :fill "olive"
    :title {:content (span {:style {:fontSize "150%"
                                    :color "olive"
                                    :lineHeight 2}} "Seed Investors" (br) "200,000RAM"
                           (br))
            :icon {:name "chart pie"
                   :className "olive large"}}

    :y 200
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "olive"
                      :fontSize "130%"}}}
   {:key "private-sale"
    :label "private-sale"
    :y 700
    :fill "violet"
    :title {:content (span {:style {:fontSize "150%"
                                    :color "violet"
                                    :lineHeight 2}} "Private Sale" (br) "700,000RAM"
                           (br))
            :icon {:name "chart pie"
                   :className "violet large"}}


    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "violet"
                      :fontSize "130%"}}}
   {:key "public-sale"
    :label "public-sale"
    :fill "purple"
    :title {:content (span {:style {:fontSize "150%"
                                    :color "purple"
                                    :lineHeight 2}} "Public Sale" (br) "800,000RAM"
                           (br))
            :icon {:name "chart pie"
                   :className "purple large"}}

    :y 800
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "purple"
                      :fontSize "130%"}}}])

(def color-scale
  (vec (for [item tokenomics-content]
         (:fill item))))
  
(defn chart []
  (div {:style {:width 700
                :align "center"}}
       (vpie {:data tokenomics-content
              :height 200
              :width 200
              :labels (fn [datum] (oget datum.datum :label))
              :style {:labels {:fontSize "6px"
                               :fill (fn [datum] (oget datum.datum :fill))}}
              :colorScale color-scale
              :innerRadius 3
              :radius (fn [datum] (/ (oget datum.datum :y) 20))
              :labelRadius 45
              :padAngle 1})))

(defn tokenomics []
  (div :.stackable.ui.grid
       {:style {:backgroundColor "#00ffff"
                :width "100%"
                :minHeight "800px"
                :padding "100px 0"}}
       (div :.one.column.centered.row
            (h3 {:style
                 {:color "#013220"
                  :fontSize "250%"}}
                "Tokenomics"))
       (div :.two.column.centered.row
            (div :.six.wide.column
                 (ui-accordion {;;:defaultActiveIndex 
                                :panels tokenomics-content
                                :exclusive false}))
            (div :.six.wide.left.aligned.column
                 {:style {:margin "-100px -50px 0 0"}}
                 (chart)))))












  