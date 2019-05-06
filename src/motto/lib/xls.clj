(ns motto.lib.xls
  (:require [incanter.core :as ic]
            [incanter.excel :as ixls]
            [motto.lib.tab :as tab]))

(defn- dset->tab [dset]
  (let [colnames (map symbol (:column-names dset))
        cols (:columns dset)]
    (tab/mktab colnames cols)))

(defn load-data [filename]
  (let [dset (ixls/read-xls filename)]
    (dset->tab dset)))
