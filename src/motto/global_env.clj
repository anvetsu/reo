(ns motto.global-env
  (:require [motto.env :as env]
            [motto.compile :as c]
            [motto.lib.list :as mll]
            [motto.lib.burrow :as mlb]))

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
      (def -get- get)
      (def -neg- -)
      (def c motto.compile/compile-file)
      (def -conj- motto.lib.list/-conj-)
      (def -fold- motto.lib.list/-fold-)
      (def -map- motto.lib.list/-map-)
      (def til motto.lib.list/til)

      (fn [expr] (eval expr)))))
