(ns motto.csv
  (:require [motto.util :as u]
            [motto.lib.tab :as tab])
  (:import [java.io File]
           [java.nio.charset Charset]
           [java.text SimpleDateFormat]
           [org.apache.commons.csv CSVFormat
            CSVParser CSVRecord]))

(defn- conv [v t]
  (if (string? t)
    (.parse (SimpleDateFormat. t) v)
    (case t
      :int (Integer/parseInt v)
      :float (Float/parseFloat v)
      :double (Double/parseDouble v)
      :number (read-string v)
      v)))

(defn- cols [records ^Integer n types]
  (let [indices (range n)
        f (fn [^CSVRecord r]
            (map (fn [^Integer i]
                   (let [v (.get r i)
                         t (types i)]
                     (if t (conv v t) v)))
                 indices))]
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

(defn- process-types [types coln]
  (if (seq types)
    (loop [types types, rs []]
      (if (seq types)
        (let [t (first types)]
          (recur
           (rest types)
           (conj rs
                 (if (string? t)
                   t
                   (keyword t)))))
        rs))
    (into [] (repeat coln nil))))

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
           types (process-types (:types config) coln)]
       [hdr (cols records coln types)])))
  ([csv-file ^CSVFormat fmt]
   (rd csv-file fmt nil))
  ([csv-file]
   (rd csv-file (fmt) nil)))

(defn csv
  ([filename config]
   (let [config (u/keyword-keys config)
         fmt1 CSVFormat/RFC4180
         fmt2 (if (contains? config :auto_header)
                (if (:auto_header config)
                  (with-auto-header fmt1)
                  fmt1)
                (with-auto-header fmt1))
         fmt3 (if-let [delim (:delim config)]
                (with-delim fmt2 delim)
                fmt2)
         [hdr data] (rd filename fmt3 config)]
     (tab/tab hdr (spread (count hdr) data))))
  ([filename]
   (csv filename nil)))
