(ns motto.eval
  (:require [motto.env :as env]
            [motto.tokens :as t]
            [motto.parse :as p]))

(defn- ex [s]
  (throw (Exception. (str "eval: " s))))

(declare evaluate)

(defn- amend [var val-expr env]
  (let [[val env] (evaluate val-expr env)]
    [val (env/amend env var val)]))

(defn- eval-map [exprs env]
  (loop [exprs exprs, env env, vals []]
    (if (seq exprs)
      (let [[val env] (evaluate (first exprs) env)]
        (recur (rest exprs) env (conj vals val)))
      [vals env])))

(defn- apply-fn [fn args env]
  (let [[fnval env] (evaluate fn env)]
    (if fnval
      (let [[eargs env] (eval-map args env)]
        [(apply fnval eargs) env])
      (ex (str "function not found: " fn)))))

(defn- eval-form [ident args env]
  (case ident
    :define (amend (first args) (second args) env)
    (apply-fn ident args env)))

(defn evaluate [expr env]
  (cond
    (= expr :true) [true env]
    (= expr :false) [false env]
    (p/literal? expr) [expr env]
    (p/identifier? expr) [(env/lookup env expr) env]
    (seq expr) (eval-form (first expr) (rest expr) env)))

(defn evaluate-all [exprs env]
  (loop [exprs exprs, [val env] [nil env]]
    (if (seq exprs)
      (let [[val env] (evaluate (first exprs) env)]
        (recur (rest exprs) [val env]))
      [val env])))

(defn compile-string [s]
  (let [tokens (t/tokens s)]
    (loop [[expr tokens] (p/parse-expr tokens)
           exprs []]
      (when-not expr
        (ex (str "compilation failed: " s)))
      (if (seq tokens)
        (recur (p/parse-expr tokens) (conj exprs expr))
        (conj exprs expr)))))
