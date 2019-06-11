(ns motto.lib.io
  (:require [clojure.java.io :as io])
  (:import [java.io InputStream OutputStream
            Reader Writer IOException File]))

(defn copy
  ([src dest opts]
   (let [bufsz (get opts 'bufsize)
         enc (get opts 'encoding)]
     (io/copy (io/input-stream src) (io/output-stream dest)
              :buffer-size bufsz :encoding enc)))
  ([src dest] (copy src dest nil)))

(defn delete-file [f]
  (.delete (File. f)))

(def input-stream io/input-stream)
(def output-stream io/output-stream)

(defmacro safe [expr success]
  `(try
     (let [r# ~expr
           s# ~success]
       (or s# r#))
     (catch IOException _#
       false)))

(defn close [x]
  (safe (.close x) true))

(defn read-byte [^InputStream in]
  (safe (.read in) nil))

(defn read-bytes [^InputStream in n]
  (let [bs (bytes (byte-array n))]
    (safe (.read in bs 0 n) nil)))

(defn reset [x] (safe (.reset x) true))
(defn skip [x n] (safe (.skip x n) nil))

(defn flush-out [^OutputStream out] (safe (.flush out) true))

(defn write-byte [^OutputStream out i]
  (safe (.write out i) true))

(defn write-bytes [^OutputStream out bs]
  (safe (.write bs 0 (count bs)) nil))

(def reader io/reader)
(def writer io/writer)

(defn write-str [^Writer w s]
  (safe (do (.write w s)
            (.flush w))
        (count s)))

(defn flush-writer [^Writer w] (safe (.flush w) true))

(defn with-open [f s]
  (with-open [s s]
    (f s)))
