(ns motto.test.list
  (:use [clojure.test]
        [motto.test.util]))

(def ^:private take-data
  ["2#[1 2 3 4]"     [1 2]
   "a:[10 20 30]"    'a
   "a"               [10 20 30]
   "1#a + 100"       [110]
   "1#a - 100*20"    [-1990]
   "2#\"hello\""     [\h \e]
   "5#1"             [1 1 1 1 1]
   "take_repeat(30 [1 5 0 0])"
   [1 5 0 0 1 5 0 0 1 5 0 0 1 5 0 0 1 5 0 0 1 5 0 0 1 5 0 0 1 5]])

(deftest take-test
  (test-with take-data))

(def ^:private cons-data
  ["1;[2 3]"     [1 2 3]
   "[2 3];1"     [2 3 1]
   "1 + 10;[2 3]" [11 3 4]
   "1 + 10;til(3)" [11 1 2 3]])

(deftest cons-test
  (test-with cons-data))

(def ^:private fold-data
  ["(+)@ [1 2 3 4 5]" 15
   "(+)@ 10;til(6)" 25
   "(*)@ 1+til(10)" 3628800
   "(*)@ 2#1.4142135623730949" 1.9999999999999996
   "n:5"            'n
   "(*)@ n#10"      100000
   "big@ [20 10 30 40]" 40
   "sml@ [20 10 30 40]" 10
   "sum ([[1 2 3] [4 5 6]])" [5 7 9]
   "dif ([[1 2 3] [4 5 6]])" [-3 -3 -3]
   "prd ([[1 2 3] [4 5 6]])" [4 10 18]
   "qt ([[1 2 3] [4 5 6]])" [1/4 2/5 1/2]
   "mx([20 40 10 5])" 40
   "mn([20 40 10 5])" 5
   "sums (1+til(10))" [1 3 6 10 15 21 28 36 45 55]
   "prds (1+til(10))" [1 2 6 24 120 720 5040 40320 362880 3628800]
   "mxs ([20 10 40 30])" [20 20 40 40]
   "mns ([20 10 40 30])" [20 10 10 10]
   "(fn (x) x;sum(-2#x))@> 10 [1 1]" [1 1 2 3 5 8 13 21 34 55 89 144]
   "(fn (x) x*x)@> 5 3" 1853020188851841])

(deftest fold-test
  (test-with fold-data))

(def ^:private fold-incr-data
  ["(+)@~ [1 2 3 4 5]" [1 3 6 10 15]
   "(+)@~ 10;til(6)" [10 10 11 13 16 20 25]])

(deftest fold-incr
  (test-with fold-incr-data))

(def ^:private map-data
  ["(sq:fn(x) x*x) 10" 10
   "sq~ til(3)"        [0 1 4]])

(deftest maptest
  (test-with map-data))

(def ^:private filter-data
  ["x:fn(x) x < 10" 'x
   "x! [100 20 3 90 4 1]" [3 4 1]])

(deftest filtertest
  (test-with filter-data))

(def ^:private search-data
  ["xs:[15 40 63 18 27 40 33 29 40 88]" 'xs
   "where:[29 63 40 33 50]" 'where
   "(fn(i) pos(xs i))~where" [7 2 1 6 -1]])

(deftest search-test
  (test-with search-data))

(def ^:product dig-data
  ["xs:[[41 26 38] [14 87 42] [30 28 19] [65 40 55] [19 31 64] [45 82 74]]" 'xs
   "dig(xs [3 2])" [[55]]
   "dig(xs [[0 4 5] [0 2]])" [[41 38] [19 64] [45 74]]
   "dig(xs [[0 1 4 5] [0]])" [[41] [14] [19] [45]]])

(deftest dig-test
  (test-with dig-data))
