(ns etaira.client
  (:require
   [etaira.ui :as ui :refer [Root]]
   [etaira.ui.login-dialog :refer [LoginForm]]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.mutations :as m]
   [com.fulcrologic.rad.application :as rad-app]
   [etaira.app :refer [etaira-app]]
   [com.fulcrologic.rad.report :as report]
   [com.fulcrologic.rad.authorization :as auth]
   [com.fulcrologic.rad.rendering.semantic-ui.semantic-ui-controls :as sui]
   [com.fulcrologic.fulcro.algorithms.timbre-support :refer [console-appender prefix-output-fn]]
   [taoensso.timbre :as log]
   [taoensso.tufte :as tufte :refer [profile]]
   [com.fulcrologic.rad.type-support.date-time :as datetime]
   [com.fulcrologic.fulcro.algorithms.tx-processing.synchronous-tx-processing :as stx]
   [com.fulcrologic.rad.routing.html5-history :as hist5 :refer [html5-history]]
   [com.fulcrologic.rad.routing.history :as history]
   [com.fulcrologic.rad.routing :as routing]
   [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
   [etaira.ui.home :refer [HomePage]]
   [etaira.ui.landing :refer [LandingPage]]))

(defonce stats-accumulator
  (tufte/add-accumulating-handler! {:ns-pattern "*"}))

(m/defmutation fix-route
  "Mutation. Called after auth startup. Looks at the session. If the user is not logged in, it triggers authentication"
  [_]
  (action [{:keys [app]}]
          (let [logged-in (auth/verified-authorities app)]
            (if (empty? logged-in)
              (routing/route-to! app LandingPage {})
              (hist5/restore-route! app HomePage {})))))

(defn setup-RAD [app]
  (rad-app/install-ui-controls! app sui/all-controls)
  (report/install-formatter! app :boolean :affirmation (fn [_ value] (if value "yes" "no"))))

(defn refresh []
  ;; hot code reload of installed controls
  (log/info "Reinstalling controls")
  (setup-RAD etaira-app)
  (comp/refresh-dynamic-queries! etaira-app)
  (app/mount! etaira-app Root "app"))

(defn init []
  (log/merge-config! {:output-fn prefix-output-fn
                      :appenders {:console (console-appender)}})
  (log/info "Starting App")
  ;; default time zone (should be changed at login for given user)
  (datetime/set-timezone! "America/Los_Angeles")
  ;; Avoid startup async timing issues by pre-initializing things before mount
  (app/set-root! etaira-app Root {:initialize-state? true})
  (dr/initialize! etaira-app)
  (setup-RAD etaira-app)
  (app/mount! etaira-app Root "app" {:initialize-state? false})
  (dr/change-route! etaira-app ["landing-page"])
  (history/install-route-history! etaira-app (html5-history))
  (auth/start! etaira-app [LoginForm] {:after-session-check `fix-route}))

(comment)

(defonce performance-stats (tufte/add-accumulating-handler! {}))

(defn pperf
  "Dump the currently-collected performance stats"
  []
  (let [stats (not-empty @performance-stats)]
    (println (tufte/format-grouped-pstats stats))))