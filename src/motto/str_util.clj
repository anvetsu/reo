(ns motto.str-util)

(defn implode [chars]
  (apply str chars))

(defn s->int [s]
  (Integer/parseInt s))

(defn s->float [s]
  (Float/parseFloat s))

(defn s->double [s]
  (Double/parseDouble s))
