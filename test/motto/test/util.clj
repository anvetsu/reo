(ns motto.test.util
  (:require [motto.global-env :as env]
            [motto.compile :as c]
            [motto.eval-native :as e])
  (:use [clojure.test]))

(defn comp-eval [s]
  (try
    (e/evaluate-all
     (c/compile-string s)
     (env/make-eval))
    (catch Exception ex
      {:ex ex})))

(defn test-with [data]
  (loop [ad data]
    (when (seq ad)
      (let [[k v] [(first ad) (second ad)]
            val (comp-eval k)
            val (if-let [ex (:ex val)]
                  (if-not (= v :ex)
                    (throw ex)
                    :ex)
                  val)]
        (is (= v val))
        (recur (rest (rest ad)))))))
