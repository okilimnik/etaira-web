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
   ["victory" :refer [VictoryBar VictoryChart VictoryAxis VictoryTheme VictoryStack]]
   ))

;; (def data2013 [
;;   {:quarter "1" :earnings "15000"}
;;   {:quarter 2 :earnings 12500}
;;   {:quarter 3 :earnings 19500}
;;   {:quarter 4 :earnings 13000}
;; ])

;; (def victor (fn []
;;               (div (h1 "tytytytyt"))
;;               VictoryChart {:polar "true"
;;                             :domain {:x [0 360]}
;;                             :height "250"
;;                             :width "250"
;;                             :padding "30"}
;; VictoryBar {:style {:data {:fill "#c43a31"
;;                            :width "50"}}
;;             :data "sampleDataPolar"})
;;   )
  


;; <VictoryChart polar
;;   domain={{ x: [0, 360] }}
;;   height={250} width={250}
;;   padding={30}
;; >
;;   <VictoryBar
;;     style={{ data: { fill: "#c43a31", width: 50 }}}
;;     data={sampleDataPolar}
;;   />
;; </VictoryChart>

  ;; (fn [] (VictoryBar {:data data2013
  ;;             :x "quarter"
  ;;             :y "earnings"})))

  ;; VictoryChart {:domainPadding "10"
  ;;               :theme {:VictoryTheme "material"}}
  ;; VictoryAxis {:tickValues ["Quarter 1" "Quarter 2" "Quarter 3" "Quarter 4"]}
  ;; VictoryAxis {:dependentAxis "true"
  ;;              :tickFormat (fn [x] (/ x 1000))}
  ;; VictoryStack {:colorScale "warm"}
  ;; VictoryBar {:data data2013
  ;;             :x "quarter"
  ;;             :y "earnings"}
  ;; VictoryBar {:data "data2013"
  ;;             :x "quarter"
  ;;             :y "earnings"})


  


















(def tokenomics-content
  [{:key "ecosystem"
    ;;:icon "angle double right"
    ;;:active "false"
    :title {:content (span {:style {:font-size "150%"
                                    :color "blue"}} "Ecosystem" (br) "500,000RAM"
                           (br) (br))
            
            :icon "blue large chart pie"}
    :rate "500,000"
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "blue"}}}
   {:key "ramifi-team-advisors"
    :title {:content (span {:style {:font-size "150%"
                                    :color "brown"}} "Hetaira Team & Advisors" (br) "500,000RAM"
                           (br) (br))
            :icon "brown large chart pie"}
    
    :rate "600,000"
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "brown"}}}
   {:key "treasury"
    :title {:content (span {:style {:font-size "150%"
                                    :color "green"}} "Treasury" (br) "500,000RAM"
                           (br) (br))
            :icon "green large chart pie"}
    
    :rate "500,000"
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "green"}}}
   {:key "seed-investors"
    :title {:content (span {:style {:font-size "150%"
                                    :color "olive"}} "Seed Investors" (br) "500,000RAM"
                           (br) (br))
            :icon "olive large chart pie"}
    
    :rate "500,000"
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "olive"}}}
   {:key "private-sale"
    :title {:content (span {:style {:font-size "150%"
                                    :color "violet"}} "Private Sale" (br) "500,000RAM"
                           (br) (br))
            :icon "violet large chart pie"}
    
    :rate "500,000"
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "violet"}}}
   {:key "public-sale"
    :title {:content (span {:style {:font-size "150%"
                                    :color "purple"}} "Public Sale" (br) "500,000RAM"
                           (br) (br))
            :icon "purple large chart pie"}
    
    :rate "500,000"
    :content {:content "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
              :style {:color "purple"}} 
    }])



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
                 (div :.five.wide.column
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
                 (div :.five.wide.column
                      (p "adlaegfyugafa jveu jedguy"))))))












   
;;    (div :.stackable.ui.grid
;;        {:style {:background-color "#00ffff"
;;                 :width "100%"
;;                 :min-height "800px"
;;                 :padding "100px 0"}}
;;        (div :.one.column.centered.row
;;             (h3 {:style
;;                  {:color "#013220"
;;                   :font-size "250%"}}
;;                 "Tokenomics"))
;;       ;; (div :.ui.grid
;;             (div :.two.column.row
;;                  (div :.eight.wide.centered.left.aligned.column
;;                     ;;   (for [item tokenomics-content]
;;                         (div :.ui.styled.accordion {:style {:background-color "#00ffff"
;;                                                             :border-color "#00ffff"}}
;;                              (div :.title
;;                                   (ui-icon {:name "angle double right"})
;;                                   "jkgjhlfjh"
;;                               ;;     (get item :name)
;;                               ;;     (br)
;;                               ;;     (get item :rate)
;;                                   )
;;                              (div :.content
;;                                   (p
;;                                    ;;   (get item :text)
;;                                      "yytu hghi Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
;;                                      ))

;;                              (div :.title
;;                                   (ui-icon {:name "angle double right"})
;;                                   "hvhkcfc"
;;                               ;;     (get item :name)
;;                               ;;     (br)
;;                               ;;     (get item :rate)
;;                                                                     )
;;                              (div :.content
;;                                   (p
;;                                      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
;;                                    ;;   (get item :text)
;;                                      ))
                             
;;                              ))
                      
;;                     ;;   )
;;                  (ui-accordion {
;;                                 }
;;                       (ui-accordion-title {:icon "angle double right"
;;                                            :children "ft vuyf"
;;                                            :active "true"
;;                                            :onClick (fn [] (str "jf" " jguftyf"))}
;;                            (ui-icon {:name "angle double right"})
;;                            "svbjhbv sdjbhj sjbfj"
;;                            (br)
;;                            "(get item :rate)")
;;                       (ui-accordion-content
;;                            "jf tyft tydut"))
;;                  )
;;            ))
;;        ;;)