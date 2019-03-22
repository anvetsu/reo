(ns motto.eval
  (:require [motto.env :as env]
            [motto.tokens :as t]
            [motto.parse :as p]))

(defn- ex [s]
  (throw (Exception. (str "eval: " s))))

(declare evaluate)

(def ^:private reserved-names #{'t 'f})

(defn- valid-ident! [var]
  (when (some #{var} reserved-names)
    (ex (str "reserved name: " var))))

(defn- amend [var val-expr env]
  (valid-ident! var)
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
    :call (apply-fn (first args) (second args) env)
    :list (eval-map (first args) env)
    (apply-fn ident args env)))

(defn- force-lookup [env expr]
  (or (env/lookup env expr)
      (ex (str "binding not found: " expr))))

(defn evaluate [expr env]
  (cond
    (= expr :true) [true env]
    (= expr :false) [false env]
    (p/literal? expr) [expr env]
    (p/identifier? expr) [(force-lookup env expr) env]
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
