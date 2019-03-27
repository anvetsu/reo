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

(defn readln []
  (loop [[flag s] (eio/readln)
         ss []]
    (case flag
      :more (recur (eio/readln)
                   (conj ss s))
      :done (str/join (conj ss s))
      :eof nil)))

(defn compile-file [filename]
  (let [ss
        (binding [*in* (io/reader filename)]
          (loop [ss []]
            (if-let [s (readln)]
              (recur (conj ss s))
              ss)))]
    (binding [*out* (io/writer (str filename ".o"))]
      (println (compile-strings ss)))))
