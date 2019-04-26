(ns motto.test.lang
  (:use [clojure.test]
        [motto.test.util]))

(deftest-with arith-test
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

(deftest-with cmrp-test
  ["1=1"     true
   "1=1=1b"   :ex
   "1b=(1=1)" true
   "(1=1)=1b" true
   "(1=2)=0b" true
   "0b=0b"     true
   "1=3"     false
   "3=1+2"   true
   "[1 2 3] < [3 4 5]" [true true true]
   "100=[99 100 101]" [false true false]
   "bools(bits(100=[99 100 101]) 3)" [false true false]
   "big(2 [1 2 3 4])" [2 2 3 4]
   "sml(2 [1 2 3 4])" [1 2 2 2]
   "big(20 10)"       20
   "sml(20 10)"       10
   "big([20 9 30] 10)" [20 10 30]
   "sml([20 9 30] 10)" [10 9 10]
   "big([20 9 30] [10 20 30])" [20 20 30]
   "sml([20 9 30] [10 20 30])" [10 9 30]])

(deftest-with logical-test
  ["1<2 & 3<4*100"  true
   "1>2 | 3<4*100"  true
   "a:10<20 & (1=2 | 1 < 2)" 'a
   "a"              true
   "a:(10<10 & (1=2 | 1 < 2))" 'a
   "a"              false
   "a:10"           'a
   "a<10 & (1=2 | 1 < 2)" false
   "a"              10])

(deftest-with lists-test
  ["[1 2 3]"       [1 2 3]
   "[\"Price: \" \"$\" 10+20]" ["Price: " "$" 30]
   "til(5)"        [0 1 2 3 4]
   "2 * til(5)"    [0 2 4 6 8]
   "til(5) + [10 20 30 40 50]" [10 21 32 43 54]
   "dim([10 20 30])" [3]
   "dim(102030)" []
   "dim(dim(102030))" [0]])

(deftest-with vars-test
  ["fn:100"       :ex
   "if:200"       :ex
   "a:10"        'a
   "a + 2"       12
   "{a:100 b:4+a b}"   104
   "a+b"         :ex
   "b:4"         'b
   "a+b"         14
   "a-b"         6
   "a=b"         false
   "b=4"         true
   "(b=4)=(4=b)" true
   "[a b c]:[10 20 a*b]" 'c
   "[a b c]" [10 20 200]
   "xs:[1 2 3]" 'xs
   "[a b c]:xs" 'c
   "a+b+c" 6])

(deftest-with fns-test
  ["(fn (x) x*x)(10)"        100
   "(fn (x y) x*2+y)(10 20)" 40
   "a:fn(x) x*x"             'a
   "a(20)"                   400
   "a:fn(x) fn(y) x+y"       'a
   "b:a(10)"                 'b
   "b(20)"                   30
   "b(b(1))"                 21
   "g:fn(x) fn(y) x + y"     'g
   "g(10)(20)"               30
   "g:fn(x) fn(y) fn (z) x + y + z" 'g
   "g(10)(20)(30)"           60
   "(fn^(x) if {x<=0 0 ^(x-1)})(100000)" 0])

(deftest-with blck-test
  ["{1+2 3+4 5+4}"    9
   "a:10"             'a
   "pyth:fn(x y) { a:x*x b:y*y a+b }" 'pyth
   "pyth(3 4)"        25
   "a"                10
   "{a:100 b:200 a+b}" 300
   "a"                10
   "{a:100 b:200 a+b c:3}" :ex])

(deftest-with op-test
  ["(+)(1 2)"     3
   "(<=)(1 1)"    true])

(deftest-with cond-test
  ["if 1 > 2 200+300" false
   "if {1 > 2 200+300 \"ok\"}" "ok"
   "if {1 < 2 200+300 400}" 500
   "a:100" 'a
   "if {a < 50 1 a < 90 2 a < 100 3 4}" 4
   "a:10" 'a
   "if {a < 50 1 a < 90 2 a < 100 3 4}" 1
   "a:60" 'a
   "if {a < 50 1 a < 90 2 a < 100 3 4}" 2])

(deftest-with burrow-test
  ["a:[[1 2 3] [4 5 6]]" 'a
   "b:[[10 100 100] [1 2 3]]" 'b
   "a*b" [[10 200 300] [4 10 18]]
   "price:[5.2 11.5 3.6 4 8.45]" 'price
   "qty:[2 1 3 6 2]" 'qty
   "costs:price * qty" 'costs
   "costs" [10.4 11.5 10.8 24 16.9]
   "vat:19.6" 'vat
   "price * vat / 100" [1.0192 2.254 0.7056 0.784 1.6562000000000001]
   "forecast:[[150 200 100 80 80 80][300 330 360 400 500 520][100 250 350 380 400 450][ 50 120 220 300 320 350]]" 'forecast
   "actual:[[141 188 111 87 82 74][321 306 352 403 497 507][118 283 397 424 411 409][ 43  91 187 306 318 363]]" 'actual
   "actual - forecast" [[-9 -12 11 7 2 -6] [21 -24 -8 3 -3 -13] [18 33 47 44 11 -41] [-7 -29 -33 6 -2 13]]])
