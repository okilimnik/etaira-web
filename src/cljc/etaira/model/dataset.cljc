(ns etaira.model.dataset
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

(defattr id :dataset/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr name :dataset/name :string
  {ao/identities #{:dataset/id}
   ao/required?  true
   ao/schema     :production})

(def exchanges
  #?(:cljs  #_(->> (for [exchange (oget js/ccxt :exchanges)]
                     [exchange (str (upper-case (first exchange))
                                    (subs exchange 1 (count exchange)))])
                   (apply concat)
                   (apply hash-map))
     {"binance" "Binance"}
     :clj {}))

(defattr exchange :dataset/exchange :enum
  {ao/identities        #{:dataset/id}
   ao/required?         true
   ao/enumerated-values (keys exchanges)
   ao/enumerated-labels exchanges
   ao/schema            :production
   fo/default-value     "binance"})

(defattr cryptopair :dataset/cryptopair :string
  {ao/identities    #{:dataset/id}
   ao/required?     true
   ao/schema        :production
   fo/default-value "BTC/USDT"})

(defattr date-from :dataset/date-from :instant
  {ao/identities    #{:dataset/id}
   ao/required?     true
   ao/schema        :production})

(defattr date-to :dataset/date-to :instant
  {ao/identities    #{:dataset/id}
   ao/required?     true
   ao/schema        :production})

(def intervals 
  ["1m"
   "3m"
   "5m"
   "15m"
   "30m"
   "1h"
   "2h"
   "4h"
   "6h"
   "8h"
   "12h"
   "1d"
   "3d"
   "1w"
   "1M"])

(defattr interval :dataset/interval :enum
  {ao/identities    #{:dataset/id}
   ao/required?     true
   ao/enumerated-values intervals
   ao/enumerated-labels (apply hash-map (mapcat (fn [i] [i i]) intervals))
   ao/schema        :production})

(defattr indicators :dataset/indicators :enum
  {ao/identities  #{:dataset/id}
   ao/cardinality :many
   ao/required?   true
   ao/schema      :production})

(defattr outputs :dataset/outputs :ref
  {ao/identities  #{:dataset/id}
   ao/cardinality :many
   ao/required?   true
   ao/schema      :production})

(defattr all-datasets :dataset/all-datasets :ref
  {ao/target     :dataset/id
   ao/pc-output  [{:dataset/all-datasets [:dataset/id]}]
   ao/pc-resolve (fn [{:keys [query-params] :as env} _]
                   #?(:clj
                      {:dataset/all-datasets (queries/get-all-datasets env query-params)}))})

#?(:cljs (defmutation delete-dataset
           "Mutation: Delete the dataset with `:dataset/id` from the list with `:list/id`"
           [{list-id   :list/id
             dataset-id :dataset/id}]
           (action [{:keys [state]}]
                   (swap! state merge/remove-ident* [:dataset/id dataset-id] [:list/id list-id :list/datasets]))))

(def attributes [id name exchange cryptopair all-datasets])