(ns motto.test.dt
  (:use [clojure.test]
        [motto.test.util]))

(deft basic-test
  ["n:dt(\"2019-03-09T12:30:44\")" 'n
   "dtget(n 'M)" 'MARCH
   "dtget(dtadd(n 's 10) 's)" 54
   "dtget(dtadd(n 'H 2), 'H)" 14
   "dtget(dtadd(n 'm 10), 'm)" 40
   "dtget(dtadd(n 'y 2), 'y)" 2021
   "n2:dtadd(n 'M 10)" 'n2
   "dtget(n2 'y)" 2020
   "dtget(n2 'M)" 'JANUARY
   "n2:dt(\"2020-03-09T12:30:44\")" 'n2
   "n < n2" true
   "dtadd(n 'y 1) = n2" true])
