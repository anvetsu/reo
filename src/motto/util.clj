(ns motto.util
  (:import [java.io File]))

(defn ex [s]
  (throw (Exception. s)))

(defn file-exists? [file-path]
  (.exists (File. file-path)))

(defn normalize-file-path [^String file-path]
  (let [i (.lastIndexOf file-path ".")]
    (.substring file-path 0 i)))

(defn atomic? [x] (not (seqable? x)))

(defn for-data [data fnmap]
  (doseq [k (keys fnmap)]
    (when-let [v (get data k)]
      ((get fnmap k) v))))
