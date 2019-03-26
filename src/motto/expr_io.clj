(ns motto.expr-io
  (:require [clojure.string :as str]
            [motto.type :as tp]))

(defn writeln [x]
  (let [v  (cond
             (boolean? x) (if x 't 'f)
             (tp/function? x) '<fn>
             :else x)]
    (println v)))

(defn- match-curlies [s]
  (loop [ss (seq s), opn 0, cls 0]
    (if (seq ss)
      (let [c (first ss)
            [opn cls]
            (cond
              (= \{ c) [(inc opn) cls]
              (= \} c) [opn (inc cls)]
              :else [opn cls])]
        (recur (rest ss) opn cls))
      (- opn cls))))

(defn readln []
  (let [s (read-line)]
    (if (str/ends-with? s " ")
      [:more s]
      (let [c (match-curlies s)]
        (cond
          (<= c 0) [:done s]
          (pos? c) [:more s])))))
