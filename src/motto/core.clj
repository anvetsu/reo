(ns motto.core
  (:require [clojure.string :as s]
            [motto.eval :as e])
  (:gen-class))

(defn -main [& args]
  (println "motto v0.0.1")
  (println (e/compile-string (s/join " " args))))
