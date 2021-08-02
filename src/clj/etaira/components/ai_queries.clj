(ns etaira.components.ai-queries
  (:import [com.google.cloud.aiplatform.v1 DatasetServiceSettings DatasetServiceClient LocationName Dataset]
           [com.google.protobuf Value]
           [com.google.protobuf.util JsonFormat]
           [java.util.concurrent TimeUnit]))

(def client (atom nil))

(defn init []
  (let [settings (-> (DatasetServiceSettings/newBuilder)
                     (.setEndpoint "us-central1-aiplatform.googleapis.com:443")
                     (.build))]
    (reset! client (DatasetServiceClient/create settings))))

(defn create-dataset [project datasetDisplayName gcsSourceUri]
  
  (when-not @client (init))

  (let [location "us-central1"
        metadataSchemaUri "gs://google-cloud-aiplatform/schema/dataset/metadata/tables_1.0.0.yaml"
        locationName (LocationName/of project location)
        jsonString (str "{\"input_config\": {\"gcs_source\": {\"uri\": [\"" gcsSourceUri "\"]}}}")
        metaData (Value/newBuilder)]
    (-> (JsonFormat/parser)
        (.merge jsonString metaData))
    (let [dataset (-> (Dataset/newBuilder)
                      (.setDisplayName datasetDisplayName)
                      (.setMetadataSchemaUri metadataSchemaUri)
                      (.setMetadata metaData)
                      (.build))
          datasetFuture (-> @client
                            (.createDatasetAsync locationName dataset))
          _ (println "Waiting for operation to finish...")
          datasetResponse (.get datasetFuture 300 TimeUnit/SECONDS)]
      (println "Create Dataset Table GCS sample")
      (println (str "Name: " (.getName datasetResponse) "\n")))))