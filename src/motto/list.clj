(ns motto.list
  (:require [motto.util :as u]
            [motto.bitvec :as bv]
            [motto.burrow :as b]))

(defn dict [x & xs]
  (if (seq xs)
    (apply hash-map x xs)
    (into {} x)))

(defn til
  ([n x step]
   (into []
         (cond
           (pos? x) (range n x step)
           (neg? x) (reverse (range n (Math/abs x) step))
           :else [])))
  ([n x] (til n x 1))
  ([x] (til 0 x)))

(defn listf
  ([f n]
   (let [r (f n)]
     (lazy-seq (cons n (listf f r)))))
  ([f]
   (lazy-seq (cons (f) (listf f)))))

(defn lift [n xs]
  (let [[f n] (if (neg? n)
                [take-last (- n)]
                [take n])]
    (vec (f n xs))))

(defn dip [n xs]
  (let [[f n] (if (neg? n)
                [drop-last (- n)]
                [drop n])]
    (vec (f n xs))))

(defn pack [maxwt wtfn items]
  (loop [bag [], maxwt maxwt, items (seq items)]
    (if items
      (let [i (first items)
            w (- maxwt (wtfn i))]
        (cond
          (pos? w) (recur (conj bag i) w (next items))
          (zero? w) [(conj bag i) (next items)]
          :else (recur bag maxwt (next items))))
      [bag items])))

(defn -conj- [x y]
  (if (seqable? x)
    (conj (vec x) y)
    (cons x (vec y))))

(defn -fold- [f ys]
  (reduce f (first ys) (rest ys)))

(defn fold-incr [f ys]
  (reverse
   (reduce
    #(conj %1 (f (first %1) %2))
    (list (first ys))
    (rest ys))))

(defn fold-times [f n x]
  (loop [n n, r x]
    (if (<= n 0)
      r
      (recur (dec n) (f r)))))

(defn sum [ys]
  (-fold- b/add ys))

(defn diff [ys]
  (-fold- b/sub ys))

(defn prd [ys]
  (-fold- b/mul ys))

(defn -quot- [ys]
  (-fold- b/div ys))

(defn -max- [ys]
  (apply max ys))

(defn -min- [ys]
  (apply min ys))

(defn sums [ys]
  (fold-incr b/add ys))

(defn diffs [ys]
  (fold-incr b/sub ys))

(defn prds [ys]
  (fold-incr b/mul ys))

(defn quots [ys]
  (fold-incr b/div ys))

(defn maxs [ys]
  (fold-incr max ys))

(defn mins [ys]
  (fold-incr min ys))

(defn twins [f xs]
  (loop [xs xs, rs [(first xs)]]
    (let [ys (rest xs)]
      (if (seq ys)
        (recur ys
               (conj rs (f (second xs) (first xs))))
        rs))))

(defn collect [f default xs]
  (loop [xs xs, i 0, rs {}]
    (if (seq xs)
      (let [x (first xs)
            v (f (get rs x default) i)]
        (recur (rest xs) (inc i) (assoc rs x v)))
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

(defn infs [x]
  (lazy-seq (cons x (infs x))))

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
  (if (string? a)
    (str a b)
    (concat (u/in-seq a) (u/in-seq b))))

(defn each-previous [f orig-xs]
  (loop [xs (rest orig-xs), x (first orig-xs), rs []]
    (if (seq xs)
      (let [y (first xs)]
        (recur (rest xs) y (conj rs (f y x))))
      rs)))

(defn factor
  ([xs options]
   (let [sort? (get options 'sort)
         levels (get options 'levels)]
     (loop [ss (cond
                 levels levels
                 sort? (apply sorted-set (sort xs))
                 :else (set xs))
            i 1
            bag {}]
       (if (seq ss)
         (recur (rest ss) (inc i)
                (assoc bag (first ss) i))
         [(into {} bag) (map #(get bag %) xs)]))))
  ([xs] (factor xs nil)))

(defn replc [xs x y]
  (let [p (if (seqable? x)
            #(if (some #{%} x) y %)
            #(if (= % x) y %))]
    (map p xs)))

(defn replcf [xs f y]
  (map #(if (f %) y %) xs))

(defn rests [xss]
  (loop [xss xss, rss []]
    (if (seq xss)
      (recur (rest xss) (conj rss (rest (first xss))))
      rss)))

(defn firsts [xss]
  (loop [xss xss, rss []]
    (if (seq xss)
      (recur (rest xss) (conj rss (first (first xss))))
      rss)))

(defn filter-by [f xs yss]
  (loop [xs xs
         yss yss
         rs (into [] (repeat (count yss) []))]
    (if (seq xs)
      (if (f (first xs))
        (recur (rest xs) (rests yss) (u/spread rs (firsts yss)))
        (recur (rest xs) (rests yss) rs))
      rs)))

(defn lazy [x f] (lazy-seq (cons x (f))))
