(ns motto.lib.json
  (:require [cheshire.core :as json]))

(defn json-str [m]
  (json/generate-string m))

(defn- kw->sym [x]
  (if (keyword? x)
    (symbol (name x))
    x))

(defn parse [s]
  (json/parse-string s kw->sym))
