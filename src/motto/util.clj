(ns motto.util
  (:import [java.io File]))

(defn ex [s]
  (throw (Exception. s)))

(defn file-exists? [filename]
  (.exists (File. filename)))

(defn normalize-filename [^String filename]
  (let [i (.lastIndexOf filename ".")]
    (.substring filename 0 i)))
