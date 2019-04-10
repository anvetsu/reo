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

(def ^:private csv-data
  ["emp:csv(\"test/data/emp.csv\")" :void
   "count(cols(emp))" 3
   "emp('Name)(0)" "Tom. G"
   "sum(map(parse, emp('Salary)))" 12623.76
   "cfg:dict('types ['string 'int 'double])" :void
   "emp:csv(\"test/data/emp.csv\" cfg)" :void
   "emp('Dept)(1)" 2
   "sum(emp('Salary))" 12623.76
   "cfg:dict('headers ['n 'd 's], 'delim \\:)" :void
   "emp:csv(\"test/data/emp2.csv\", cfg)" :void
   "cols(emp)" ['n 'd 's]
   "first((emp('d)(2)) = \"3\")" true])

(deftest csvtests
  (test-with csv-data))
