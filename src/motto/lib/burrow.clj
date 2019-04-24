(ns motto.lib.burrow
  (:require [motto.util :as u]
            [motto.bitvec :as bv]))

(declare burrow)

(defn- seq-burrow [opr x y]
  (loop [x x, y y, r []]
    (if (and (seq x) (seq y))
      (recur (rest x) (rest y)
             (conj r (burrow opr (first x) (first y))))
      r)))

(defn- seq-x-burrow [opr x y]
  (loop [x x, r []]
    (if (seq x)
      (recur (rest x) (conj r (opr (first x) y)))
      r)))

(defn seq-y-burrow [opr x y]
  (loop [y y, r []]
    (if (seq y)
      (recur (rest y) (conj r (opr (first y) x)))
      r)))

(defn- burrow [opr x y]
  (when (or (nil? x) (nil? y))
    (throw (Exception. "invalid argument to operator")))
  (cond
    (and (string? x) (string? y)) (opr x y)
    (and (u/atomic? x) (u/atomic? y)) (opr x y)
    (and (seqable? x) (seqable? y)) (seq-burrow opr x y)
    (seqable? x) (seq-x-burrow opr x y)
    :else (seq-y-burrow opr x y)))

(defn- burrow->bitvec [opr x y]
  (let [r (burrow opr x y)]
    (if (seqable? r)
      (bv/from-seq r)
      r)))

(defn- not-eq [a b]
  (not (= a b)))

(defn- c< [x y]
  (< (compare x y) 0))

(defn- c> [x y]
  (> (compare x y) 0))

(defn- c>= [x y]
  (let [x (compare x y)]
    (or (> x 0) (= x 0))))

(defn- c<= [x y]
  (let [x (compare x y)]
    (or (< x 0) (= x 0))))

(def add   (partial burrow +))
(def sub   (partial burrow -))
(def mul   (partial burrow *))
(def div   (partial burrow /))
(def eq    (partial burrow->bitvec =))
(def neq   (partial burrow->bitvec not-eq))
(def lt    (partial burrow->bitvec c<))
(def gt    (partial burrow->bitvec c>))
(def lteq  (partial burrow->bitvec c<=))
(def gteq  (partial burrow->bitvec c>=))
(def big   (partial burrow max))
(def small (partial burrow min))
