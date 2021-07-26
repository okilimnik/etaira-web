(ns etaira.ui
  (:require
   [com.fulcrologic.fulcro.dom :refer [div button a]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown :refer [ui-dropdown]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-menu :refer [ui-dropdown-menu]]
   [com.fulcrologic.semantic-ui.modules.dropdown.ui-dropdown-item :refer [ui-dropdown-item]]
   [com.fulcrologic.semantic-ui.modules.tab.ui-tab :refer [ui-tab]]
   [etaira.ui.login-dialog :refer [LoginForm]]
   [etaira.ui.ai.config :refer [AIConfigForm AIConfigList]]
   [etaira.ui.ai.model :refer [AIModelForm AIModelList]]
   [etaira.ui.dataset :refer [DatasetForm DatasetList]]
   [etaira.ui.indicator :refer [IndicatorForm IndicatorList]]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom.html-entities :as ent]
   [com.fulcrologic.fulcro.routing.dynamic-routing :refer [defrouter]]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.authorization :as auth]
   [com.fulcrologic.rad.routing :as rroute]
   [etaira.ui.home :refer [HomePage]]
   [etaira.ui.landing :refer [LandingPage]]))

(defrouter MainRouter [this {:keys [current-state route-factory route-props]}]
  {:always-render-body? true
   :router-targets      [HomePage LandingPage
                         AIConfigForm AIConfigList
                         AIModelForm AIModelList
                         DatasetForm DatasetList
                         IndicatorForm IndicatorList]}
  (div
   (div :.ui.loader {:classes [(when-not (= :routed current-state) "active")]})
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
    (div
     (div :.ui.top.menu
          (div :.ui.item "Etaira")
          (if logged-in?
            (comp/fragment
             (ui-dropdown {:className "item" :text "AI Configs"}
                          (ui-dropdown-menu {}
                                            (ui-dropdown-item {:onClick (fn [] (rroute/route-to! this AIConfigList {}))} "View All")
                                            (ui-dropdown-item {:onClick (fn [] (form/create! this AIConfigForm))} "New")))
             (ui-dropdown {:className "item" :text "Datasets"}
                          (ui-dropdown-menu {}
                                            (ui-dropdown-item {:onClick (fn [] (rroute/route-to! this DatasetList {}))} "View All")
                                            (ui-dropdown-item {:onClick (fn [] (form/create! this DatasetForm))} "New")))
             (ui-dropdown {:className "item" :text "Indicators"}
                          (ui-dropdown-menu {}
                                            (ui-dropdown-item {:onClick (fn [] (rroute/route-to! this IndicatorList {}))} "View All")
                                            (ui-dropdown-item {:onClick (fn [] (form/create! this IndicatorForm))} "New")))
             (ui-dropdown {:className "item" :text "AI Models"}
                          (ui-dropdown-menu {}
                                            (ui-dropdown-item {:onClick (fn [] (rroute/route-to! this AIModelList {}))} "View All")
                                            (ui-dropdown-item {:onClick (fn [] (form/create! this AIModelForm))} "New"))))
            (comp/fragment
             (div {:className "ui tabular menu"}
                  (a {:className "item" :data-tab "white-paper"} "White Paper")
                  (a {:className "item" :data-tab "docs"} "docs"))))
          (div :.right.menu

             

               (if logged-in?
                 (comp/fragment
                  (div :.ui.item
                       (str "Logged in as " username))
                  (div :.ui.item
                       (button :.ui.button {:onClick (fn []
                                                       (rroute/route-to! this LandingPage {})
                                                       (auth/logout! this :local))}
                               "Logout")))

                 (comp/fragment
                  #_(div :.item
                         (div :.ui.tiny.loader {:classes [(when busy? "active")]})
                         "Welcome to Etaira. Please log in." ent/nbsp ent/nbsp ent/nbsp ent/nbsp)
                  (div :.ui.item
                       (str "Welcome to Etaira. Please log in."))

                  (div :.ui.item
                       (button :.ui.primary.button {:onClick #(auth/authenticate! this :local nil)}
                               "Login"))))))
     (div
      (ui-authenticator authenticator)
      (ui-main-router router)))))

(def ui-root (comp/factory Root))
