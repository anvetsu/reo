(ns motto.core
  (:require [motto.repl :as r])
  (:gen-class))

(defn -main [& args]
  (println "motto v0.0.1")
  (r/repl))
