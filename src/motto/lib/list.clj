(ns motto.lib.list
  (:require [motto.util :as u]
            [motto.lib.burrow :as b]))

(defn til [x]
  (into []
        (if (pos? x)
          (range 0 x)
          [])))

(defn- n-of [x n]
  (loop [n n, xs []]
    (if (<= n 0)
      xs
      (recur (dec n) (conj xs x)))))

(defn listf [f n]
  (loop [n n, i 0, rs []]
    (if (<= n 0)
      rs
      (recur (dec n) (inc i) (conj rs (f i))))))

(defn -take- [x n]
  (if (seqable? x)
    (let [[f n] (if (neg? n)
                  [take-last (- n)]
                  [take n])]
      (f n x))
    (n-of x n)))

(defn lift [n xs]
  (let [[f n] (if (neg? n)
                [take-last (- n)]
                [take n])]
    (into [] (f n xs))))

(defn dip [n xs]
  (let [[f n] (if (neg? n)
                [drop-last (- n)]
                [drop n])]
    (into [] (f n xs))))

(defn -conj- [x y]
  (if (seqable? y)
    (conj y x)
    (cons y x)))

(defn -fold- [ys f]
  (loop [xs (rest ys), r (first ys)]
    (if (seq xs)
      (recur (rest xs) (f r (first xs)))
      r)))

(defn -map- [xs f]
  (loop [xs xs, rs []]
    (if (seq xs)
      (recur (rest xs) (conj rs (f (first xs))))
      rs)))

(defn -filter- [xs f]
  (filter f xs))

(defn fold-incr [ys f]
  (loop [xs (rest ys), lv (first ys), r [lv]]
    (if (seq xs)
      (let [cv (f lv (first xs))]
        (recur (rest xs) cv (conj r cv)))
      r)))

(defn fold-times [x n f]
  (loop [n n, r x]
    (if (<= n 0)
      r
      (recur (dec n) (f r)))))

(defn sum [ys]
  (-fold- ys b/add))

(defn diff [ys]
  (-fold- ys b/sub))

(defn prd [ys]
  (-fold- ys b/mul))

(defn -quot- [ys]
  (-fold- ys b/div))

(defn -max- [ys]
  (apply max ys))

(defn -min- [ys]
  (apply min ys))

(defn sums [ys]
  (fold-incr ys b/add))

(defn diffs [ys]
  (fold-incr ys b/sub))

(defn prds [ys]
  (fold-incr ys b/mul))

(defn quots [ys]
  (fold-incr ys b/div))

(defn maxs [ys]
  (fold-incr ys max))

(defn mins [ys]
  (fold-incr ys min))

(defn twins [f xs]
  (loop [xs xs, rs [(first xs)]]
    (let [ys (rest xs)]
      (if (seq ys)
        (recur ys
               (conj rs (f (second xs) (first xs))))
        rs))))

(defn collect [f default xs]
  (loop [xs xs, rs {}]
    (if (seq xs)
      (let [x (first xs)
            v (f (get rs x default))]
        (recur (rest xs) (assoc rs x v)))
      (into {} rs))))

(defn collect-once [f xs]
  (loop [xs xs, rs {}]
    (if (seq xs)
      (let [x (first xs)]
        (if-let [v (get rs x)]
          (recur (rest xs) rs)
          (recur (rest xs) (assoc rs x (f x)))))
      (into {} rs))))

(defn count-for [f xs]
  (loop [xs xs, n 0]
    (if (seq xs)
      (if (f (first xs))
        (recur (rest xs) (inc n))
        (recur (rest xs) n))
      n)))

(defn count-eq [x xs]
  (count-for #(= x %) xs))

(defn counts [xs]
  (collect inc 0 xs))

(defn zip [xs ys]
  (map vector xs ys))

(defn pairs [xs ys]
  (apply hash-map (apply concat (zip xs ys))))

(defn index-of [xs x]
  (.indexOf xs x))

(defn truths [xs]
  (filter identity xs))

(defn dim [xs]
  (let [cols (count xs)]
    (if (and (> cols 0)
             (= (type xs) (type (first xs))))
      [cols (dim (first xs))]
      cols)))

(defn sel [xs ys]
  (loop [xs xs, ys ys, rs []]
    (if (and (seq xs) (seq ys))
      (if (first xs)
        (recur (rest xs) (rest ys)
               (conj rs (first ys)))
        (recur (rest xs) (rest ys) rs))
      rs)))

(defn in? [xs x]
  (if (some #{x} xs)
    true
    false))
