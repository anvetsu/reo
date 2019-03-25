;; Tranlsate motto to clojure and eval.
(ns motto.eval-bytes
  (:require [motto.tokens :as t]
            [motto.parse :as p]
            [motto.lib.list :as mll]
            [motto.lib.arith :as mla]))

(def + mla/add)
(def - mla/sub)
(def * mla/mul)
(def / mla/div)
(def <> mla/not-eq)
(def -get- get)
(def -neg- -)
(def til mll/til)

(defn- ex [s]
  (throw (Exception. (str "eval-bytes: " s))))

(declare evaluate)

(def ^:private reserved-names #{'t 'f})

(defn- valid-ident! [var]
  (when (some #{var} reserved-names)
    (ex (str "reserved name: " var))))

(defn- amend [var val-expr]
  (valid-ident! var)
  `(def ~var ~(evaluate val-expr)))

(defn- eval-form [ident args]
  (case ident
    :define (amend (first args) (second args))
    :call `(~(first args) ~@(second args))
    :list (first args)
    :and `(and ~@args)
    :or `(or ~@args)
    `(~ident ~@args)))

(defn expr->clj [expr]
  (cond
    (= expr :true) 'true
    (= expr :false) 'false
    (or (p/literal? expr)
        (p/identifier? expr)) expr
    (seq expr) (eval-form (first expr) (rest expr))))

(defn evaluate [expr]
  (let [e (expr->clj expr)]
    (eval e)))

(defn evaluate-all [exprs env]
  (loop [exprs exprs, val nil]
    (if (seq exprs)
      (let [val (evaluate (first exprs))]
        (recur (rest exprs) val))
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
