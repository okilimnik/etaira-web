(ns etaira.ui.login-dialog
  (:require
   [etaira.model.account :as account]
   [com.fulcrologic.semantic-ui.modules.modal.ui-modal :refer [ui-modal]]
   [com.fulcrologic.semantic-ui.modules.modal.ui-modal-header :refer [ui-modal-header]]
   [com.fulcrologic.semantic-ui.modules.modal.ui-modal-content :refer [ui-modal-content]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.rad.authorization :as auth]
   [com.fulcrologic.fulcro.dom :refer [div label input]]
   [com.fulcrologic.fulcro.mutations :as m]
   [taoensso.timbre :as log]))

(defsc LoginForm [this {:ui/keys [username password] :as props} {:keys [visible?]}]
  {:query               [:ui/username
                         :ui/password]
   :initial-state       {:ui/username ""
                         :ui/password ""}

   ::auth/provider      :local
   ::auth/check-session `account/check-session
   ::auth/logout        `account/logout

   :ident               (fn [] [:component/id ::LoginForm])}
  (ui-modal {:open (boolean visible?) :dimmer true}
            (ui-modal-header {} "Please Log In")
            (ui-modal-content {}
                              (div :.ui.form
                                   (div :.ui.field
                                        (label "Username")
                                        (input {:type     "email"
                                                :onChange (fn [evt] (m/set-string! this :ui/username :event evt))
                                                :value    (or username "")}))
                                   (div :.ui.field
                                        (label "Password")
                                        (input {:type     "password"
                                                :onChange (fn [evt] (m/set-string! this :ui/password :event evt))
                                                :value    (or password "")}))
                                   (div :.ui.primary.button
                                        {:onClick (fn [] (comp/transact! this [(account/login {:username username :password password})]))}
                                        "Login")))))
