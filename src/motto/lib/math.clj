(ns motto.lib.math)

(defn pow [x y & ys]
  (let [r (Math/pow x y)
        rs (seq (map #(Math/pow x %) ys))]
    (if (seq rs)
      (conj rs r)
      r)))

(defn ceil [x]
  (if (seqable? x)
    (map #(Math/ceil %) x)
    (Math/ceil x)))

(defn floor [x]
  (if (seqable? x)
    (map #(Math/floor %) x)
    (Math/floor x)))
