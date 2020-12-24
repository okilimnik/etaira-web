(ns etaira.ui
  (:require
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown :refer [ui-dropdown]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-menu :refer [ui-dropdown-menu]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-item :refer [ui-dropdown-item]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div label input]]
   [etaira.ui.login-dialog :refer [LoginForm]]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom.html-entities :as ent]
   [com.fulcrologic.fulcro.routing.dynamic-routing :refer [defrouter]]
   [com.fulcrologic.rad.authorization :as auth]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.ids :refer [new-uuid]]
   [com.fulcrologic.rad.routing :as rroute]
   [taoensso.timbre :as log]))

(defsc LandingPage [this props]
  {:query         ['*]
   :ident         (fn [] [:component/id ::LandingPage])
   :initial-state {}
   :route-segment ["landing-page"]}
  (dom/div "Welcome to the Demo. Please log in."))

;; This will just be a normal router...but there can be many of them.
(defrouter MainRouter [this {:keys [current-state route-factory route-props]}]
  {:always-render-body? true
   :router-targets      [LandingPage]}
  ;; Normal Fulcro code to show a loader on slow route change (assuming Semantic UI here, should
  ;; be generalized for RAD so UI-specific code isn't necessary)
  (dom/div
    (dom/div :.ui.loader {:classes [(when-not (= :routed current-state) "active")]})
    (when route-factory
      (route-factory route-props))))

(def ui-main-router (comp/factory MainRouter))

(auth/defauthenticator Authenticator {:local LoginForm})

(def ui-authenticator (comp/factory Authenticator))

(defsc Root [this {::auth/keys [authorization]
                   ::app/keys  [active-remotes]
                   :keys       [authenticator router]}]
  {:query         [{:authenticator (comp/get-query Authenticator)}
                   {:router (comp/get-query MainRouter)}
                   ::app/active-remotes
                   ::auth/authorization]
   :initial-state {:router        {}
                   :authenticator {}}}
  (let [logged-in? (= :success (some-> authorization :local ::auth/status))
        busy?      (seq active-remotes)
        username   (some-> authorization :local :account/name)]
    (dom/div
      (div :.ui.top.menu
           (div :.ui.item "Demo")
           (div :.right.menu
                (div :.item
                     (div :.ui.tiny.loader {:classes [(when busy? "active")]})
                     ent/nbsp ent/nbsp ent/nbsp ent/nbsp)
                (if logged-in?
                  (comp/fragment
                   (div :.ui.item
                        (str "Logged in as " username))
                   (div :.ui.item
                        (dom/button :.ui.button {:onClick (fn []
                                                    ;; TODO: check if we can change routes...
                                                            (rroute/route-to! this LandingPage {})
                                                            (auth/logout! this :local))}
                                    "Logout")))
                  (div :.ui.item
                       (dom/button :.ui.primary.button {:onClick #(auth/authenticate! this :local nil)}
                                   "Login")))))
      (div :.ui.container.segment
        (ui-authenticator authenticator)
        (ui-main-router router)))))

(def ui-root (comp/factory Root))

