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

(defn spread [xs xss]
  (loop [i 0, xs xs, xss xss]
    (if (seq xss)
      (let [x (xs i)]
        (recur (+ i 1)
               (assoc xs i (conj x (first xss)))
               (rest xss)))
      xs)))

(defn dupl [xs]
  (loop [xs xs, rs []]
    (if (seq xs)
      (let [x (first xs)]
        (recur (rest xs) (conj rs x x)))
      rs)))

(defn keyword-keys [m]
  (if (keyword? (first (keys m)))
    m
    (let [xs (map (fn [[k v]] [(keyword k) v]) m)]
      (into {} xs))))
