(ns reo.lib.func
  (:require [reo.util :as u]))

(defn fork [f g h]
  (fn [x & xs]
    (let [r1 (apply f x xs)
          r2 (apply h x xs)]
      (g r1 r2))))

(defn times [n f]
  (when-not (pos? n)
    (u/ex (str "not a poitive integer: " n)))
  (when-not (fn? f)
    (u/ex (str "not a function: " f)))
  (fn [x]
    (loop [n n, r x]
      (if (pos? n)
        (recur (dec n) (f r))
        r))))
