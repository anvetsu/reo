(ns motto.test.list
  (:use [clojure.test]
        [motto.test.util]))

(def ^:private cons-data
  ["1;[2 3]"     [1 2 3]
   "[2 3];1"     [2 3 1]
   "1 + 10;[2 3]" [11 3 4]
   "1 + 10;til(3)" [11 1 2 3]])

(deftest constest
  (test-with cons-data))

(def ^:private fold-data
  ["(+)@ [1 2 3 4 5]" 15
   "(+)@ 10;til(6)" 25
   "(*)@ 1+til(10)" 3628800
   "big@ [20 10 30 40]" 40
   "sml@ [20 10 30 40]" 10
   "sum ([[1 2 3] [4 5 6]])" [5 7 9]
   "dif ([[1 2 3] [4 5 6]])" [-3 -3 -3]
   "prd ([[1 2 3] [4 5 6]])" [4 10 18]
   "qt ([[1 2 3] [4 5 6]])" [1/4 2/5 1/2]
   "mx([20 40 10 5])" 40
   "mn([20 40 10 5])" 5])

(deftest foldtest
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
