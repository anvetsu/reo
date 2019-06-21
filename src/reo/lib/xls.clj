(ns reo.lib.xls
  (:require [incanter.core :as ic]
            [incanter.excel :as ixls]
            [reo.tab :as tab]))

(defn read-xls
  ([filename options]
   (let [sheet (get options 'sheet 0)
         all-sheets? (get options 'all_sheets false)
         dset (ixls/read-xls filename :sheet sheet :all-sheets all-sheets?)]
     (tab/dset->t dset)))
  ([filename] (read-xls {})))
