(ns etaira.ui-ico-segments.tokenomics
  (:require
   [com.fulcrologic.fulcro.dom :as dom :refer [div span br h3]]
   [com.fulcrologic.rad.rendering.semantic-ui.semantic-ui-controls :as sui]
   [com.fulcrologic.semantic-ui.modules.accordion.ui-accordion :refer [ui-accordion]]
   ["victory" :refer [VictoryBar VictoryChart VictoryAxis VictoryGroup VictoryPie VictoryTheme VictoryStack VictoryLine sampleDataPolar VictoryPolarAxis]]
   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [taoensso.timbre :as log]
   [oops.core :refer [oget ocall]]))

(def vpie (interop/react-factory VictoryPie))

(def tokenomics-content
  [{:key "ecosystem"
    :label "ecosystem"
    :fill "blue"
    :title {:content (span {:style {:font-size "170%"
                                    :color "blue"
                                    :line-height "2"}} "Ecosystem" (br) "500,000RAM"
                           (br))

            :icon "blue large chart pie"}
    :y 500
    :x 35
    :rad 50
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "blue"
                      :font-size "130%"}}}
   {:key "hetaira-team-advisors"
    :label "hetaira-team-advisors"
    :fill "brown"
    :title {:content (span {:style {:font-size "170%"
                                    :color "brown"
                                    :line-height "2"}} "Hetaira Team & Advisors" (br) "600,000RAM"
                           (br))
            :icon "brown large chart pie"}
    
    :y 600
    :rad 60
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "brown"
                      :font-size "130%"}}}
   {:key "treasury"
    :label "treasury"
    :fill "green"
    :title {:content (span {:style {:font-size "170%"
                                    :color "green"
                                    :line-height "2"}} "Treasury" (br) "300,000RAM"
                           (br))
            :icon "green large chart pie"}
    
    :y 300
    :rad 30
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "green"
                      :font-size "130%"}}}
   {:key "seed-investors"
    :label "seed-investors"
    :fill "olive"
    :title {:content (span {:style {:font-size "150%"
                                    :color "olive"
                                    :line-height "2"}} "Seed Investors" (br) "200,000RAM"
                           (br))
            :icon "olive large chart pie"}
    
    :y 200
    :rad 20
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "olive"
                      :font-size "130%"}}}
   {:key "private-sale"
    :label "private-sale"
    :y 700
    :rad 70
    :fill "violet"
    :title {:content (span {:style {:font-size "150%"
                                    :color "violet"
                                    :line-height "2"}} "Private Sale" (br) "700,000RAM"
                           (br))
            :icon "violet large chart pie"}
    
    
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "violet"
                      :font-size "130%"}}}
   {:key "public-sale"
    :label "public-sale"
    :fill "purple"
    :title {:content (span {:style {:font-size "150%"
                                    :color "purple"
                                    :line-height "2"}} "Public Sale" (br) "800,000RAM"
                           (br))
            :icon "purple large chart pie"}
    
    :y 800
    :rad 80
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "purple"
                      :font-size "130%"}} 
    }])

(def color-scale
  ["blue" "brown" "green" "olive" "violet" "purple"]
;;   [(str (for [item tokenomics-content]
;;           (get item :color)))]
  )

(defn chart []
  
  (div {:style {:width 700
                ;;:marging "200 200 0 0"
                :align "center"}}
   (vpie {:data tokenomics-content
          :height 200
          :width 200
          ;;:max-width "100%"
          ;;:min-width "100%"
          :labels (fn [datum] (oget datum :label))
          :style {:labels {:fontSize "6px"
                           :fill (fn [datum] "#013220")}
                  ;;:width "100%"
                  ;;:marging "100px 0 0 0"
                  }
          :colorScale color-scale
          :cornerRadius (fn [datum] 7)
          :innerRadius 3
          :radius (fn [datum] 35)
          :labelRadius 40
          :labelPlacement "parallel"
          :padAngle 1}

        
        )))

(def tokenomics (fn []
                  (div :.stackable.ui.grid
       {:style {:background-color "#00ffff"
                :width "100%"
                :min-height "800px"
                :padding "100px 0"}}
       (div :.one.column.centered.row
            (h3 {:style
                 {:color "#013220"
                  :font-size "250%"}}
                "Tokenomics"))
      ;; (div :.ui.grid
            (div :.two.column.centered.row
                 (div :.six.wide.column
                      (ui-accordion {:defaultActiveIndex "0"
                                     :panels tokenomics-content
                                     :exclusive "false"}))
                 (div :.six.wide.left.aligned.column
                      {:style {:margin "-100px -50px 0 0"}}
                      (chart))))))












  