(ns reo.lib.func)

(defn fork [f g h]
  (fn [x & xs]
    (let [r1 (apply f x xs)
          r2 (apply h x xs)]
      (g r1 r2))))

(defn times [n f]
  (fn [x]
    (loop [n n, r x]
      (if (pos? n)
        (recur (dec n) (f r))
        r))))
