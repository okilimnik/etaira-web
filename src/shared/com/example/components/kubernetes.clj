(ns com.example.components.kubernetes
  (:require [clojure.java.io :as io])
  (:import
   [io.kubernetes.client.openapi Configuration]
   [io.kubernetes.client.openapi.apis CoreV1Api]
   [io.kubernetes.client.openapi.models V1Pod]
   [io.kubernetes.client.util KubeConfig ClientBuilder]))

(defn init []
  (with-open [reader (io/reader (io/resource "kube/config"))]
    (let [client (.build (ClientBuilder/kubeconfig (KubeConfig/loadKubeConfig reader)))]
      (Configuration/setDefaultApiClient client)
      (let [api (CoreV1Api.)
            list (.listPodForAllNamespaces api false "" "" "" (int 5) "" "" (int 3000) false)]
        (doseq [^V1Pod item (.getItems list)]
          #_(let [class! (.getClass item)
                  methods (.getMethods class!)]
              (doseq [method methods]
                (println method)))
          (println (.getName (.getMetadata item))))))))