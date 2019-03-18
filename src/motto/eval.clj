(ns motto.eval
  (:require [motto.env :as env]
            [motto.tokens :as t]
            [motto.parse :as p]))

(defn- ex [s]
  (throw (Exception. (str "eval: " s))))

(declare evaluate)

(defn- amend [var val-expr env]
  (let [val (evaluate val-expr env)]
    [val (env/amend env var val)]))

(defn- eval-form [ident args env]
  (case ident
    :define (amend (first args) (second args) env)))

(defn evaluate [expr env]
  (cond
    (p/literal? expr) [expr env]
    (p/identifier? expr) [(env/lookup env expr) env]
    (seq expr) (eval-form (first expr) (rest expr) env)))

(defn compile-string [s]
  (let [tokens (t/tokens s)]
    (loop [[expr tokens] (p/parse-expr tokens)]
      (when-not expr
        (ex (str "compilation failed: " s)))
      (if (seq tokens)
        (recur (p/parse-expr tokens))
        expr))))
