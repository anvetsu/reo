(ns motto.eval
  (:require [motto.env :as env]))

(defn- literal [x]
  (or (number? x)
      (string? x)
      (boolean? x)))

(declare evaluate)

(defn- amend [var val-expr env]
  (let [val (evaluate val-expr env)]
    [val (env/amend env var val)]))

(defn- eval-form [ident args env]
  (case ident
    :define (amend (first args) (second args) env)))

(defn evaluate [expr env]
  (cond
    (literal? expr) [expr env]
    (symbol? expr) [(env/lookup env expr) env]
    (seq expr) (eval-form (first expr) (rest expr) env)))
