(ns motto.lib.arith)

(defn- seq-arith [opr x y]
  (loop [x x, y y, r []]
    (if (and (seq x) (seq y))
      (recur (rest x) (rest y) (conj r (opr (first x) (first y))))
      r)))

(defn- seq-x-arith [opr x y]
  (loop [x x, r []]
    (if (seq x)
      (recur (rest x) (conj r (opr (first x) y)))
      r)))

(defn seq-y-arith [opr x y]
  (loop [y y, r []]
    (if (seq y)
      (recur (rest y) (conj r (opr (first y) x)))
      r)))

(defn- arith [opr x y]
  (cond
    (and (number? x) (number? y)) (opr x y)
    (and (seqable? x) (seqable? y)) (seq-arith opr x y)
    (seqable? x) (seq-x-arith opr x y)
    :else (seq-y-arith opr x y)))

(def add (partial arith +))
(def sub (partial arith -))
(def mul (partial arith *))
(def div (partial arith /))
