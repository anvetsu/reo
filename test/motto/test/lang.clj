(ns motto.test.lang
  (:require [motto.global-env :as env]
            [motto.compile :as c]
            [motto.eval :as e])
  (:use [clojure.test]))

(def ^:private env (env/make))

(defn- comp-eval [s env]
  (try
    (e/evaluate-all
     (c/compile-string s)
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
   "3.14 * 12 * .01"  0.3768
   "10*10+2"          102
   "10*(10+2)"        120
   "10+100/20"        15
   "10+100/20+4"      19
   "10+100+20/4"      115])

(deftest basic-arith
  (test-with arith-data))

(def ^:private cmrp-data
  ["1=1"     true
   "1=1=t"   :ex
   "t=(1=1)" true
   "(1=1)=t" true
   "(1=2)=f" true
   "f=f"     true
   "1=3"     false
   "3=1+2"   true
   "[1 2 3] < [3 4 5]" [true true true]
   "100=[99 100 101]" [false true false]])

(deftest basic-cmpr
  (test-with cmrp-data))

(def ^:private logical-data
  ["1<2 & 3<4*100"  true
   "1>2 | 3<4*100"  true
   "a:10<20 & (1=2 | 1 < 2)" true
   "a"              true
   "a:10<10 & (1=2 | 1 < 2)" false
   "a"              false
   "(a:10)<10 & (1=2 | 1 < 2)" false
   "a"              10])

(deftest basic-logical
  (test-with logical-data))

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
   "t:100"       :ex
   "f:200"       :ex
   "a:10"        10
   "a + b"       :ex
   "a + 2"       12
   "b:4+(a:100)"   104
   "a+b"           204
   "(b:4)+(a:100)" 104
   "a-b"         96
   "a=b"         false
   "b=4"         true
   "(b=4)=(4=b)" true])

(deftest vars
  (test-with vars-data))

(def ^:private access-data
  ["2#[1 2 3 4]"     3
   "a:[10 20 30]"    [10 20 30]
   "1#a + 100"       120
   "1#a - 100*20"    -1980])

(deftest access
  (test-with access-data))

(def ^:private fns-data
  ["(fn (x) x*x)(10)"        100
   "(fn (x y) x*2+y)(10 20)" 40
   "(a:fn(x) x*x)100"        100
   "a(20)"                   400
   "(a:fn(x) fn(y) x+y)1"    1
   "(b:a(10))1"              1
   "b(20)"                   30
   "b(b(1))"                 21])

(deftest fns
  (test-with fns-data))

(def ^:private blck-data
  ["{1+2 3+4 5+4}"    9
   "(pyth:fn(x y) { a:x*x b:y*y a+b })1" 1
   "pyth(3 4)"        25])

(deftest blck
  (test-with blck-data))

(def ^:private op-data
  ["(+)(1 2)"     3
   "(<=)(1 1)"    true])

(deftest op
  (test-with op-data))