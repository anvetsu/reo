(ns motto.lib.str
  (:require [clojure.string :as s]))

(def join s/join)

(defn split
  ([s pat]
   (s/split s pat))
  ([s]
   (split s #"")))
