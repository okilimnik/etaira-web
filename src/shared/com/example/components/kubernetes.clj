(ns com.example.components.kubernetes
  (:require [clojure.java.io :as io])
  (:import
   [java.io FileReader]
   [io.kubernetes.client.openapi Configuration]
   [io.kubernetes.client.openapi.apis CoreV1Api]
   [io.kubernetes.client.openapi.models V1Pod]
   [io.kubernetes.client.util KubeConfig ClientBuilder]))

(defn init []
  (let [kubeConfigPath (io/resource "kube/config")
        client (.build (ClientBuilder/kubeconfig (KubeConfig/loadKubeConfig (FileReader. kubeConfigPath))))]
    (Configuration/setDefaultApiClient client)
    (let [api (CoreV1Api.)
          list (.listPodForAllNamespaces api false "" "" "" (int 5) "" "" (int 3000) false)]
      (doseq [^V1Pod item (.getItems list)]
        #_(let [class! (.getClass item)
              methods (.getMethods class!)]
          (doseq [method methods]
            (println method)))
        (println (.getName (.getMetadata item)))))))