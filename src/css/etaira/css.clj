(ns etaira.css
  (:require
   [garden.stylesheet :refer [at-media]]
   [garden.selectors :as s]
   [garden.units :refer [px em]]))

(def ^:garden main
  (list

   ;;General Type

   [:body
    {:font-family "\"Helvetica\", \"Arial\", sans-serif" 
     :background-color "#f7f7f7"}]

   [:h1
    {:font-size (px 34)}]

   [:header
    [:h1
     {:line-height (em 1.45)
      :font-weight "300"
      :color "rgba(255, 255, 255, 0.7)"}]]

   [:h1
    [:b
     {:font-weight "400"
      :color "rgba(255, 255, 255, 1)"}]]

   [:h2
    {:margin "5px 0"
     :font-weight "300"
     :font-size (px 18)}]

   [:h3
    {:margin "10px 0"}]

   [:p
    [:a
     {:color "#0D658C"}]]

   ;;Layout

   [:body
    {:margin 0}]

   [:.l--body
    {:width (px 550)
     :margin-left "auto"
     :margin-right "auto"}]
   
   [:.l--page
    {:width "90%"
     :margin-left "auto"
     :margin-right "auto"}]
   
   #_(at-media
    {:min-width (px 1180)}
    [:.l--page
     {:width "90%"}])

   #_(at-media
    {:min-width (px 1400)}
    [:.l--page
     {:width "90%"}])

   ;;Buttons
   
   [:#main-part
    [:.mdl-button
     {:background-color "rgba(158,158,158,.1)"
      :width (px 28)
      :height (px 28)
      :min-height (px 28)}]]
  
   [:#main-part
    [:.mdl-button
     [:&:hover
      {:background-color "rgba(158,158,158,.3)"}]]]

   [:#main-part
    [:.mdl-button
     [(s/& (s/focus (s/not s/active)))
      {:background-color "rgba(158,158,158,.4)"}]]]

   [:#main-part
    [:.mdl-button
     [:&:active
      {:background-color "rgba(158,158,158,.5)"}]]]

   [:#main-part
    [:.mdl-button
     [:.material-icons
      {:font-size (px 20)
       :color "rgba(0, 0, 0, 0.7)"}]]]

   #_[:.button
    {:cursor "pointer"
     :display "-webkit-box -moz-box -ms-flexbox -webkit-flex flex"
     :align-items "center"
     :-webkit-justify-content "center"
     :justify-content "center"
     :width (px 24)
     :height (px 24)
     :font-size (px 18)
     :border-radius "50%"
     :margin "0 1px"
     :background-color "rgba(0,0,0,0.05)"
     :outline "none"
     :border "none"
     :padding 0
     :color "#666"
     :transition "background-color 0.3s, color 0.3s"}]

   [:.button
    [:&:hover
     {:background-color "rgba(0,0,0,0.1)"}]]

   [:.button
    [:&:active
     {:background-color "rgba(0,0,0,0.15)"
      :color "#333"}]]

   [:.button
    [:i
     {:font-size (px 16)}]]

   [:.hide-button
    {:cursor "pointer"
     :padding "6px 4px 8px 4px"
     :border-left "1 px  solid #2c2c2c"
     :border-bottom "1px solid #2c2c2c"
     :position "fixed"
     :right (px 0)
     :background "#1a1a1a"
     :color "#eee"
     :font "11 px  'Lucida Grande'  sans-serif"
     :display "table"}]

   ;;Header

   [:.github-link
    {:width (px 60)
     :height (px 60)
     :position "absolute"
     :display "block"
     :top 0
     :right 0
     :z-index 1000}]
   
   [:.github-link
    [:.bg
     {:fill "#fff"
      :fill-opacity "0.2"}]]
   
   [:.github-link
    [:&:hover
     [:.bg
      {:fill-opacity "0.3"}]]]
   
   [:.github-link
    [:.icon
     {:fill "#fff"
      :fill-opacity 0.6}]]

   [:.github-link
    [:&:hover
     [:.icon
      {:fill-opacity 0.7}]]]

   [:header
    {:border-bottom "solid 1px rgba(0,0,0,0.4)"
     :background-color "#183D4E"
     :color "white"
     :overflow "hidden"
     :box-shadow "0 2px 4px rgba(0,0,0,0.2)"
     :position "relative"}]
   
   [:header
    [:h1
     {:font-size (px 30)
      :text-align "center"
      :margin-top (px 30)
      :margin-bottom (px 30)
      :-webkit-font-smoothing "antialised"}]]
  
   [:header
    [:h1
     [:.optional
      {:display "none"}]]]

   (at-media
    {:min-width (px 1064)}
    [:header
     [:h1
      [:.optional
       {:display "inline"}]]])

   (at-media
    {:min-height (px 700)}
    [:header
     [:h1
      {:margin-top (px 40)
       :margin-bottom (px 40)}]])

   (at-media
    {:min-height (px 800)}
    [:header
     [:h1
      {:font-size (px 34)
       :margin-top (px 60)
       :margin-bottom (px 60)}]])
   
   ;;Top Controls

   [:#top-controls
    {:border-bottom "1px solid #ddd"
     :padding "18px 0"
     :box-shadow "0 1px 4px rgba(0,0,0,0.08)"
     :background "white"}]
   
   (at-media
    {:min-height (px 700)}
    [:#top-controls
     {:padding "24px 0"}])
   
   [:#top-controls
    [:.container
     {:display "flex" ;;-webkit-box -moz-box -ms-flexbox -webkit-flex
      :-webkit-justify-content "space-betweenspace-between"
      :justify-content "space-between"}]]
   
   [:#top-controls
    [:.timeline-controls
     {:display "-webkit-box -moz-box -ms-flexbox -webkit-flex flex"
      :align-items "center"
      :margin-right (px 20)
      :width (px 140)}]]
   
   [:#play-pause-button
    [:.material-icons
     {:color "white"
      :font-size (px 36)
      :transform "translate(0px, 0px)"}]] ;;;;transform: translate(-18px,-12px);
   
 [:#play-pause-button
   [:.material-icons
    ["&:nth-of-type(2)"
     {:display "none"}]]]
   
   [:#play-pause-button
    [:.playing
     [:.material-icons
      ["&:nth-of-type(1)"
       {:display "none"}]]]]
   
   [:#play-pause-button
    [:.playing
     [:.material-icons
      ["&:nth-of-type(2)"
       {:display "inherit"}]]]]

   [:#top-controls
    [:.control
     {:flex-grow 1
      :max-width (px 180)
      :min-width (px 110)
      :margin-left (px 30)
      :margin-top (px 6)}]]
   
   [:#top-controls
    [:.control
     [:.label :label
      {:color "#777"
       :font-size (px 13)
       :display "block"
       :margin-bottom (px 6)
       :font-weight "300"}]]]
   
   [:#top-controls
    [:.control
     [:.value
      {:font-size (px 24)
       :margin 0
       :font-weight "300"}]]]
   
   [:#top-controls
    [:.control
     [:.select
      {:position "relative"}]]]
   
   [:#top-controls
    [:.control
     [:select
      {:-webkit-appearance "none"
       :-moz-appearance "none"
       :appearance "none"
       :display "block"
       :background "none"
       :border "none"
       :border-radius 0
       :padding "6px 0"
       :width "100%"
       :font-size (px 14)
       :border-bottom "solid 1px #ccc"
       :color "#333"
       :outline "none"}]]]
   
   [:#top-controls
    [:.control
     [:select
      [:&:focus
       {:border-bottom-color "#183D4E"}]]]]
   
   [:#top-controls
    [:.control
     [:.select
      ["&::after"
       {:class "\"material-icons\"";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;????????????
        :content "\"arrow_drop_down\""
        :color "#999"
        :font-family "'Material Icons'"
        :font-weight "normal"
        :font-style "normal"
        :font-size (px 18)
        :line-height 1
        :letter-spacing "normal"
        :text-transform "none"
        :display "inline-block"
        :white-space "nowrap"
        :word-wrap "normal"
        :direction "ltr"
        :position "absolute"
        :right 0
        :top (px 6)
        :pointer-events "none"}]]]]
  
  ;;;;;;;;;;;;;;;;hover card;;;;;;;;
 
    [:#hovercard
     {:display "none"
      :position "absolute"
      :padding (px 5)
      :border "1px solid #aaa"
      :z-index "1000"
      :background "#fff"
      :cursor "default"
      :border-radius (px 5)
      :left (px 240)
      :width (px 150)
      :top (px -20)}]
    
    [:#hovercard
     [:input
      {:width (px 60)}]]
    
    ;;;;;;;main part
    
    [:#main-part
     {:display "flex" ;;-webkit-box -moz-box -ms-flexbox -webkit-flex
      :-webkit-justify-content "space-between"
      :justify-content "space-between"
      :margin-top (px 30)
      :margin-bottom (px 50)
      :padding-top (px 2)
      :position "relative"}]
    
    (at-media
     {:min-height (px 700)}
     [:#main-part
      {:margin-top (px 50)}])
    
    [:#main-part
     [:h4
      {:display "flex" ;;-webkit-box -moz-box -ms-flexbox -webkit-flex
       :align-items "center"
       :font-weight "400"
       :font-size (px 16)
       :text-transform "uppercase"
       :position "relative"
       :padding-bottom (px 8)
       :margin 0
       :line-height (em 1.4)}]]
    
    [:#main-part
     [:.column
      [:.label :label
       {:font-weight "300"
        :line-height (em 1.38)
        :margin 0
        :color "#777"
        :font-size (px 13)}]]]
    
    [:#main-part
     [:p
       {:font-weight "300"
        :line-height (em 1.38)
        :margin 0
        :color "#777"
        :font-size (px 13)}]]
    
    [:.more
     {:position "absolute"
      :left "50%"}]
    
    [:.more
     [:button
      {:position "absolute"
       :left (px -28)
       :top (px -28)
       :background "white"}]]
    
    [:.more
     [:button
      [:&:hover :&:active :&:focus (s/& (s/focus (s/not s/active)))
       {:background "white"}]]]
    
    [:svg
     [:text
      {:dominant-baseline "middle"}]]
    
    [:canvas
     {:display "block"}]
    
    [:.link
     {:fill "none"
      :stroke "#aaa"
      :stroke-width "1"}]
    
    [:g.column
      [:rect
       {:stroke "none"}]]
    
    [:#heatmap
     {:position "relative"
      :float "left"
      :margin-top (px 10)}]
    
    [:#heatmap
     [:.tick
      [:line
       {:stroke "#ddd"}]]]
    
    [:#heatmap
     [:.tick
      [:text
       {:fill "#bbb"
        :dominant-baseline "auto"}]]]
    
    [:#heatmap
     [:.tick
      ["&:nth-child(7)"
       [:text
        {:fill "#333"}]]]]
    
    [:#heatmap
     [:.tick
      ["&:nth-child(7)"
       [:line
        {:stroke "#999"}]]]]
    
    ;;;;;;;data column
    
    [:.vcenter
     {:display "-webkit-box -moz-box -ms-flexbox -webkit-flex flex"
      :align-items "center"}]
    
    [:.data.column
      {:width "10%"}]
    
    [:.data.column
      [:.dataset-list
       {:margin "20px 0 10px"
        :overflow "hidden"}]]
    
    [:.data.column
      [:.dataset
       {:position "relative"
        :float "left"
        :width (px 34)
        :height (px 34)
        :margin "0 14px 14px 0"}]]
    
    [:.data
     [:.column
      [:.dataset
       ["&:nth-of-type(2n)"
        {:margin-right 0}]]]]
    
    [:.data.column
      [:.data-thumbnail
       {:cursor "pointer"
        :width "100%"
        :height "100%"
        :opacity "0.2"
        :border "2px solid rgba(0,0,0,0.1)"
        :border-radius (px 3)}]]
    
    [:.data.column
      [:.data-thumbnail
       [:&:hover
        {:border "2px solid #999"}]]]
    
    [:.data.column
      [:.data-thumbnail.selected
        {:border "2px solid black"
         :opacity "1"
         :box-shadow "0 1px 5px rgba(0,0,0,0.2)"
         :background-color "white"}]]
    
    [:#main-part
     [:.data.column
       [:.dataset
        [:.label
         {:position "absolute"
          :left (px 48)
          :top "calc(50% - 9px)"
          :display "none"}]]]]
    
    [:#main-part
     [:.data.column
       [:p.slider
         {:margin "0 -25px 20px"}]]]
    
    [:#main-part
     [:.basic-button
      {:font-family "\"Roboto\", \"Helvetica\", \"Arial\", sans-serif"
       :margin-top (px 25)
       :height (px 34)
       :margin-right 0
       :width "100%"
       :display "block"
       :color "rgba(0,0,0,0.5)"
       :border "none"
       :background "rgba(158,158,158,.1)"
       :border-radius (px 3)
       :padding (px 5)
       :font-size (px 12)
       :text-transform "uppercase"
       :font-weight "500"
       :outline "none"
       :transition "background 0.3s linear"
       :cursor "pointer"}]]
    
    [:#main-part
     [:.basic-button
      [:&:hover
       {:background "rgba(158,158,158,.3)"
        :color "rgba(0,0,0,0.6)"}]]]
    
    [:#main-part
     [:.basic-button
      [:&:focus
       {:background "rgba(158,158,158,.4)"
        :color "rgba(0,0,0,0.7)"}]]]
    
    [:#main-part
     [:.basic-button
      [:&:active
       {:background "rgba(158,158,158,.5)"
        :color "rgba(0,0,0,0.8)"}]]]
    
    ;;;;;Features column;;;;;;;;;;;;;;;
    
    [:.features.column
      {:width "10%"
       :position "relative"}]
    
    [:.features.column
      [:.plus-minus-neurons
       {:position "absolute"
        :text-align "center"
        :line-height (px 28)
        :top (px -58)
        :width (px 65)
        :height (px 44)
        :font-size (px 12)
        :z-index "100"}]]
    
    [:.plus-minus-neurons
     [:.mdl-button
      [:&:first-of-type
       {:margin-right (px 5)}]]]
    
    [:.features.column
      [:.callout
       {:position "absolute"
        :width (px 95)
        :font-style "italic"}]]
    
    [:.features.column
      [:.callout
       [:svg
        {:position "absolute"
         :left (px -15)
         :width (px 30)
         :height (px 30)}]]]
    
    [:.features.column
      [:.callout
       [:svg
        [:path
         {:fill "none"
          :stroke "black"
          :stroke-opacity "0.4"}]]]]
    
    [:.features.column
      [:.callout
       [:svg
        [:defs
         [:path
          {:fill "black"
           :stroke "none"
           :fill-opacity "0.4"}]]]]]
    
    [:#main-part
     [:.features.column
       [:.callout
        [:.label
         {:position "absolute"
          :top (px 24)
          :left (px 3)
          :font-size (px 11)}]]]]
    
    ;;;;;;;;;;;;;;;;Network (inside features column)
    
    [:#network
     {:position "absolute"
      :top (px 110)
      :left 0
      :z-index "100"}]
    
    [:#network
     [:svg
      [:.main-label
       {:font-size (px 13)
        :fill "#333"
        :font-weight "300"}]]]
    
    [:.axis
     [:line
      {:fill "none"
       :stroke "#777"
       :shape-rendering "crispEdges"}]]
    
    [:.axis
     [:text
      {:fill "#777"
       :font-size (px 10)}]]
    
    [:.axis
     [:path
      {:display "none"}]]
    
    [:#network
     [:svg
      [:.active
       [:.main-label
        {:fill "#333"}]]]]
    
    [:#network
     [:svg
      [:#markerArrow
       {:fill "black"
        :stroke "black"
        :stroke-opacity "0.2"}]]]
    
    [:#network
     [:.node
      {:cursor "default"}]]
    
    [:#network
     [:.node
      [:rect
       {:fill "white"
        :stroke-width 0}]]]
    
    [:#network
     [:.node.inactive
       {:opacity "0.5"}]]
    
    [:#network
     [:.node.hovered
       {:opacity "1.0"}]]
    
    #_(at--webkit-keyframes  ;;;;;;;;;;;;;;@-webkit-keyframes flowing {
     [:flowing               ;;;;;;;;;;;;;from { stroke-dashoffset: 0; } to { stroke-dashoffset: -10; }}
      [:from
       {:stroke-dashoffset 0}]
      [:to
       {:stroke-dashoffset "-10"}]])
    
    [:#network
     [:.core
      [:.link
       {:stroke-dasharray "9 1"
        :stroke-dashoffset "1"}]]] ;;;;;;;;/*-webkit-animation: 0.5s linear 0s infinite flowing;*/
    
    [:#network
     [:.core
      [:.link-hover
       {:stroke-width "8"
        :stroke "black"
        :fill "none"
        :opacity 0}]]]
    
    [:#network
     [:.canvas
      [:canvas
       {:position "absolute"
        :top (px -2)
        :left (px -2)
        :border "2px solid black"
        :border-radius (px 3)
        :box-shadow "0 2px 5px rgba(0,0,0,0.2)"}]]]
    
    [:#network
     [:.canvas.inactive
       [:canvas
        {:box-shadow "inherit"}]]]
    
    [:#network
     [:.canvas.inactive
       [:canvas
        {:opacity "0.4"
         :border 0
         :top 0
         :left 0}]]]
    
    [:#network
     [:.canvas.hovered
       [:canvas
        {:opacity "1.0"
         :border "2px solid #666"
         :top (px -2)
         :left (px -2)}]]]
    
    ;;;;;;;;;;;;;;;;;;;;;;;Hidden layers column
    
    [:.hidden-layers.column
      {:width "40%"}]
    
    [:#main-part
     [:.hidden-layers
      [:h4
       {:-webkit-justify-content "center"
        :justify-content "center"
        :margin-top (px -5)}]]]
    
    [:.hidden-layers
     [:#layers-label
      {:width (px 125)
       :display "inline-block"}]]
    
    [:.hidden-layers
     [:#num-layers
      {:margin "0  10px"
       :width (px 10)
       :display "inline-block"}]]
    
    [:.hidden-layers
     [:h4
      [:.mdl-button
       {:margin-right (px 5)}]]]
    
    [:.bracket
     {:margin-top (px 5)
      :border "solid 1px rgba(0, 0, 0, 0.2)"
      :border-bottom 0
      :height (px 4)}]
    
    [:.bracket.reverse
      {:border-bottom "solid 1px rgba(0, 0, 0, 0.2)"
       :border-top 0
       :margin-top 0
       :margin-bottom (px 5)}]
    
    ;;;;;;;;;;;;;;;;Output column
    
    [:.output.column
      {:width (px 275)}]
    
    [:.metrics
     {:position "relative"
      :font-weight "300"
      :font-size (px 13)
      :height (px 60)}]
    
    [:#linechart
     {:position "absolute"
      :top 0
      :right 0
      :width "50%"
      :height (px 55)}]
    
    [:.metrics
     [:.train
      {:color "#777"}]]
    
    [:#loss-test
     {:color "black"}]
    
    [:.output
     [:.output-stats
      [:.value
       {:color "rgba(0, 0, 0, 0.6)"
        ;;;:font-size (px 20)  ;;;;;/*font-size: 20px;*/????????????????????????
        :font-weight "300"
        :display "inline"}]]]
    
    [:g.train
      [:circle
       {:stroke "white"
        :stroke-width "1"
        :stroke-opacity "0.8"
        :fill-opacity "0.9"}]]
    
    [:g.test
      [:circle
       {:stroke-width "1"
        :stroke "black"
        :stroke-opacity "0.6"
        :fill-opacity "0.9"}]]
    
    [:#main-part
     [:.output
      [:.mdl-checkbox__label.label
        {:line-height (em 1.7)}]]]
    
    ;;;;;;;;;;;;;Material Overrides
    
    ;;;;;;;;;;;;;;buttons
    
    [:.mdl-button--fab.mdl-button--colored
     {:background "#183D4E"}]
    
    [:.mdl-button--fab.mdl-button--colored
      [:&:hover :&:active :&:focus (s/& (s/focus (s/not s/active)))
       {:background "#183D4E"}]]
    
    ;;;;;;;;;;;;;;;;checkbox
    
    [:.mdl-checkbox__box-outline
     {:border-color "rgba(0, 0, 0, 0.5)"}]
    
    [:.mdl-checkbox.is-checked
     [:.mdl-checkbox__tick-outline
      {:background-color "#183D4E"}]]
    
    [:.mdl-checkbox.is-checked
     [:.mdl-checkbox__box-outline
      {:border-color "#183D4E"}]]
    
    [:.mdl-checkbox__ripple-container
     [:.mdl-ripple
      {:background-color "#183D4E"}]]
    
    ;;;;;;;;slider
    
    [:#main-part
     [:.mdl-slider.is-upgraded
      {:color "#183D4E"}]]
    
    [:#main-part
     [:.mdl-slider__background-lower
      {:background "#183D4E"}]]
    
    [:#main-part
     [:.mdl-slider.is-upgraded
      ["&::-webkit-slider-thumb"
      {:background-color "#183D4E"}]]]
    
    [:#main-part
     [:.mdl-slider.is-upgraded
      ["&::-moz-range-thumb"
      {:background-color "#183D4E"}]]]
    
    [:#main-part
     [:.mdl-slider.is-upgraded
      ["&::-ms-thumb"
      {:background-color "#183D4E"}]]]
    
    [:#main-part
     [:.mdl-slider.is-upgraded.is-lowest-value
      ["&::-webkit-slider-thumb"
      {:border-color "#183D4E"}]]]
    
    [:#main-part
     [:.mdl-slider.is-upgraded.is-lowest-value
      ["&::-moz-range-thumb"
      {:border-color "#183D4E"}]]]
    
    ;;;;;;;;;;;Keep grey focus circle for non-start values
    
    [:#main-part
     [:.mdl-slider.is-upgraded
      [(s/& (s/focus (s/not s/active)))
       ["&::-webkit-slider-thumb"
        {:box-shadow "0 0 0 10px rgba(0,0,0, 0.12)"}]]]]
    

   ;;------------------------------------
   ))