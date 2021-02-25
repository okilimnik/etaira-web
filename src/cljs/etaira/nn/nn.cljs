(ns etaira.nn.nn
  (:require
   [oops.core :refer [oget ocall]]
   ["d3" :as d3]))

(defn default-link []
  {:id nil
   :source nil
   :dest nil
   :weight (- (rand) 0.5)
   :is-dead 0
   :error-der 0
   :acc-error-der 0
   :num-accumulated-ders 0
   :regularization nil})

(defn link-constructor [source dest regularization init-zero]
  (merge (default-link) {:id (str (get source :id) "-" (get dest :id))
                 :source source
                 :dest dest
                 :regularization regularization}
         (when init-zero {:weight 0})))

(def node (let [id ""
                input-links link
                bias 0.1
                outputs link
                total-input 0
                output 0
                output-der 0
                input-der 0
                acc-input-der 0
                num-accumulated-ders 0
                activation activation-function
                update-output (fn []
                                (let [total-input (atom bias)]
                                  (loop [j 0]
                                    (let [link (get input-links j)]
                                      (reset! total-input (+ @total-input (* (get link :weight) (get-in link [:source :output]))))
                                      (if (< j (count input-links))
                                        (recur (inc j))
                                        (let [output (-> activation
                                                         (ocall :output total-input))]
                                          (output)))))))
                node {}]
            (assoc node {:id id
                         :input-links input-links
                         :bias bias
                         :outputs outputs
                         :total-input total-input
                         :output output
                         :output-der output-der
                         :input-der input-der
                         :acc-input-der acc-input-der
                         :num-accumulated-ders num-accumulated-ders
                         :activation activation
                         :update-output update-output})))

(defn node-constructor [id activation init-zero]
  (let [id id
        activation activation
        bias (if init-zero
               0
               (get node :bias))
        new-node node]
    (assoc new-node :id id :activation activation :bias bias)))

 (defn square [output target]
   (let [error (* 0.5 (js/Math.pow (- output target) 2))
         der (- output target)
         square-result []]
     (conj square-result {:error error :der der})))


(defn math-tanh [x]
  (or (js/Math.tanh x) (if (= x ##Inf)
                         1
                         (if (= x ##-Inf)
                           -1
                           (let [e2x (js/Math.exp (* 2 x))]
                             (/ (- e2x 1) (+ e2x 1)))))))

(defn tanh []
  (let [output (fn [x]
                 (math-tanh x))
        der (fn [x]
              (let [output (output x)]
                (- 1 (* output output))))
        tanh-result []]
    (conj tanh-result {:output output :der der})))
 
(defn relu []
  (let [output (fn [x]
                 (js/Math.max 0 x))
        der (fn [x]
              (if (<= x 0)
                0
                1))
        relu-result []]
    (conj relu-result {:output output :der der})))

(defn sigmoid []
  (let [output (fn [x]
                 (/ 1 (+ 1 (js/Math.exp (- x)))))
        der (fn [x]
              (let [output (output x)]
                (* output (- 1 output))))
        sigmoid-result []]
    (conj sigmoid-result {:output output :der der})))

(defn linear []
  (let [output (fn [x]
                 x)
        der 1
        linear-result []]
    (conj linear-result {:output output :der der})))

(defn l1 []
  (let [output (fn [w]
                 (js/Math.abs w))
        der (fn [w]
              (if (< w 0)
                -1
                (if (> w 0)
                  1
                  0)))
        l1-result []]
    (conj l1-result {:output output :der der})))

(defn l2 []
  (let [output (fn [w]
                 (* 0.5 w w))
        der (fn [w]
              w)
        l2-result []]
    (conj l2-result {:output output :der der})))

 (defn build-network [network-shape activation output-activation regularization input-ids init-zero]
  (let [num-layers (count network-shape)
        id (atom 1)
        network []]
    (loop [layer-idx 0]
      (let [is-output-layer (= layer-idx (- num-layers 1))
            is-input-layer (= layer-idx 0)
            current-layer (atom node)
            network (conj network current-layer)
            num-nodes (get network-shape layer-idx)]
        (loop [i 0]
          (let [node-id (atom (str id))]
            (if is-input-layer
              (reset! node-id (get input-ids i))
              (swap! id inc))
            (let [node (node-constructor node-id (if is-output-layer output-activation activation) init-zero)]
              (swap! current-layer assoc node)
              (when (>= layer-idx 1)
                (loop [j 0]
                  (let [prev-node (get-in network [(- layer-idx 1) j])
                        link (link-constructor prev-node node regularization init-zero)]
                    (do
                      (assoc prev-node :outputs link)
                      (assoc node :input-links link))
                    ))))))))))

export function buildNetwork(
    networkShape: number[], activation: ActivationFunction,
    outputActivation: ActivationFunction,
    regularization: RegularizationFunction,
    inputIds: string[], initZero?: boolean): Node[][] {
  let numLayers = networkShape.length
  let id = 1;
  /** List of layers, with each layer being a list of nodes. */
  let network: Node[][] = [];
  for (let layerIdx = 0; layerIdx < numLayers; layerIdx++) {
    let isOutputLayer = layerIdx === numLayers - 1;
    let isInputLayer = layerIdx === 0
    let currentLayer: Node[] = [];
    network.push(currentLayer);
    let numNodes = networkShape[layerIdx];
    for (let i = 0; i < numNodes; i++) {
      if (isInputLayer) {
        nodeId = inputIds[i];
      } else {
        id++;
      }
      let node = new Node(nodeId,
          isOutputLayer ? outputActivation : activation, initZero);
      currentLayer.push(node);
      if (layerIdx >= 1) {
        // Add links from nodes in the previous layer to this node.
        for (let j = 0; j < network[layerIdx - 1].length; j++) {
          let prevNode = network[layerIdx - 1][j];
          let link = new Link(prevNode, node, regularization, initZero);
          prevNode.outputs.push(link);
          node.inputLinks.push(link);
        }
      }
    }
  }
  return network;
}