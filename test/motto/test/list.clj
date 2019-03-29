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
