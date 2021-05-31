(ns etaira.ui-ico-segments.use-cases
(:require
   [com.fulcrologic.fulcro.dom :refer [div p h3]]
   [com.fulcrologic.semantic-ui.elements.icon.ui-icon :refer [ui-icon]]))

(def use-cases-content
  [{:icon "universal access"
    :head "Trade your favorite asset on your favorite exchange."
    :text "Etaira supports a large list of exchanges. The full list see <a href=''>here<a/>"}
   {:icon "edit outline"
    :head "Train your neural network based advisors locally in the browser or in the cloud."
    :text "Powered by Google's Tensorflow Etaira allows you to create and train neural network models right in the browser as well as in the cloud."}
   {:icon "share square outline"
    :head "Sell your trained advisors to or buy them from other users."
    :text "Are you a talented researcher? Neural network specialist? Write neural network based advisors and sell them to Etaira users at the marketplace."}
   {:icon "balance scale"
    :head "Invest and grow."
    :text "Are you an investor? Buy our tokens and grow with us."}])

;;green huge

(defn use-cases []
  (div :.stackable.ui.grid
       {:style {:backgroundColor "#00ffff"
               :width "100%"
               :minHeight "800px"
               :textAlign "center"
               :display "flex"
               :justifyContent "space-between"
               :position "relative"
               :padding "100px 0"}}
       (div :.sixteen.wide.column
            (h3 {:style
                 {:color "#013220"
                  :fontSize "250%"}}
                "Use Cases"))

       (for [[index item] (map-indexed vector use-cases-content)]
         (div :.six.wide.centered.column {:key index}
              (div :.two.column.ui.grid
                   (div :.right.aligned.four.wide.column
                        
                             (ui-icon {:name (get item :icon)
                                       :className "green huge"})
                        )
                   (div :.left.aligned.twelve.wide.column
                        (p {:style {:fontSize "170%"}}
                           (get item :head)))
                   (div :.right.aligned.four.wide.column
                        (p ""))
                   (div :.left.aligned.twelve.wide.column
                        (p {:style {:fontSize "130%"}}
                         (get item :text))))))))
