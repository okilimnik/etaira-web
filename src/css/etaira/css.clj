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
    {:margin [(px 5) 0]
     :font-weight "300"
     :font-size (px 18)}]

   [:h3
    {:margin [(px 10) 0]}]

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

   (at-media
    {:min-width (px 1180)}
    [:.l--page
     {:width (px 1100)}])

   (at-media
    {:min-width (px 1400)}
    [:.l--page
     {:width (px 1220)}])

   ;;Buttons

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

   [:.button
    {:cursor "pointer"
     :display "-webkit-box -moz-box -ms-flexbox -webkit-flex flex"
     :align-items "center"
     :-webkit-justify-content "center"
     :justify-content "center"
     :width (px 24)
     :height (px 24)
     :font-size (px 18)
     :border-radius "50%"
     :margin [0 (px 1)]
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
     :padding [(px 6) (px 4) (px 8) (px 4)]
     :border-left [(px 1) "solid" "#2c2c2c"]
     :border-bottom [(px 1) "solid" "#2c2c2c"]
     :position "fixed"
     :right (px 0)
     :background "#1a1a1a"
     :color "#eee"
     :font [(px 11) "'Lucida Grande'" "sans-serif"]
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
    [:.icon
     {:fill "#fff"
      :fill-opacity 0.6}]]

   [:.github-link
    [:&:hover
     [:.icon
      {:fill-opacity 0.7}]]]

   [:header
    {:border-bottom ["solid" (px 1) "rgba(0,0,0,0.4)"]
     :background-color "#183D4E"
     :color "white"
     :overflow "hidden"
     :box-shadow [(px 0) (px 2) (px 4) "rgba(0,0,0,0.2)"]
     :position "relative"}]

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

   [:#top-controls]
   
   ;;------------------------------------
   ))