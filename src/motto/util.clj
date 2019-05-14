(ns motto.util
  (:require [clojure.string :as str])
  (:import [java.io File]))

(defn ex [s]
  (throw (Exception. s)))

(defn file-exists? [file-path]
  (.exists (File. file-path)))

(defn normalize-file-path [^String file-path]
  (let [i (.lastIndexOf file-path ".")]
    (.substring file-path 0 i)))

(defn atomic? [x] (not (seqable? x)))

(defn str-atomic? [x]
  (or (string? x) (atomic? x)))

(defn for-data [data fnmap]
  (doseq [k (keys fnmap)]
    (when-let [v (get data k)]
      ((get fnmap k) v))))

(defn spread [xs xss]
  (loop [i 0, xs xs, xss xss]
    (if (seq xss)
      (let [x (get xs i)]
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

(defn safe-parse-int [s]
  (if (seq s)
    (try
      (Integer/parseInt s)
      (catch Exception _ 0))
    0))

(def ^:private f0 (float 0.0))

(defn safe-parse-float [s]
  (if (seq s)
    (try
      (Float/parseFloat s)
      (catch Exception _ f0))
    f0))

(defn safe-parse-double [s]
  (if (seq s)
    (try
      (Double/parseDouble s)
      (catch Exception _ 0.0))
    0.0))

(defn keys->type [m s->t]
  (into {} (map (fn [[k v]] [(s->t (name k)) v]) m)))

(defn keys->syms [m]
  (keys->type m symbol))

(defn keys->kws [m]
  (keys->type m keyword))

(defn in-seq [x]
  (if (seqable? x)
    x
    [x]))

(defn string->reader
  ([s] (string->reader s "UTF-8"))
  ([s encoding]
   (-> s
       (.getBytes encoding)
       (java.io.ByteArrayInputStream.)
       (java.io.InputStreamReader.))))

(defn normalized-sym [x]
  (let [n (name x)]
    (symbol (str/replace n #"[_ -]" "_"))))
