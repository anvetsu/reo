(ns motto.global-env
  (:require [motto.compile]
            [motto.lib.burrow]
            [motto.lib.num]
            [motto.lib.list]))

(defn make-eval []
  (eval
   '(do
      (def -+- motto.lib.burrow/add)
      (def --- motto.lib.burrow/sub)
      (def -*- motto.lib.burrow/mul)
      (def -div- motto.lib.burrow/div)
      (def -=- motto.lib.burrow/eq)
      (def ->- motto.lib.burrow/gt)
      (def -<- motto.lib.burrow/lt)
      (def ->=- motto.lib.burrow/gteq)
      (def -<=- motto.lib.burrow/lteq)
      (def <> motto.lib.burrow/neq)

      (def -neg- -)
      (def big motto.lib.num/big)
      (def sml motto.lib.num/small)

      (def c motto.compile/compile-file)

      (def -take- motto.lib.list/-take-)
      (def -conj- motto.lib.list/-conj-)
      (def -fold- motto.lib.list/-fold-)
      (def -map- motto.lib.list/-map-)
      (def -fold-incr- motto.lib.list/fold-incr)
      (def sum motto.lib.list/sum)
      (def dif motto.lib.list/diff)
      (def prd motto.lib.list/prd)
      (def qt motto.lib.list/-quot-)
      (def mx motto.lib.list/-max-)
      (def mn motto.lib.list/-min-)
      (def til motto.lib.list/til)

      (fn [expr] (eval expr)))))
