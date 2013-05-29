(ns orlando.core
  (:import [com.google.common.hash Hashing]))

(def m 100)

(def k 10)

(defn bloom-filter []
  (take m (repeat 0)))

(defn hash-function [seed]
  (fn [x]
    (-> seed
        Hashing/murmur3_32
        (.hashString x)
        .asInt
        (mod m))))

(defn hash-functions
  []
  (map hash-function (range k)))

(defn indices [x]
  ((apply juxt (hash-functions)) x))

(defn add
  [b-filter x]
  (reduce #(assoc %1 %2 1)
          (vec b-filter)
          (indices x)))

(defn bloom-contains?
  [b-filter x]
  (not-any? zero? (map #(nth b-filter %) (indices x))))
