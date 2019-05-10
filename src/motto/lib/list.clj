(ns motto.lib.list
  (:require [motto.util :as u]
            [motto.bitvec :as bv]
            [motto.lib.burrow :as b]))

(defn til [x]
  (into []
        (if (pos? x)
          (range 0 x)
          [])))

(defn listf
  ([f n i]
   (if (>= i n)
     []
     (lazy-seq (cons (f i) (listf f n (inc i))))))
  ([f n] (listf f n 0)))

(defn lift [n xs]
  (let [[f n] (if (neg? n)
                [take-last (- n)]
                [take n])]
    (f n xs)))

(defn dip [n xs]
  (let [[f n] (if (neg? n)
                [drop-last (- n)]
                [drop n])]
    (f n xs)))

(defn -conj- [x y]
  (if (seqable? y)
    (conj (vec y) x)
    (cons y (vec x))))

(defn -fold- [ys f]
  (reduce f (first ys) (rest ys)))

(defn fold-incr [ys f]
  (reverse
   (reduce
    #(conj %1 (f (first %1) %2))
    (list (first ys))
    (rest ys))))

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

(defn zip [xs ys & yss]
  (apply map vector xs ys yss))

(defn pairs [xs ys]
  (apply hash-map (apply concat (zip xs ys))))

(defn index-of [xs x]
  (.indexOf xs x))

(defn truths [xs]
  (filter identity xs))

(defn dim [xs]
  (if (seqable? xs)
    (let [cols (count xs)]
      (if (and (> cols 0)
               (seqable? (first xs)))
        (let [d (dim (first xs))]
          (if (int? d)
            (conj [cols] d)
            (concat [cols] d)))
        [cols]))
    []))

(defn- bitvec->seq [obj]
  (if (bv/bitvec? obj)
    (bv/to-seq obj)
    obj))

(defn sel [xs ys]
  (loop [xs (bitvec->seq xs), ys ys, rs []]
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

(defn inf [x]
  (lazy-seq (cons x (inf x))))

(defn take-repeat
  ([n xs orig-xs into-vec?]
   (loop [ys xs, n n, rs []]
     (let [ts (take n ys)
           c (count ts)]
       (if (< c n)
         (recur orig-xs (- n c) (conj rs ts))
         [(let [xx (apply concat (conj rs ts))]
            (if into-vec?
              (into [] xx)
              xx))
          (nthrest ys n)]))))
  ([n xs orig-xs]
   (take-repeat n xs orig-xs true)))

(defn -take-repeat- [n xs]
  (let [xs (if (seqable? xs) xs [xs])]
    (first (take-repeat n xs xs))))

(defn- dig-thru [xs dims]
  (loop [xs xs, dims dims]
    (if (seq dims)
      (recur (get xs (first dims))
             (rest dims))
      xs)))

(defn dig [xs dims]
  (if (every? int? dims)
    (dig-thru xs dims)
    (let [fdim (first dims)
          rdims (seq (rest dims))]
      (loop [fdim fdim, rs []]
        (if (seq fdim)
          (let [ys (get xs (first fdim))]
            (recur (rest fdim)
                   (if rdims
                     (concat rs [(dig ys rdims)])
                     (conj rs ys))))
          rs)))))

(defn without-iter [xs ys]
  (loop [xs xs, rs ys]
    (if (seq xs)
      (recur (rest xs)
             (filter #(not (= (first xs) %)) rs))
      rs)))

(defn without [x xs]
  (let [d (seq (dim x))]
    (cond
      (not d) (filter #(not (= x %)) xs)
      (= (count d) 1) (without-iter x xs)
      :else (without-iter (flatten x) xs))))

(defn -concat- [a b]
  (if (string? b)
    (str b a)
    (concat (u/in-seq b) (u/in-seq a))))

(defn each-previous [f orig-xs]
  (loop [xs (rest orig-xs), x (first orig-xs), rs []]
    (if (seq xs)
      (let [y (first xs)]
        (recur (rest xs) y (conj rs (f y x))))
      rs)))

(defn -map- [xs f]
  (map f xs))
