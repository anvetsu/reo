(ns motto.lib.burrow)

(defn- seq-burrow [opr x y]
  (loop [x x, y y, r []]
    (if (and (seq x) (seq y))
      (recur (rest x) (rest y) (conj r (opr (first x) (first y))))
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
  (cond
    (and (number? x) (number? y)) (opr x y)
    (and (seqable? x) (seqable? y)) (seq-burrow opr x y)
    (seqable? x) (seq-x-burrow opr x y)
    :else (seq-y-burrow opr x y)))

(defn- not-eq [a b]
  (not (= a b)))

(def add (partial burrow +))
(def sub (partial burrow -))
(def mul (partial burrow *))
(def div (partial burrow /))
(def eq  (partial burrow =))
(def neq (partial burrow not-eq))
(def lt  (partial burrow <))
(def gt  (partial burrow >))
(def lteq (partial burrow <=))
(def gteq (partial burrow >=))
