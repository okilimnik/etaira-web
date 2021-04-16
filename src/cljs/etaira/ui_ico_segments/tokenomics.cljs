(ns etaira.ui-ico-segments.tokenomics
  (:require
   [com.fulcrologic.fulcro.dom :as dom :refer [div button a p img i svg span br h1 h3]]
   [com.fulcrologic.rad.rendering.semantic-ui.semantic-ui-controls :as sui]
   [com.fulcrologic.semantic-ui.elements.icon.ui-icon :refer [ui-icon]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.mutations :refer [defmutation]]
   [com.fulcrologic.semantic-ui.modules.accordion.ui-accordion :refer [ui-accordion]]
   [com.fulcrologic.semantic-ui.modules.accordion.ui-accordion-title :refer [ui-accordion-title]]
   [com.fulcrologic.semantic-ui.modules.accordion.ui-accordion-content :refer [ui-accordion-content]]
   ["victory" :refer [VictoryBar VictoryChart VictoryAxis VictoryGroup VictoryPie VictoryTheme VictoryStack VictoryLine sampleDataPolar VictoryPolarAxis]]
   [cljs.pprint :refer [cl-format]]
   ["react" :refer [React View]]
   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [taoensso.timbre :as log]
   [clojure.string :refer [join]]))







(defn us-dollars [n]
  (str "$" (cl-format nil "~:d" n)))

(def vchart (interop/react-factory VictoryChart))
(def vaxis (interop/react-factory VictoryAxis))
(def vline (interop/react-factory VictoryLine))
(def vbar (interop/react-factory VictoryBar))
(def vtheme (interop/react-factory VictoryTheme))
(def vpolar-axis (interop/react-factory VictoryPolarAxis))
(def vstack (interop/react-factory VictoryStack))
(def vpie (interop/react-factory VictoryPie))

;; " [ {:year 1991 :value 2345 } ...] "
(defsc YearlyValueChart [this {:keys [label plot-data x-step]}]
  (let [start-year (apply min (map :year plot-data))
        end-year   (apply max (map :year plot-data))
        years      (range start-year (inc end-year) x-step)
        dates      (mapv #(new js/Date % 1 2) years)
        {:keys [min-value
                max-value]} (reduce (fn [{:keys [min-value max-value] :as acc}
                                         {:keys [value] :as n}]
                                      (assoc acc
                                             :min-value (min min-value value)
                                             :max-value (max max-value value)))
                                    {}
                                    plot-data)
        min-value  (int (* 0.8 min-value))
        max-value  (int (* 1.2 max-value))
        points     (mapv (fn [{:keys [year value]}]
                           {:x (new js/Date year 1 2)
                            :y value})
                         plot-data)]
    (vchart nil
            (vaxis {:label      label
                    :standalone false
                    :scale      "time"
                    :tickFormat (fn [d] (.getFullYear d))
                    :tickValues dates})
            (vaxis {:dependentAxis true
                    :standalone    false
                    :tickFormat    (fn [y] (us-dollars y))
                    :domain        #js [min-value max-value]})
            (vline {:data points}))))

(def yearly-value-chart (comp/factory YearlyValueChart))

(defsc Root [this props]
  {:initial-state {:label     "Yearly Value"
                   :x-step    2
                   :plot-data [{:year 1983 :value 20}
                               ]}}
  (dom/div
   (yearly-value-chart props)))

(def ui-chart (comp/factory Root))

(def data2014 [
  {:quarter 1 :earnings 11500}
  {:quarter 2 :earnings 13250}
  {:quarter 3 :earnings 20000}
  {:quarter 4 :earnings 15500}
])


(def data001 [{:x 0 :y 500} {:x 60 :y 650} {:x 120 :y 800} {:x 180 :y 250} {:x 240 :y 400} {:x 300 :y 570}])

(defn ttchart []
  (vchart {:polar "true"}
          (vstack {:colorScale ["#ad1b11" "#c43a31" "#dc7a6b"]
                   :style {:width 50}}
                  (vbar {:polar "true"
                         :domain {:x [0 360] :y [0 1000]}
                         :height 150
                         :width 150
         ;;:labels (fn [] (str (p "yy")))
                         :data data001
                         :style {:data {:fill "#c89a31"
                                        :stroke "none"
                                ;;:strokeWidth 1
                                        }}}))))
(def data002 [{:y 20 :label "dog" :colorScale ["blue"]} {:x "cats" :y 30} {:x "cats" :y 50}])




(defn tchart []
  (vchart {:polar "true"
           :domain {:x [0 360] :y [0 1000]}
           :height 150
           :width 150
           ;;:theme "material"
           }
          (vpolar-axis {:tickCount 0})

          #_(vbar {:data data001
                 :style {:data {:fill "#c43a31"
                         ;;:width 40
                                :stroke "black"
                                :strokeWidth 1}}})

          (vbar {:data [{:x 0 :y 100} {:x 180 :y 50}]
                 :labels "ttt"
                 :style {:data {:fill "#c89a31"
                                :stroke "none"
                                ;;:stroke "black"
                                ;;:strokeWidth 1
                                }}})
          
          (vbar {:data data001
                 :style {:data {:fill "#c89a31"
                                :stroke "none"
                                ;;:strokeWidth 1
                                }}})))
  

;;style={{ data: { fill: "#c43a31", stroke: "black", strokeWidth: 2 }}}
















(def tokenomics-content
  [{:key "ecosystem"
    ;;:icon "angle double right"
    ;;:active "false"
    :label "ecosystem"
    :color "blue"
    :title {:content (span {:style {:font-size "170%"
                                    :color "blue"
                                    :line-height "2"}} "Ecosystem" (br) "500,000RAM"
                           (br))
            
            :icon "blue large chart pie"}
    :y 500
    :rad 50
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "blue"
                      :font-size "130%"}}}
   {:key "hetaira-team-advisors"
    :label "hetaira-team-advisors"
    :color "brown"
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
    :color "green"
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
    :color "olive"
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
    :color "violet"
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
    :color "purple"
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
          :labels (fn [datum] (:label datum))
          :style {:labels {:fontSize "6px"
                           :fill (fn [datum] "#013220")}
                  ;;:width "100%"
                  ;;:marging "100px 0 0 0"
                  }
          :colorScale color-scale
          :cornerRadius (fn [datum] 20)
          :innerRadius 30
          :radius (fn [datum] 37)
          :labelRadius 40
          :labelPlacement "parallel"
          :padAngle 5}

        
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
                    ;;   (for [item tokenomics-content]
                        ;; (div :.ui.styled.accordion {:style {:background-color "#00ffff"
                        ;;                                     :border-color "#00ffff"}}
                                
                  (ui-accordion {:defaultActiveIndex "0"
                                 :panels tokenomics-content
                                 :exclusive "false"
                                 ;;:styled "true"
                                ;;  :style {:background-color "#00ffff"
                                ;;          :border "none"
                                ;;          :fluid "false"
                                ;;          :display "block"}
                                 }))
                 (div :.six.wide.left.aligned.column
                      {:style {
                               :margin "-100px -50px 0 0"
                              
                               }}
                      (chart)
                      )))))












  