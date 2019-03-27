(ns motto.repl
  (:require [clojure.string :as str]
            [motto.global-env :as genv]
            [motto.eval :as e]
            [motto.compile :as c]
            [motto.expr-io :as eio]))

(defn- multiln-prompt []
  (print "- ")
  (flush))

(defn ld [env ^String filename]
  (let [filename (if (.endsWith filename ".m.o")
                   filename
                   (str filename ".m.o"))
        exprss (read-string (slurp filename))]
    (loop [exprss exprss, env env, val val]
      (if (seq exprss)
        (let [[val env] (e/evaluate-all (first exprss) env)]
          (recur (rest exprss) env val))
        val))))

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
