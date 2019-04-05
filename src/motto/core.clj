(ns motto.core
  (:require [motto.repl :as r]
            [motto.dbconn :as dbconn])
  (:gen-class))

(defn- shutdown []
  (when (dbconn/cleanup)
    (println "db cleanup - OK")))

(defn- setup-sig-handler! []
  (.addShutdownHook
   (Runtime/getRuntime)
   (Thread.
    (fn []
      (shutdown)))))

(defn -main [& args]
  (setup-sig-handler!)
  (println "motto v0.0.1")
  (r/repl))
