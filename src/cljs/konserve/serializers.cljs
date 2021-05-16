(ns konserve.serializers
  (:require [konserve.protocols :refer [PStoreSerializer -serialize -deserialize]]
            [fress.api :as fress]
            [incognito.fressian :refer [incognito-read-handlers incognito-write-handlers]]
            [incognito.edn :refer [read-string-safe]]))

(defrecord FressianSerializer [custom-read-handlers custom-write-handlers]
  PStoreSerializer
  (-deserialize [_ read-handlers bytes]
    (let [buf->arr (.from js/Array (.from js/Int8Array bytes))
          buf      (fress.impl.buffer/BytesOutputStream. buf->arr (count buf->arr))
          reader   (fress/create-reader buf
                                        :handlers (merge custom-read-handlers
                                                         (incognito-read-handlers read-handlers)))
          read     (fress/read-object reader)]
      read))
  (-serialize [_ bytes write-handlers val]
    (let [writer (fress/create-writer bytes
                                      :handlers (merge
                                                 custom-write-handlers
                                                 (incognito-write-handlers write-handlers)))]
      (fress/write-object writer val))))

(defn fressian-serializer
  ([] (fressian-serializer {} {}))
  ([read-handlers write-handlers] (map->FressianSerializer {:custom-read-handlers read-handlers
                                                            :custom-write-handlers write-handlers})))
(defrecord StringSerializer []
  PStoreSerializer
  (-deserialize [_ read-handlers s]
    (read-string-safe @read-handlers s))
  (-serialize [_ output-stream _ val]
              (pr-str val)))

(defn string-serializer []
  (map->StringSerializer {}))