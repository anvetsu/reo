(ns motto.repl
  (:require [clojure.string :as str]
            [motto.global-env :as env]
            [motto.eval :as e]
            [motto.compile :as c]
            [motto.expr-io :as eio]))

(defn- readln []
  (loop [[flag s] (eio/readln)
         ss []]
    (case flag
      :more (do (print "- ")
                (flush)
                (recur (eio/readln)
                       (conj ss s)))
      :done (str/join (conj ss s))
      :eof (System/exit 0))))
  
(defn repl []
  (loop [env (env/make)]
    (print "> ") (flush)
    (recur
     (try
       (let [s (readln)
             exprs (c/compile-string s)
             [val env] (e/evaluate-all exprs env)]
         (eio/writeln val)
         env)
       (catch Exception ex
         (println (str "ERROR: " (.getMessage ex)))
         (.printStackTrace ex)
         env)))))
