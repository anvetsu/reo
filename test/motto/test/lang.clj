(ns motto.test.lang
  (:require [motto.env :as env]
            [motto.eval :as e])
  (:use [clojure.test]))

(def ^:private env (env/global))

(defn- comp-eval [s env]
  (e/evaluate-all
   (e/compile-string s)
   env))

(def ^:private arith-data
  {"1 + 2 - 3"        0
   "10 -3*100"        -290
   "(10-3) * 100"     700
   "-12-30"           -42
   "-12--30"          18
   "-(12-30)"         18
   "-(12-30)*100/2"   900
   "3.14 * 12 * .01"  0.3768})

(deftest basic-arith
  (loop [ad arith-data
         env env]
    (when (seq ad)
      (let [[k v] (first ad)
            [val env] (comp-eval k env)]
        (is (= v val))
        (recur (rest ad) env)))))
