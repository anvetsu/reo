(ns motto.test.tab
  (:use [clojure.test]
        [motto.test.util]))

(def ^:private basic-data
  ["a:tab(['a 'b 'c] [10 20 30])" 'a
   "a('a)" 10
   "a:tab(['a 'b 'c] [[10 20 30] [100 200 300] [40 50 60 70]])" 'a
   "a('b)(2)" 300
   "a:tab([dict('a 10 'b 20 'c 30) dict('a 100 'b 200 'c 300)])" 'a
   "a('a)(1)" 100])

(deftest basic
  (test-with basic-data))

(def ^:private club-data
  ["a:tab(['a 'b] [[10 20] [30 40]])" 'a
   "b:tab(['a 'c] [[100 200] [300 400]])" 'b
   "c:club(a, b)" 'c
   "cols(c)" #{'a 'b 'c}
   "c('a)" [100 200]
   "c('c)" [300 400]
   "c('b)" [30 40]
   "c:club(b a)" 'c
   "cols(c)" #{'a 'b 'c}
   "c('a)" [10 20]
   "c('c)" [300 400]
   "c('b)" [30 40]])

(deftest club-test
  (test-with club-data))

(def ^:private csv-data
  ["emp:csv(\"test/data/emp.csv\")" 'emp
   "count(cols(emp))" 3
   "emp('Name)(0)" "Tom. G"
   "sum(map(parse, emp('Salary)))" 12623.76
   "cfg:dict('types ['s 'i 'd])" 'cfg
   "emp:csv(\"test/data/emp.csv\" cfg)" 'emp
   "emp('Dept)(1)" 2
   "sum(emp('Salary))" 12623.76
   "cfg:dict('headers ['n 'd 's], 'delim \\:)" 'cfg
   "emp:csv(\"test/data/emp2.csv\", cfg)" 'emp
   "cols(emp)" #{'n 'd 's}
   "emp('d)(2)" "3"])

(deftest csvtests
  (test-with csv-data))
