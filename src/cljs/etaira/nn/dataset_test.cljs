(ns etaira.nn.dataset-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [etaira.nn.dataset :refer [rand-uniform shuffle! normal-random dist regress-plane classify-two-gauss-data regress-gaussian 
                                       classify-spiral-data classify-circle-data classify-xor-data testo tr rt]]))

(deftest test-numbers
  (is (= 1 1)))

(deftest rand-uniform-test
  (println (rand-uniform 7 5))

  (testing "arguments are numbers"
    (println (rand-uniform 7 5))
    (is (= true (number? (rand-uniform 7 9)))))
  (testing "one of arguments is nil"
    (is (= true (number? (rand-uniform nil nil))))))

(deftest shuffle!-test
  (println (shuffle! [1 2 3 4 5 6]))
  (testing "is result a vector?"
    (is (= true (vector? (shuffle! ["nil" 7 4 8]))))))

(deftest normal-random-test
  (println (normal-random 3 7))
  (testing "is result a number"
    (is (= true (number? (normal-random)))))
  (testing "is result a number"
    (is (= true (number? (normal-random 3 9))))))

(deftest dist-test
  (println (dist {:x 2 :y 5} {:x 0 :y 9})))

(deftest regress-plane-test
  (println (regress-plane 4 9)))

(deftest classify-two-gauss-data-test
  (println (classify-two-gauss-data 12 9)))

(deftest regress-gaussian-test
  (println (regress-gaussian 7 2)))

(deftest classify-spiral-data-test
  (println (classify-spiral-data 6 3)))

(deftest classify-circle-data-test
  (println (classify-circle-data 4 1)))

(deftest classify-xor-data-test
  (println (classify-xor-data 4 8)))

(deftest testo-test
  (println (testo 1 (if true tr rt)))
  )
