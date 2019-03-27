(ns motto.repl
  (:require [clojure.string :as str]
            [motto.global-env :as env]
            [motto.eval :as e]
            [motto.compile :as c]
            [motto.expr-io :as eio]))

(defn- multiln-prompt []
  (print "- ")
  (flush))

(defn repl []
  (loop [env (env/make)]
    (print "> ") (flush)
    (recur
     (try
       (let [s (eio/read-multiln multiln-prompt)]
         (if s
           (let [exprs (c/compile-string s)
                 [val env] (e/evaluate-all exprs env)]
             (eio/writeln val)
             env)
           (System/exit 0)))
       (catch Exception ex
         (println (str "ERROR: " (.getMessage ex)))
         (.printStackTrace ex)
         env)))))
