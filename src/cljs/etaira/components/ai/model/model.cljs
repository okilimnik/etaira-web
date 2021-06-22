(ns etaira.components.ai.model.model
  (:require
   ["@tensorflow/tfjs" :as tfjs]
   [oops.core :refer [oget+ oget ocall]]
   [etaira.interop.async :refer [async await await-all]]
   [etaira.indicators.talib.api :as talib]))

(def dense (.. tfjs -layers -dense))
(def tdata (.-data tfjs))

(defn generate-layers [layers]
  (for [{:neural-network-layer/keys [number-of-neurons type]} layers]
    (dense #js {:units number-of-neurons})))

(defn build-model [{:neural-network-config/keys [problem layers]} num-features]
  (let [model (.sequential tfjs)]
    (.add model (dense #js {:units 20
                            :inputShape #js [1 num-features]}))
    (doseq [layer (generate-layers layers)]
      (.add model layer))
    (case problem
      :classification (.add model (dense #js {:units 1 :activation "sigmoid"}))
      :regression (.add model (dense #js {:units 1 :activation "sigmoid"}))
      (.add model (dense #js {:units 1 :activation "sigmoid"})))
    (.compile model #js {:loss "binaryCrossentropy" :optimizer "adam"})
    (.summary model)
    model))

(def EPOCHS 100)
(def BATCH-SIZE 20)

(defn calculate-number-of-features [dataset-db indicators]
  (async
   (+ (count (-> (await (.get dataset-db #js {:id 1}))
                 (js->clj :keywordize-keys true)
                 (dissoc :id)
                 keys))
      (count (mapcat :indicator/outputs indicators)))))

(defn calc-training-total [dataset-db]
  (int (* (await (.count dataset-db)) 0.7)))

(defn calc-batches-per-epoch [training-total]
  (dec (dec (int (/ training-total BATCH-SIZE)))))

(defn create-dataset [next-batch-fn]
  (.generator tdata next-batch-fn))

(def callbacks
  #js {:onTrainBegin (fn [logs] false)
       :onTrainEnd   (fn [logs]
                       (println "train end."))
       :onEpochEnd   (fn [epoch logs]
                       (js/console.log epoch)
                       (js/console.log logs)
                       (reset! batch-number 1))
       :onBatchEnd   (fn [batch logs]
                       (js/console.log logs))})

(defn create-training-options [batches-per-epoch validation-dataset]
  #js {:batchesPerEpoch batches-per-epoch
       :epochs          EPOCHS
       :callbacks       callbacks
       :validationData  validation-dataset})

(defn train! [{:keys [id config dataset]} dataset-db]
  (async
   (let [indicators (:dataset/indicators dataset)
         num-features (await (calculate-number-of-features dataset-db indicators))
         model (build-model config num-features)
         training-total (calc-training-total dataset-db)
         batches-per-epoch (calc-batches-per-epoch training-total)
         get-next-batch-fn' (partial get-next-batch-fn dataset-db indicators num-features training-total batches-per-epoch)
         train-dataset (create-dataset #(get-next-batch-fn' false))
         validation-dataset (create-dataset #(get-next-batch-fn' true))
         options (create-training-options batches-per-epoch validation-dataset)]
     (.fitDataset model train-dataset options))))