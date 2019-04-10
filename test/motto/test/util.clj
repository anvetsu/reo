(ns motto.test.util
  (:require [motto.global-env :as env]
            [motto.compile :as c]
            [motto.type :as tp]
            [motto.eval-native :as e])
  (:use [clojure.test]))

(defn comp-eval [s evl]
  (try
    (let [rs
          (e/evaluate-all
           (c/compile-string s)
           evl)]
      (if (or (and (seqable? rs) (some tp/err? rs))
              (tp/err? rs))
        {:ex rs}
        rs))
    (catch Exception ex
      {:ex ex})))

(defn test-with [data]
  (let [evl (env/make-eval)]
    (loop [ad data]
      (when (seq ad)
        (let [[k v] [(first ad) (second ad)]
              val (comp-eval k evl)
              val (if-let [ex (:ex val)]
                    (if-not (= v :ex)
                      (throw ex)
                      :ex)
                    val)]
          (is (= v val))
          (recur (rest (rest ad))))))))
