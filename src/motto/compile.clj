(ns motto.compile
  (:require [clojure.java.io :as io]
            [taoensso.nippy :as nippy]
            [motto.parse :as p]
            [motto.tokens :as t]
            [motto.util :as u]
            [motto.expr-io :as eio])
  (:import [java.io DataInputStream DataOutputStream]))

(defn- ex [s]
  (u/ex (str "compile: " s)))

(defn compile-string [s]
  (let [tokens (t/tokens s)]
    (loop [[expr tokens] (p/parse tokens)
           exprs []]
      (when-not expr
        (ex (str "compilation failed: " s)))
      (if (seq tokens)
        (recur (p/parse tokens) (conj exprs expr))
        (conj exprs expr)))))

(defn compile-strings [ss]
  (map compile-string ss))

(defn compile-file [^String file-path]
  (let [file-path (if-not (u/file-exists? file-path)
                   (str file-path ".m")
                   file-path)
        ss
        (binding [*in* (io/reader file-path)]
          (loop [ss []]
            (if-let [s (eio/read-multiln)]
              (recur (conj ss s))
              ss)))]
    (with-open [w (io/output-stream (str (u/normalize-file-path file-path) ".mo"))]
      (nippy/freeze-to-out! (DataOutputStream. w) (compile-strings ss)))))

(defn slurp-o [^String file-path]
  (with-open [r (io/input-stream file-path)]
    (nippy/thaw-from-in! (DataInputStream. r))))
