;; An interpreter for motto.
(ns motto.eval
  (:require [motto.env :as env]
            [motto.compile :as c]
            [motto.type :as tp]
            [motto.util :as u]))

(defn- ex [s]
  (u/ex (str "eval: " s)))

(declare evaluate evaluate-all)

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

(defn- apply-fn [fnval args env]
  (let [e1 (env/link (tp/fnenv fnval) env)
        e2 (env/amend-all e1 (tp/fnparams fnval) args)
        [v _] (evaluate (tp/fnbody fnval) e2)]
    [v env]))

(defn- call-fn [fn args env]
  (let [[fnval env] (evaluate fn env)
        [eargs env] (eval-map args env)]
    (cond
      (tp/function? fnval) (apply-fn fnval eargs env)
      (fn? fnval) [(apply fnval eargs) env]
      :else (ex (str "invalid function: " fn)))))

(defn- eval-shortcircuit [exprs env check]
  (loop [exprs exprs, last-val false, env env]
    (if (seq exprs)
      (let [[v env] (evaluate (first exprs) env)]
        (if (check v)
          [v env]
          (recur (rest exprs) v env)))
      [last-val env])))

(defn- eval-and [exprs env]
  (eval-shortcircuit exprs env not))

(defn- eval-or [exprs env]
  (eval-shortcircuit exprs env identity))

(defn- eval-block [exprs env]
  (loop [exprs exprs, val nil, env env]
    (if (seq exprs)
      (let [[v e] (evaluate (first exprs) env)]
        (recur (rest exprs) v e))
      [val env])))

(defn- eval-form [ident args env]
  (case ident
    :define (amend (first args) (second args) env)
    :call (call-fn (first args) (second args) env)
    :list (eval-map (first args) env)
    :and (eval-and args env)
    :or (eval-or args env)
    :block (eval-block (first args) env)
    :load (ld (first args) env)
    (call-fn ident args env)))

(defn- force-lookup [env expr]
  (let [v (env/lookup env expr)]
    (if (nil? v)
      (ex (str "binding not found: " expr))
      v)))

(defn evaluate [expr env]
  (cond
    (= expr :true) [true env]
    (= expr :false) [false env]
    (tp/literal? expr) [expr env]
    (tp/identifier? expr) [(force-lookup env expr) env]
    (tp/function? expr) [(tp/closure expr env) env]
    (seq expr) (eval-form (first expr) (rest expr) env)))

(defn evaluate-all [exprs env]
  (loop [exprs exprs, [val env] [nil env]]
    (if (seq exprs)
      (let [[val env] (evaluate (first exprs) env)]
        (recur (rest exprs) [val env]))
      [val env])))
