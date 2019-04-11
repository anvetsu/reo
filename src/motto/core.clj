(ns motto.core
  (:require [clojure.tools.cli :as cli]
            [motto.flag :as f]
            [motto.repl :as r]
            [motto.dbconn :as dbconn]
            [motto.httpd :as httpd])
  (:gen-class))

(defn- shutdown []
  (httpd/stop)
  (when (dbconn/cleanup)
    (println "db cleanup - OK")))

(defn- setup-sig-handler! []
  (.addShutdownHook
   (Runtime/getRuntime)
   (Thread.
    (fn []
      (shutdown)))))

(def cli-opts [["-s" "--server" "HTTP server mode"]
               ["-p" "--port" "HTTP server port"
                 :default 3030
                :parse-fn #(Integer/parseInt %)
                :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
               ["-V" "--verbose" "verbose output"]
               ["-v" "--version" "show version"]
               ["-h" "--help" "print this help"]])

(defn- show-version! [exit?]
  (println "motto v0.0.1")
  (when exit?
    (System/exit 0)))

(defn- show-help! [opts]
  (println (:summary opts))
  (System/exit 0))

(defn- repl []
  (show-version! false)
  (r/repl))

(defn -main [& args]
  (setup-sig-handler!)
  (let [opts (:options (cli/parse-opts args cli-opts))]
    (cond
      (:version opts) (show-version! true)
      (:help opts) (show-help! opts)
      (:verbose opts) (f/verbose!))
    (if (:server opts)
      (httpd/start (:port opts))
      (repl))))
