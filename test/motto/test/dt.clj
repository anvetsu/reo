(ns motto.test.dt
  (:use [clojure.test]
        [motto.test.util]))

(def ^:private basic-data
  ["n:dt(\"2019-03-09T12:30:44\")" :void
   "dt_get(n 'M)" 3
   "dt_get(dt_add(n 's 10) 's)" 54
   "dt_get(dt_add(n 'H 2), 'H)" 14
   "dt_get(dt_add(n 'm 10), 'm)" 40
   "dt_get(dt_add(n 'y 2), 'y)" 2021
   "n2:dt_add(n 'M 10)" :void
   "dt_get(n2 'y)" 2020
   "dt_get(n2 'M)" 1
   "n2:dt(\"2020-03-09T12:30:44\")" :void
   "n < n2" true
   "dt_add(n 'y 1) = n2" true])

(deftest basic
  (test-with basic-data))
