(ns motto.lib.func)

(defn fork [f g h]
  (fn [x]
    (g (f x) (h x))))
