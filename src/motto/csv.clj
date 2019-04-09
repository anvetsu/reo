(ns motto.csv
  (:require [motto.util :as u])
  (:import [java.io Reader FileReader]
           [org.apache.commons.csv CSVFormat CSVRecord]))

(defn- cols-by-headers [records headers]
  (let [f (fn [^CSVRecord r]
            (map (fn [^String h] (.get r h)) headers))]
    (map f records)))

(defn- cols [records ^Integer n]
  (let [indices (range n)
        f (fn [^CSVRecord r]
            (map (fn [^Integer i] (.get r i)) indices))]
    (map f records)))

(defn parser []
  CSVFormat/RFC4180)

(defn with-auto-header [p]
  (.withFirstRecordAsHeader p))

(defn with-header [p headers]
  (.withHeader p headers))

(defn with-delim [p c]
  (.withDelimiter p c))

(defn rd
  ([csv-file ^CSVFormat parser config]
   (let [config (u/keyword-keys config)
         ^Reader in (FileReader. csv-file)]
     (try 
       (let [headers (seq (:headers config))
             coln (:numcols config)
             records (.parse parser in)
             hdr headers]
         [hdr
          (if headers
            (cols-by-headers records headers)
            (cols records coln))])
       (finally
         true))))
;         (.close in)))))
  ([csv-file ^CSVFormat parser]
   (rd csv-file parser nil))
  ([csv-file]
   (rd csv-file (parser) nil)))
