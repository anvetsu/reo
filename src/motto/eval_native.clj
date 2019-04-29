;; Tranlsate motto to clojure and eval.
(ns motto.eval-native
  (:require [clojure.tools.logging :as log]
            [motto.tokens :as t]
            [motto.parse :as p]
            [motto.compile :as c]
            [motto.util :as u]
            [motto.type :as tp]))

(def ^:private idents (set '(+ - * / % = > < >= <=)))

(defn- translate-ident [s]
  (if (= s '/)
    '-div-
    (if (some #{s} idents)
      (symbol (str "-" s "-"))
      s)))

(defn- ex [s]
  (u/ex (str "eval-native: " s)))

(declare ->lisp evaluate-all)

(defn ld [^String file-path eval]
  (let [file-path (if (.endsWith file-path ".mo")
                   file-path
                   (str file-path ".mo"))]
    (when-not (u/file-exists? file-path)
      (c/compile-file (u/normalize-file-path file-path)))
    (let [exprss (c/slurp-o file-path)]
      (loop [exprss exprss, val val]
        (if (seq exprss)
          (let [val (evaluate-all (first exprss) eval)]
            (recur (rest exprss) val))
          val)))))

(defn all->lisp [exprs lsp]
  (map #(lsp %) exprs))

(defn- call-fn [fn args lsp]
  (let [fnval (lsp fn)
        eargs (all->lisp args lsp)]
    (concat (list fnval) eargs)))

(defn- form->let [bindings body lsp]
  (let [body-expr (lsp body)]
    (if (seq bindings)
      `(let ~(into [] (lsp bindings))
         ~@body-expr)
      `(do ~@body-expr))))

(defn- condbody->lisp [exprs lsp]
  (loop [exprs exprs, s-exprs []]
    (if (seq exprs)
      (if (seq (rest exprs))
        (let [[c b] (first exprs)]
          (recur (rest exprs)
                 (conj s-exprs (lsp c) (lsp b))))
        (recur (rest exprs)
               (conj s-exprs :else (lsp (first exprs)))))
      s-exprs)))

(defn- form->lisp [ident args eval]
  (let [lsp (partial ->lisp eval)]
    (case ident
      :define `(do (def ~(first args)
                     ~(lsp (second args)))
                   '~(first args))
      :call (call-fn (first args) (second args) lsp)
      :list (vec (all->lisp (first args) lsp))
      :and `(and ~@(all->lisp args lsp))
      :or `(or ~@(all->lisp args lsp))
      :block `(do ~@(all->lisp (first args) lsp))
      :when `(if ~(lsp (first args)) ~(lsp (second args)) false)
      :cond `(cond ~@(condbody->lisp (first args) lsp))
      :let (form->let (first args) (second args) lsp)
      :loop `(loop ~(first args) ~(lsp (second args)))
      :load `(ld ~(first args) eval)
      (call-fn ident args lsp))))

(defn- mkfn [fexpr]
  (let [f (:fn fexpr)]
    `(fn ~(into [] (:params f)) ~(->lisp eval (:body f)))))

(defn ->lisp [eval expr]
  (cond
    (= expr :true) true
    (= expr :false) false
    (= expr :void) :void
    (tp/literal? expr) expr
    (tp/identifier? expr) (translate-ident expr)
    (tp/function? expr) (mkfn expr)
    (seq expr) (form->lisp (first expr) (rest expr) eval)))

(defn evaluate [expr eval]
  (try
    (eval (->lisp eval expr))
    (catch Exception ex
      (log/error ex)
      (throw ex))))

(defn evaluate-all [exprs eval]
  (loop [exprs exprs, val nil]
    (if (seq exprs)
      (let [val (evaluate (first exprs) eval)]
        (recur (rest exprs) val))
      val)))
