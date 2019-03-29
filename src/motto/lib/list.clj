(ns motto.lib.list)

(defn til [x]
  (into []
        (if (pos? x)
          (range 0 x)
          [])))

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

(defn -fold-incr- [ys f]
  (loop [xs (rest ys), lv (first ys), r [lv]]
    (if (seq xs)
      (let [cv (f lv (first xs))]
        (recur (rest xs) cv (conj r cv)))
      r)))
