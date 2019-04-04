(ns motto.test.tab
  (:use [clojure.test]
        [motto.test.util]))

(def ^:private basic-data
  ["a:tab(['a 'b 'c] [10 20 30])" :void
   "a('a)" 10
   "a:tab(['a 'b 'c] [[10 20 30] [100 200 300] [40 50 60 70]])" :void
   "a('b)(2)" 300])

(deftest basic
  (test-with basic-data))
