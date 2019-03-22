(ns motto.test.lang
  (:require [motto.env :as env]
            [motto.eval :as e])
  (:use [clojure.test]))

(def ^:private env (env/global))

(defn- comp-eval [s env]
  (try
    (e/evaluate-all
     (e/compile-string s)
     env)
    (catch Exception ex
      [{:ex ex} env])))

(defn- test-with [data]
  (loop [ad data
         env env]
    (when (seq ad)
      (let [[k v] [(first ad) (second ad)]
            [val env] (comp-eval k env)
            val (if-let [ex (:ex val)]
                  (if-not (= v :ex)
                    (throw ex)
                    :ex)
                  val)]
        (is (= v val))
        (recur (rest (rest ad)) env)))))

(def ^:private arith-data
  ["1 + 2 - 3"        0
   "10 -3*100"        -290
   "(10-3) * 100"     700
   "-12-30"           -42
   "-12--30"          18
   "-(12-30)"         18
   "-(12-30)*100/2"   900
   "3.14 * 12 * .01"  0.3768])

(deftest basic-arith
  (test-with arith-data))

(def ^:private cmrp-data
  ["1=1"        true
   "1=1=true"   false
   "true=(1=1)" true
   "(1=1)=true" true
   "1=3"        false
   "3=1+2"      true])

(deftest basic-cmpr
  (test-with cmrp-data))

(def ^:private lists-data
  ["[1 2 3]"       [1 2 3]
   "[\"Price: \" \"$\" 10+20]" ["Price: " "$" 30]
   "til(5)"        [0 1 2 3 4]
   "2 * til(5)"    [0 2 4 6 8]
   "til(5) + [10 20 30 40 50]" [10 21 32 43 54]])

(deftest lists
  (test-with lists-data))

(def ^:private vars-data
  ["a"           :ex
   "a:10"        10
   "a + b"       :ex
   "a + 2"       12
   "b:4+a:100"   104
   "a+b"         204
   "(b:4)+a:100" 104
   "a-b"         96
   "a=b"         false
   "b=4"         true
   "(b=4)=(4=b)" true])

(deftest vars
  (test-with vars-data))
