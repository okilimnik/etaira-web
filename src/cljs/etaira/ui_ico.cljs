(ns etaira.ui-ico
  (:require
   [com.fulcrologic.fulcro.dom :refer [div button a p img i span h3]]))

(def use-cases-content
  [{:icon "fas fas-camera"
    :head "head1"
    :text "text1"}
   {:icon "fab-camera"
    :head "head2"
    :text "text2"}
   {:icon "fas fa-ad"
    :head "head3"
    :text "text3"}
   {:icon "fa-camera"
    :head "head4"
    :text "text4"}])

(defn use-cases []
  (div {:class "two column stackable ui grid"
        :style {:background-color "#00ffff"
                :width "100%"
                :height "700px"
                :text-align "center"
                :display "flex"
                :justify-content "space-between"
                :position "relative"
                :padding "100px 0"}}
       (div {:class "sixteen wide column"}
            (h3 {:style
                 {:color "#013220"
                  :font-size "250%"}}
                "Use Cases"))
       
       (for [item use-cases-content]
         (div {:class "eight wide column"}
              (div {:class "two column stackable ui grid"}
                   (div {:class "four wide column"}
                        (i {:class (get item :icon)}))
                   (div {:class "twelve wide column"}
                        (p {:style {:font-size "150%"}}
                           (get item :head)))
                   (div {:class "four wide column"}
                        (p ""))
                   (div {:class "twelve wide column"}
                        (p (get item :text)))))
         )
       



       #_(div {:class "eight wide column"}
            (div {:class "two column stackable ui grid"}
                 (div {:class "four wide column"}
                      (i {:prefix "fas"
                          :iconName "fa-camera"}))
                 (div {:class "twelve wide column"}
                      (p "111111111111111111111111111111111111 1111111111111111111111 11111111111111111111111111111"))
                 (div {:class "four wide column"}
                      (p ""))
                 (div {:class "twelve wide column"}
                      (p "222222222222222222 4444444444444444 55555555555555555"))
                 ))
       



      ;;  (div {:class "eight wide column"} "11122222222222222222222222222222222222222222222222221")
      ;;  (div {:class "eight wide column"} "111333333333333333333333333333333333333333333333331")
      ;;  (div {:class "eight wide column"} "111444444444444444444444444444444444444444444441")
       ))

(defn MainPage []
    (div (use-cases)))