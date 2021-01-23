(ns etaira.ui.nn.features-column
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div button i span label select path b svg defs marker
                                               option h4 p canvas input]]))

(defn features-column []
  (div :.column.features
       (h4 "Features")
       (p "Which properties do you want to feed in?")
       (div :#network
            (svg :#svg
                 {:width "510"
                  :height "450"}
                 (defs 
                   (marker :#markerArrow
                           {:markerWidth "7"
                            :markerHeight "13"
                            :refX "1"
                            :refY "6"
                            :orient "auto"
                            :markerUnits "userSpaceOnUse"}
                           (path {:d [M2 11 L7 6 L2 2]}))))
            ;;;;;hover card
            (div :#hovercard
                 (div {:style {:font-size "10px"}}
                      "Click anywhere to edit.")
                 (div
                  (span :.type "Weight/Bias") "is" 
                  (span :.value "0.2") 
                  (span (input {:type "number"})) "."))
            (div :.callout.thumbnail
                 (svg {:viewBox [0 0 30 30]}
                      (defs 
                        (marker :#arrow
                                {:markerWidth "5"
                                 :markerHeight "5"
                                 :refx "5"
                                 :refy "2.5"
                                 :orient "auto"
                                 :markerUnits "userSpaceOnUse"}
                                (path {:d [M0 0 L5 2.5 L0 5 z]})))
                      (path {:d [M12 30 C5 20 2 15 12 0]
                             :marker-end "url(#arrow)"}))
                 (div :.label
                      "This is the output from one " (b "neuron") ". Hover to see it larger"))
            (div :.callout.weights
                 (svg {:viewBox [0 0 30 30]}
                      (defs
                        (marker :#arrow
                                {:markerWidth "5"
                                 :markerHeight "5"
                                 :refx "5"
                                 :refy "2.5"
                                 :orient "auto"
                                 :markerUnits "userSpaceOnUse"}
                                (path {:d [M0 0 L5 2.5 L0 5 z]})))
                      (path {:d [M12 30 C5 20 2 15 12 0]
                             :marker-end "url(#arrow)"}))
                 (div :.label
                      "The outputs are mixed with varying " (b "weights") ", shown by the thickness of the lines.")))))


   
        
       
          