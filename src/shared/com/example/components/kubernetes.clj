(ns com.example.components.kubernetes
  (:import
   [java.io FileReader]
   [io.kubernetes.client.openapi Configuration]
   [io.kubernetes.client.openapi.apis CoreV1Api]
   [io.kubernetes.client.openapi.models V1Pod]
   [io.kubernetes.client.util Config KubeConfig ClientBuilder]))

(defn local-init []
  (let [kubeConfigPath (str (System/getenv "HOME") "/.kube/config")
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

(defn init []
  (Configuration/setDefaultApiClient (Config/defaultClient))
  (let [api (CoreV1Api.)
        list (.listPodForAllNamespaces api false "" "" "" (int 5) "" "" (int 3000) false)]
    (doseq [^V1Pod item (.getItems list)]
      #_(let [class! (.getClass item)
              methods (.getMethods class!)]
          (doseq [method methods]
            (println method)))
      (println (.getName (.getMetadata item))))))