(ns motto.lib.json
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [motto.tab :as tab]
            [motto.util :as u]))

(defn json-str [m]
  (json/write-str m))

(defn str->json [s]
  (json/read-str s :key-fn symbol))

(defn- read-all-json [reader]
  (loop [accum []]
    (if-let [record (json/read reader :eof-error? false)]
      (recur (conj accum record))
      accum)))

(defn parse [filename]
  (let [rs (with-open
             [r (u/string->reader (slurp filename))]
             (read-all-json r))]
    (tab/mkt rs)))
