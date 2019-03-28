(ns motto.repl
  (:require [clojure.string :as str]
            [motto.global-env :as env]
            [motto.eval-native :as e]
            [motto.compile :as c]
            [motto.expr-io :as eio]))

(defn- multiln-prompt []
  (print "- ")
  (flush))

(defn repl []
  (let [eval (env/make-eval)]
    (loop []
      (do (print "> ") (flush)
          (try
            (let [s (eio/read-multiln multiln-prompt)]
              (if s
                (let [exprs (c/compile-string s)
                      val (e/evaluate-all exprs eval)]
                  (eio/writeln val))
                (System/exit 0)))
            (catch Exception ex
              (println (str "ERROR: " (.getMessage ex)))))
          (recur)))))
