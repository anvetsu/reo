(ns motto.lib.func)

(defn fork [f g h]
  (fn [x]
    (g (f x) (h x))))

(defn times [n f]
  (fn [x]
    (loop [n n, r x]
      (if (pos? n)
        (recur (dec n) (f r))
        r))))
