;; Tranlsate motto to clojure and eval.
(ns motto.eval-native
  (:require [motto.tokens :as t]
            [motto.parse :as p]
            [motto.compile :as c]
            [motto.util :as u]
            [motto.type :as tp]
            [motto.global-env :as env]))

(def ^:private idents (set '(+ - * / = > < >= <=)))

(defn- translate-ident [s]
  (if (= s '/)
    '-div-
    (if (some #{s} idents)
      (symbol (str "-" s "-"))
      s)))

(defn- ex [s]
  (u/ex (str "eval-native: " s)))

(declare ->lisp evaluate-all)

(defn- ld [^String filename env]
  (let [filename (if (.endsWith filename ".mo")
                   filename
                   (str filename ".mo"))]
    (when-not (u/file-exists? filename)
      (c/compile-file (u/normalize-filename filename)))
    (let [exprss (read-string (slurp filename))]
      (loop [exprss exprss, env env, val val]
        (if (seq exprss)
          (let [[val env] (evaluate-all (first exprss) env)]
            (recur (rest exprss) env val))
          [val env])))))

(def ^:private reserved-names #{'t 'f})

(defn- valid-ident [var]
  (when (some #{var} reserved-names)
    (ex (str "reserved name: " var)))
  var)

(defn all->lisp [exprs]
  (map ->lisp exprs))

(defn- call-fn [fn args]
  (let [fnval (->lisp fn)
        eargs (all->lisp args)]
    (concat (list fnval) eargs)))

(defn- form->lisp [ident args]
  (case ident
    :define `(do (def ~(valid-ident (first args)) ~(->lisp (second args))) ~(first args))
    :call (call-fn (first args) (second args))
    :list (vec (all->lisp (first args)))
    :and `(and ~@(all->lisp args))
    :or `(or ~@(all->lisp args))
    :block `(do ~@(all->lisp (first args)))
    :load `(ld ~@(first args))
    (call-fn ident args)))

(defn- mkfn [fexpr]
  (let [f (:fn fexpr)]
    `(fn ~(into [] (:params f)) ~(->lisp (:body f)))))

(defn ->lisp [expr]
  (cond
    (= expr :true) true
    (= expr :false) false
    (tp/literal? expr) expr
    (tp/identifier? expr) (translate-ident expr)
    (tp/function? expr) (mkfn expr)
    (seq expr) (form->lisp (first expr) (rest expr))))

(defn evaluate [expr eval]
  (eval (->lisp expr)))

(defn evaluate-all [exprs env eval]
  (loop [exprs exprs, val nil]
    (if (seq exprs)
      (let [val (evaluate (first exprs) eval)]
        (recur (rest exprs) val))
      [val env])))
