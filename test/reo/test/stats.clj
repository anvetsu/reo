(ns reo.test.stats
  (:use [clojure.test]
        [reo.test.util]))

(deft summary-test
  ["patientID:[1, 2, 3, 4] age:[25, 34, 28, 52]" 'age
   "diabetes:[\"Type1\", \"Type2\", \"Type1\", \"Type1\"] status:[\"Poor\", \"Improved\", \"Excellent\", \"Poor\"]" 'status
   "ss:factor(status ['sort:1b]) ss" [{"Excellent" 1 "Improved" 2 "Poor" 3} [3 2 1 3]]
   "ds:factor(diabetes) ds" [{"Type1" 1 "Type2" 2} [1 2 1 1]]
   "pdata:['patientID 'age 'diabetes 'status]$[patientID age ds(1) ss(1)]" 'pdata
   "s:tab(summary(pdata))" 's
   "s('col)" ['patientID 'age 'diabetes 'status]
   "s('min)" [1 25 1 1]
   "s('max)" [4 52 2 3]
   "s('mean)" [2.5 34.75 1.25 2.25]
   "s('median)" [2.5 31.0 1.0 2.5]
   "s('is_numeric)" [true true true true]
   "xs:[10 78 23 15 100 12]" 'xs
   "summary(xs)" [{'col 'NA 'min 10 'max 100 'mean 39.666666666666664 'median 19.0 'is_numeric true}]
   "summary(['a]$[xs])" [{'col 'a 'min 10 'max 100 'mean 39.666666666666664 'median 19.0 'is_numeric true}]])
