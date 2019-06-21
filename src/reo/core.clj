(ns reo.core
  (:require [clojure.tools.cli :as cli]
            [reo.flag :as f]
            [reo.repl :as r]
            [reo.dbconn :as dbconn]
            [reo.httpd :as httpd])
  (:gen-class))

(defn- shutdown []
  (httpd/stop)
  (when-not (dbconn/cleanup)
    (println "ERROR: db cleanup failed")))

(defn- setup-sig-handler! []
  (.addShutdownHook
   (Runtime/getRuntime)
   (Thread.
    (fn []
      (shutdown)))))

(def cli-opts [["-s" "--server" "HTTP server mode, expose the `POST /eval` API"]
               ["-p" "--port PORT" "HTTP server port"
                :default 3030
                :parse-fn #(Integer/parseInt %)
                :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
               ["-r" "--repl FLAG" "start the repl. FLAG must be either true or false"
                :default true
                :parse-fn #(Boolean/valueOf %)]
               ["-V" "--verbose" "verbose output"]
               ["-v" "--version" "show version"]
               ["-h" "--help" "print this help"]])

(defn- show-version! [exit?]
  (println "reo v0.0.1")
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
  (let [p-opts (cli/parse-opts args cli-opts)
        opts (:options p-opts)]
    (cond
      (:version opts) (show-version! true)
      (:help opts) (show-help! p-opts)
      (:verbose opts) (f/verbose!))
    (when (:server opts)
      (httpd/start (:port opts)))
    (when (:repl opts)
      (repl))))
