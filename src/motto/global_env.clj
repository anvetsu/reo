(ns motto.global-env
  (:require [motto.env :as env]
            [motto.compile :as c]
            [motto.lib.list :as mll]
            [motto.lib.burrow :as mlb]))

(defn make []
  (let [bindings {'+ mlb/add '- mlb/sub
                  '* mlb/mul '/ mlb/div
                  '= mlb/eq '> mlb/gt
                  '< mlb/lt '>= mlb/gteq
                  '<= mlb/lteq '<> mlb/neq
                  '-get- get '-neg- - 'c c/compile-file
                  'til mll/til}]
    (env/make bindings)))

(defn make-eval []
  (let [s "(do
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
    (def til motto.lib.list/til)

    (fn [expr] (eval expr)))"]
    (eval (read-string s))))
