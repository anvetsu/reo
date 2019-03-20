(ns motto.repl
  (:require [motto.env :as env]
            [motto.eval :as e]))

(defn repl []
  (loop [env (env/global)]
    (print "> ") (flush)
    (recur
     (try
       (let [s (read-line)
             exprs (e/compile-string s)
             [val env] (e/evaluate-all exprs env)]
         (println val)
         env)
       (catch Exception ex
         (println (str "ERROR: " (.getMessage ex)))
         (.printStackTrace ex))))))
