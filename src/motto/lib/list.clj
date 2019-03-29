(ns motto.lib.list)

(defn til [x]
  (into []
        (if (pos? x)
          (range 0 x)
          [])))

(defn -conj- [x y]
  (if (seqable? x)
    (conj x y)
    (cons x y)))
