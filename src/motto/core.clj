(ns motto.core
  (:require [clojure.string :as s]
            [motto.tokens :as t])
  (:gen-class))

(defn -main [& args]
  (println "motto v0.0.1")
  (println (t/tokens (s/join " " args))))
