(ns com.example.components.seeded-connection
  (:require
   [mount.core :refer [defstate]]
   [konserve-fire.core :refer [new-fire-store]]
   [clojure.core.async :refer [<!!]]
   [com.fulcrologic.rad.database-adapters.key-value :refer [make-key-store]]))

;;
;; We've got a tiny database so let's seed it every time we refresh
;; Far less confusing not to have this :on-reload thing - change the seed function and it will be run!
;; ^{:on-reload :noop}
;;
(defstate kv-connections
  "The connection to the database that has just been freshly populated"
  :start {:main (make-key-store (<!! (new-fire-store "fire" :root "/konserve" :db "hetaira-gcp"))
                                "hetaira-web" {})})
