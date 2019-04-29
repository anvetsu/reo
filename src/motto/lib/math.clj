(ns motto.lib.math)

(defn pow [x y & ys]
  (let [r (Math/pow x y)
        rs (seq (map #(Math/pow x %) ys))]
    (if (seq rs)
      (conj rs r)
      r)))
