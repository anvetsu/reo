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
   "(+)@ 10;til(6)" 25])

(deftest foldtest
  (test-with fold-data))

(def ^:private map-data
  ["(sq:fn(x) x*x) 10" 10
   "sq~ til(3)"        [0 1 4]])

(deftest maptest
  (test-with map-data))
