(ns motto.expr-io
  (:require [clojure.string :as str]
            [motto.type :as tp]))

(defn writeln [x]
  (let [v  (cond
             (boolean? x) (if x 't 'f)
             (tp/function? x) '<fn>
             :else x)]
    (println v)))

(defn readln []
  (let [s (str/trimr (read-line))]
    (if (str/ends-with? s ";")
      [:more s]
      [:done s])))
