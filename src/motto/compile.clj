(ns motto.compile
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [motto.eval :as e]
            [motto.parse :as p]
            [motto.tokens :as t]
            [motto.util :as u]
            [motto.expr-io :as eio]))

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

(defn compile-file [^String filename]
  (let [filename (if (.endsWith filename ".m")
                   filename
                   (str filename ".m"))
        ss
        (binding [*in* (io/reader filename)]
          (loop [ss []]
            (if-let [s (eio/read-multiln)]
              (recur (conj ss s))
              ss)))]
    (binding [*out* (io/writer (str filename ".o"))]
      (println (compile-strings ss)))))
