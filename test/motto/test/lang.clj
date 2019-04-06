(ns motto.test.lang
  (:use [clojure.test]
        [motto.test.util]))

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
   "100=[99 100 101]" [false true false]
   "big(2 [1 2 3 4])" [2 2 3 4]
   "sml(2 [1 2 3 4])" [1 2 2 2]
   "big(20 10)"       20
   "sml(20 10)"       10
   "big([20 9 30] 10)" [20 10 30]
   "sml([20 9 30] 10)" [10 9 10]
   "big([20 9 30] [10 20 30])" [20 20 30]
   "sml([20 9 30] [10 20 30])" [10 9 30]])

(deftest basic-cmpr
  (test-with cmrp-data))

(def ^:private logical-data
  ["1<2 & 3<4*100"  true
   "1>2 | 3<4*100"  true
   "a:10<20 & (1=2 | 1 < 2)" :void
   "a"              true
   "a:(10<10 & (1=2 | 1 < 2))" :void
   "a"              false
   "a:10"           :void
   "a<10 & (1=2 | 1 < 2)" false
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
  ["t:100"       :ex
   "f:200"       :ex
   "a:10"        :void
   "a + 2"       12
   "{a:100 b:4+a b}"   104
   "a+b"         :ex
   "b:4"         :void
   "a+b"         14
   "a-b"         6
   "a=b"         false
   "b=4"         true
   "(b=4)=(4=b)" true])

(deftest vars
  (test-with vars-data))

(def ^:private fns-data
  ["(fn (x) x*x)(10)"        100
   "(fn (x y) x*2+y)(10 20)" 40
   "a:fn(x) x*x"             :void
   "a(20)"                   400
   "a:fn(x) fn(y) x+y"       :void
   "b:a(10)"                 :void
   "b(20)"                   30
   "b(b(1))"                 21
   "g:fn(x) fn(y) x + y"     :void
   "g(10)(20)"               30
   "g:fn(x) fn(y) fn (z) x + y + z" :void
   "g(10)(20)(30)"           60])

(deftest fns
  (test-with fns-data))

(def ^:private blck-data
  ["{1+2 3+4 5+4}"    9
   "a:10"             :void
   "pyth:fn(x y) { a:x*x b:y*y a+b }" :void
   "pyth(3 4)"        25
   "a"                10
   "{a:100 b:200 a+b}" 300
   "a"                10
   "{a:100 b:200 a+b c:3}" :ex])

(deftest blck
  (test-with blck-data))

(def ^:private op-data
  ["(+)(1 2)"     3
   "(<=)(1 1)"    true])

(deftest op
  (test-with op-data))

(def ^:private cond-data
  ["if 1 > 2 200+300" false
   "if {1 > 2 200+300 \"ok\"}" "ok"
   "if {1 < 2 200+300 400}" 500
   "a:100" :void
   "if {a < 50 1 a < 90 2 a < 100 3 4}" 4
   "a:10" :void
   "if {a < 50 1 a < 90 2 a < 100 3 4}" 1
   "a:60" :void
   "if {a < 50 1 a < 90 2 a < 100 3 4}" 2])

(deftest condtest
  (test-with cond-data))
