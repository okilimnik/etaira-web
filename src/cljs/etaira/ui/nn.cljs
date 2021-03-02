(ns etaira.ui.nn
  (:require
   [oops.core :refer [oget ocall oset!]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div button i span label select
                                               option h4 p canvas input]]
   [etaira.ui.nn.top-controls :refer [top-controls]]
   [etaira.ui.nn.data-column :refer [data-column]]
   [etaira.ui.nn.features-column :refer [features-column]]
   [etaira.ui.nn.hidden-layers-column :refer [hidden-layers-column]]
   [etaira.ui.nn.output-column :refer [output-column]]
   [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
   [etaira.nn.dataset :as dataset]
   ["@tensorflow/tfjs" :refer [regularizers]]
   ["d3" :as d3]))

(def ^:const RECT_SIZE 30)
(def ^:const BIAS_SIZE 5)
(def ^:const NUM_SAMPLES_CLASSIFY 500)
(def ^:const NUM_SAMPLES_REGRESS 1200)
(def ^:const DENSITY 100)

(defn main-view []
  (div :#main-part.l--page
       (data-column)
       (features-column)
       (hidden-layers-column)
       (output-column)))

(def regularizations
  {"none" nil
   "L1"   (oget regularizers :l1)
   "L2"   (oget regularizers :l2)})

(def datasets
  {"circle" dataset/classify-circle-data
   "xor"    dataset/classify-xor-data
   "gauss"  dataset/classify-two-gauss-data
   "spiral" dataset/classify-spiral-data})

(def reg-datasets
  {"reg-plane" dataset/regress-plane
   "reg-gauss" dataset/regress-gaussian})

(def problems
  [:classification :regression])

(def first-interaction? (atom true))
(def parameters-changed? (atom false))

(defn user-has-interacted []
  (when @first-interaction?
    (reset! first-interaction? false)))

(defn simulation-started []
  (reset! parameters-changed? false))

(defmutation update-seed
  [{:keys [seed]}] 
  (action [{:keys [state]}]
          (swap! state assoc ::seed seed)))

(defn generate-data [{:etaira.ui.nn/keys [seed problem dataset reg-dataset noise perc-train-data show-test-data?] :as this} first-time?]
  (when-not first-time?
    (comp/transact! this [(update-seed {:seed (.toFixed (js/Math.random) 5)})])
    (user-has-interacted))
    ;; Change the seed.
  (js/Math.seedrandom seed)
  (let [numSamples (if (= problem :regression) NUM_SAMPLES_REGRESS NUM_SAMPLES_CLASSIFY)
        generator (if (= problem :classification) dataset reg-dataset)
        data (generator numSamples (/ noise 100))]
      ;; Shuffle the data in-place.
    (shuffle data)
      ;; Split into train and test data.
    (let [splitIndex (js/Math.floor (-> (oget data :length)
                                        (* perc-train-data)
                                        (/ 100)))
          trainData (.slice data 0 splitIndex)
          testData (.slice data splitIndex)]
      (heatmap/update-points heatmap trainData)
      (heatmap/update-test-points  (if show-test-data? testData [])))))

(defn draw-dataset-thumbnails [{:etaira.ui.nn/keys [problem]}]
  (let [render-thumbnail (fn [canvas data-generator]
                           (let [w 100
                                 h 100]
                             (ocall canvas :setAttribute "width" w)
                             (ocall canvas :setAttribute "height" h)
                             (let [context (ocall canvas :getContext "2d")
                                   data (data-generator 200 0)]
                               (.forEach data (fn [d]
                                                (oset! context :fillStyle (color-scale (oget d :label)))
                                                (ocall context :fillRect
                                                       (-> (oget d :x)
                                                           (+ 6)
                                                           (* w)
                                                           (/ 12))
                                                       (-> (oget d :y)
                                                           (+ 6)
                                                           (* h)
                                                           (/ 12))
                                                       4 4)))
                               (-> (ocall d3 :select (oget canvas :parentNode))
                                   (ocall :style "display" nil)))))]
    (-> (ocall d3 :selectAll ".dataset")
        (ocall :style "display" "none"))
    (case problem
      :classification (for [dataset datasets]
                        (let [canvas (js/document.querySelector (str "canvas[data-dataset=" dataset "]"))
                              data-generator (get datasets dataset)]
                          (render-thumbnail canvas data-generator)))
      :regression (for [reg-dataset reg-datasets]
                    (let [canvas (js/document.querySelector (str "canvas[data-regDataset=" reg-dataset "]"))
                          data-generator (get reg-datasets reg-dataset)]
                      (render-thumbnail canvas data-generator)))
      "not implemented")))

(defn make-gui [{:etaira.ui.nn/keys [dataset]}]
  (-> (ocall d3 :select "#reset-button")
      (ocall :on "click" (fn []
                           (reset)
                           (user-has-interacted)
                           (ocall d3 :select "#play-pause-button"))))
  (-> (ocall d3 :select "#play-pause-button")
      (ocall :on "click" (fn []
                           ;; Change the button's content.
                           (user-has-interacted)
                           (play-or-pause player))))
  (on-play-pause player (fn [playing?]
                          (-> (ocall d3 :select "#play-pause-button")
                              (ocall :classed "playing" playing?))))
  (-> (ocall d3 :select "#next-step-button")
      (ocall :on "click" (fn []
                           (pause player)
                           (user-has-interacted)
                           (when (= iter 0)
                             (simulation-started))
                           (one-step))))
   (-> (ocall d3 :select "#data-regen-button")
       (ocall :on "click" (fn []
                            (generate-data)
                            (reset! parameters-changed? true))))
  (let [data-thumbnails (ocall d3 :selectAll "canvas[data-dataset]")]
    (ocall data-thumbnails :on "click" (fn []
                                         (let [new-dataset (get datasets dataset)]
                                           (ocall data-thumbnails :classed "selected" false)
                                           (ocall d3 :select (str "canvas[data-dataset=" dataset "]") "selected" true)
                                           (generate-data)
                                           (reset! parameters-changed? true)
                                           (reset)))))
  
  )

(defn reset [{:etaira.ui.nn/keys [num-hidden-layers problem]} on-startup?]
  (.reset line-chart)
  (when-not on-startup?
    (user-has-interacted))
  (pause player)

  (let [suffix (if (= num-hidden-layers 1) "" "s")]
    (-> (ocall d3 :select "#layers-label")
        (ocall :text (str "Hidden layer" suffix)))
    (-> (ocall d3 :select "#num-layers")
        (ocall :text num-hidden-layers)))

  ;; Make a simple network.
  (let [iter 0
        numInputs (oget (construct-input 0 0) :length)
        shape 1
        outputActivation (if (= problem :regression)
                           :linear
                           :tanh)
        network (build-network)
        lossTrain (get-loss network train-data)
        lossTest (get-loss network test-data)]
    (draw-network network)
    (update-ui true)))



(defsc NeuralNetwork [this {}]
  {:initial-state
   (fn []
     {::activation         :tanh
      ::regularization     nil
      ::batchSize          10
      ::dataset            (first datasets)
      ::regDataset         (first reg-datasets)
      ::learningRate       0.03
      ::regularizationRate 0
      ::noise              0
      ::networkShape       [4 2]
      ::seed               nil
      ::show-test-data?       false
      ::discretize         false
      ::perc-train-data      50
      ::x                  true
      ::y                  true
      ::xTimesY            false
      ::xSquared           false
      ::ySquared           false
      ::cosX               false
      ::sinX               false
      ::cosY               false
      ::sinY               false
      ::collectStats       false
      ::tutorial           nil
      ::problem            (first problems)
      ::initZero           false
      ::hideText           false
      ::num-hidden-layers    1
      ::hiddenLayerControls []})
   :componentDidMount
   (fn [this]
     (draw-dataset-thumbnails this)
     (make-gui this)
     (generate-data true)
     (reset this true)
     (hide-controls true))}
  (div
   (top-controls)
   (main-view)))

(def ui-neural-network (comp/factory NeuralNetwork {}))