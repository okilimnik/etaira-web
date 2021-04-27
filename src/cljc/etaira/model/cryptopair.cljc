(ns etaira.model.cryptopair
  (:refer-clojure :exclude [name])
  (:require
   #?@(:clj
       [[etaira.components.database-queries :as queries]]
       :cljs
       [[com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
        [oops.core :refer [oget]]
        ["ccxt/dist/ccxt.browser"]])
   [clojure.string :refer [upper-case]]
   [com.fulcrologic.fulcro.algorithms.merge :as merge]
   [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
   [com.fulcrologic.rad.attributes-options :as ao]
   [com.fulcrologic.rad.form-options :as fo]))

#?(:cljs (defmutation load-cryptopairs
           "Mutation: Loads exchange cryptopairs."
           [{:keys [cryptopairs]}]
           (action [{:keys [state]}]
                   (let [target-path [:com.fulcrologic.rad.picker-options/options-cache :cryptopair/all-cryptopairs :options]]
                     (swap! state assoc-in target-path cryptopairs)
                     (swap! state assoc-in [:cryptopair/id] (into {} (mapv (juxt :cryptopair/id identity) (mapv :value cryptopairs))))))))