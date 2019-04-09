(ns motto.csv
  (:require [motto.util :as u]
            [motto.lib.tab :as tab])
  (:import [java.io File]
           [java.nio.charset Charset]
           [org.apache.commons.csv CSVFormat
            CSVParser CSVRecord]))

(defn- cols-by-headers [records headers]
  (let [f (fn [^CSVRecord r]
            (map (fn [^String h] (.get r h)) headers))]
    (map f records)))

(defn- cols [records ^Integer n]
  (let [indices (range n)
        f (fn [^CSVRecord r]
            (map (fn [^Integer i] (.get r i)) indices))]
    (map f records)))

(defn fmt []
  CSVFormat/RFC4180)

(defn with-auto-header [p]
  (.withFirstRecordAsHeader p))

(defn with-header
  ([p headers]
   (.withHeader p headers))
  ([p]
   (.withHeader p nil)))

(defn with-delim [p c]
  (.withDelimiter p c))

(defn- spread [col-cnt data]
  (loop [data data
         rows (into [] (repeat col-cnt []))]
    (if (seq data)
      (recur (rest data) (u/spread rows (first data)))
      rows)))

(defn rd
  ([csv-file ^CSVFormat fmt config]
   (let [config (u/keyword-keys config)
         ^File in (File. csv-file)]
     (let [headers (seq (:headers config))
           charset (Charset/forName (or (:charset config) "UTF-8"))
           ^CSVParser parser (CSVParser/parse in charset fmt)
           records (.getRecords parser)
           hdr (map symbol (or (seq headers) (keys (.getHeaderMap parser))))
           coln (or (:numcols config) (count hdr))
           spread-f (partial spread (count hdr))]
       (tab/tab
        hdr
        (spread-f
         (cols records coln))))))
  ([csv-file ^CSVFormat fmt]
   (rd csv-file fmt nil))
  ([csv-file]
   (rd csv-file (fmt) nil)))
