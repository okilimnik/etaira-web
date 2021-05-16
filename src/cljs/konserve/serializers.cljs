(ns konserve.serializers
  (:require [konserve.protocols :refer [PStoreSerializer -serialize -deserialize]]
            [incognito.edn :refer [read-string-safe]]))

(defrecord StringSerializer []
  PStoreSerializer
  (-deserialize [_ read-handlers s]
    (read-string-safe @read-handlers s))
  (-serialize [_ output-stream _ val]
              (pr-str val)))

(defn string-serializer []
  (map->StringSerializer {}))