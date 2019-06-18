(ns motto.config)

(def ^:private prompt-str (atom "=> "))
(def ^:private prompt2-str (atom ".. "))

(defn prompt [] @prompt-str)

(defn prompt! [s]
  (reset! prompt-str s))

(defn prompt2 [] @prompt2-str)

(defn prompt2! [s]
  (reset! prompt2-str s))
