(ns motto.lib.xls
  (:require [incanter.core :as ic]
            [incanter.excel :as ixls]))

(defn load-data [filename]
  (ixls/read-xls filename))
