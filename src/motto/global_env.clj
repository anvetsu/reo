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
