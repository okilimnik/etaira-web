(ns etaira.indicators.talib.parser
  (:require
   [clojure.data.json :as json]))

(defmacro defapi [name file]
  (let [json (json/read-str (slurp (str "resources/" file)) :key-fn keyword)]
    `(def ~name ~json)))