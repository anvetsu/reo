(ns motto.httpd
  (:use [compojure.route :only [files not-found]]
        [compojure.core :only [routes POST context]]
        ring.middleware.params
        org.httpkit.server)
  (:require [clojure.string :as str]
            [cheshire.core :as json]
            [motto.global-env :as env]
            [motto.eval-native :as e]
            [motto.compile :as c]))

(defn- evaluate [s]
  (let [eval (env/make-eval)
        exprs (c/compile-string s)]
    (e/evaluate-all exprs eval)))    

(defn- request-obj [req]
  (json/parse-string (String. (.bytes (:body req))) true))

(defn- handle-eval [req]
  (let [expr-str (:expr (request-obj req))
        val (evaluate expr-str)]
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (json/generate-string {:value val})}))

(defn- mkendpoints []
  (let [res "/eval"]
    [(POST res [] handle-eval)]))

(defn- make-routes []
  (apply routes (mkendpoints)))

(def ^:private server (atom nil))

(defn start
  ([port]
   (reset! server (run-server (make-routes) {:port port})))
  ([]
   (start 3030)))

(defn stop []
  (when @server
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))
