(ns motto.repl
  (:require [clojure.string :as str]
            [motto.env :as env]
            [motto.eval :as e]
            [motto.expr-io :as eio]))

(defn- readln []
  (loop [[flag s] (eio/readln)
         ss []]
    (case flag
      :more (do (print "- ")
                (flush)
                (recur (eio/readln)
                       (conj ss s)))
      :done (str/join (conj ss s)))))

(defn repl []
  (loop [env (env/global)]
    (print "> ") (flush)
    (recur
     (try
       (let [s (readln)
             exprs (e/compile-string s)
             [val env] (e/evaluate-all exprs env)]
         (eio/writeln val)
         env)
       (catch Exception ex
         (println (str "ERROR: " (.getMessage ex)))
         (.printStackTrace ex)
         env)))))
