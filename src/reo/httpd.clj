(ns reo.httpd
  (:use [compojure.route :only [files not-found]]
        [compojure.core :only [routes POST]]
        ring.middleware.params
        org.httpkit.server)
  (:require [clojure.string :as str]
            [clojure.walk :as w]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [reo.flag :as flag]
            [reo.global-env :as env]
            [reo.eval-native :as e]
            [reo.compile :as c]
            [reo.lib.dt :as dt]))

(defn- evaluate [s]
  (let [eval (env/make-eval)
        exprs (c/compile-string s)]
    (e/evaluate-all exprs eval)))

(defn- request-obj [req]
  (json/read-str (String. (.bytes (:body req)))
                 :key-fn keyword))

(defn- sanitize-obj [x]
  (if (instance? java.util.Calendar x)
    (dt/sdt x)
    x))

(defn- sanitize [form]
  (w/prewalk sanitize-obj form))

(defn- resp-obj [obj]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/write-str {:value (sanitize obj)})})

(defn- handle-eval [req]
  (try
    (let [expr-str (:expr (request-obj req))
          val (evaluate expr-str)]
      (resp-obj val))
    (catch Exception ex
      (when (flag/verbose?)
        (.printStackTrace ex))
      (log/error ex)
      (resp-obj {:error (.getMessage ex)}))))

(defn- mkendpoints []
  [(POST "/eval" [] handle-eval)
   (files "/static/")
   (not-found "<p>Resource not found.</p>")])

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
