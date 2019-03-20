(ns motto.lib.list)

(defn til [x]
  (into []
        (if (pos? x)
          (range 0 x)
          [])))
