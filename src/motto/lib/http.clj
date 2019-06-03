(ns motto.lib.http
  (:require [org.httpkit.client :as http]
            [motto.util :as u]))

(defn http-get
  ([url options handler]
   (http/get url (u/keys->kws options) handler))
  ([url options]
   (http-get url options nil))
  ([url]
   (http-get url nil nil)))

(defn http-res
  ([p timeout-ms timeout-val]
   (u/keys->syms (deref p timeout-ms timeout-val)))
  ([p timeout-ms]
   (http-res p timeout-ms :void))
  ([p]
   (u/keys->syms @p)))

(defn http-req [options]
  (let [opts (u/keys->kws options)
        method (keyword (get opts :method 'get))]
    (http/request (assoc opts :method method))))
