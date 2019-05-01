(ns motto.parse
  (:require [motto.type :as tp]
            [motto.tokens :as tk]
            [motto.const :as c]
            [motto.util :as u]))

(defn- ex [s]
  (u/ex (str "parser: " s)))

(declare parse-expr parse-val fetch-expr)

(defn- tokens->str [tokens]
  (str "`" (tk/tokens->str tokens 6) "...`"))

(defn- ignore-comma [tokens]
  (if (= :comma (first tokens))
    (rest tokens)
    tokens))

(defn- assert-defs! [exprs]
  (loop [exprs exprs, defs-over? false]
    (when (seq exprs)
      (let [e (first exprs)]
        (if (and (seqable? e) (= :define (first e)))
          (if defs-over?
            (ex (str "misplaced definition: " e))
            (recur (rest exprs) false))
          (recur (rest exprs) true))))))

(defn- local-defs [exprs]
  (loop [exprs exprs, bindings []]
    (if (seq exprs)
      (let [e (first exprs)]
        (if (and (seqable? e) (= :define (first e)))
          (let [[_ var val] e]
            (recur (rest exprs) (conj bindings var val)))
          [bindings exprs]))
      [bindings exprs])))

(defn- vars->let [exprs]
  (assert-defs! exprs)
  (let [[bindings body] (local-defs exprs)]
    [:let bindings body]))

(defn- parse-params [tokens]
  (if (= :openp (first tokens))
    (loop [ts (rest tokens)
           params []]
      (if (seq ts)
        (let [t (first ts)
              [done? nt]
              (cond
                (tp/identifier? t) [false t]
                (= :and t) [false '&]
                (= :closep t) [true [params (rest ts)]]
                :else (ex (str "invalid parameter: " t)))]
          (if done?
            nt
            (recur (ignore-comma (rest ts)) (conj params nt))))
        (ex "missing closing parenthesis in function definition")))
    (ex (str "missing open parenthesis: " (tokens->str tokens)))))

(defn- fn-iter [body params]
  [:loop (into [] (u/dupl params)) body])

(defn- parse-fn [tokens]
  (let [iter? (= 'recur (first tokens))
        tokens (if iter? (rest tokens) tokens)
        [params ts1] (parse-params tokens)
        [body ts2] (parse-expr ts1)
        body (if iter? (fn-iter body params) body)]
    [(tp/make-fn params body) ts2]))

(defn- parse-cond [tokens]
  (let [[conds ts]
        (loop [ts tokens, conds []]
          (if (seq ts)
            (if (= :close-cb (first ts))
              [(conj conds :false) ts]
              (let [[c ts1] (parse-expr ts)]
                (if (= :close-cb (first ts1))
                  [(conj conds c) ts1]
                  (let [[b ts2] (parse-expr ts1)]
                    (recur ts2 (conj conds [c b]))))))
            [conds nil]))]
    [[:cond conds] ts]))

(defn- parse-if [tokens]
  (cond
    (= :open-cb (first tokens))
    (let [[expr ts] (parse-cond (rest tokens))]
      (when-not (= :close-cb (first ts))
        (ex (str "`if` condition not terminated: " (tokens->str tokens))))
      [expr (rest ts)])

    :else
    (let [[c ts1] (parse-expr tokens)
          [b ts2] (parse-expr ts1)]
      [[:when c b] ts2])))

(defn- parse-atom [x tokens]
  (cond
    (= x c/t) [:true (rest tokens)]
    (= x c/f) [:false (rest tokens)]
    (= x 'fn) (parse-fn (rest tokens))
    (= x 'if) (parse-if (rest tokens))
    :else [x (rest tokens)]))

(defn- parse-list [tokens]
  (loop [tokens tokens, xs []]
    (if (seq tokens)
      (if (= :close-sb (first tokens))
        [[:list xs] (rest tokens)]
        (let [[x tokens] (parse-expr tokens)]
          (recur (ignore-comma tokens) (conj xs x))))
      (ex (str "invalid list: " (tokens->str tokens))))))

(defn- parse-neg-expr [tokens]
  (let [parser (if (= :openp (first tokens))
                 parse-expr
                 parse-val)
        [expr tokens] (parser tokens)]
    [['-neg- expr] tokens]))

(defn- parse-val [tokens]
  (let [x (first tokens)]
    (cond
      (or (tp/identifier? x) (tp/literal? x)) (parse-atom x tokens)
      (= :minus x) (parse-neg-expr (rest tokens))
      (= :open-sb x) (parse-list (rest tokens))
      (tp/bitvec-lit? x) [x (rest tokens)]
      :else [nil tokens])))

(defn- parse-args [tokens]
  (if (= (first tokens) :openp)
    (let [[proper? args ts]
          (loop [tokens (rest tokens), args []]
            (if (seq tokens)
              (if (= (first tokens) :closep)
                [true args (rest tokens)]
                (let [[expr tokens] (parse-expr tokens)]
                  (recur (ignore-comma tokens) (conj args expr))))
              [false args nil]))]
      (when-not proper?
        (ex (str "invalid argument list: " (tokens->str tokens))))
      [args ts])
    [nil tokens]))

(defn- parse-call [x tokens]
  (let [[args ts] (parse-args tokens)]
    (if args
      [[:call x args] ts]
      [x tokens])))

(defn- parse-fncall [tokens]
  (let [[x tokens] (parse-val tokens)]
    (if (and (tp/maybe-fn? x) (seq tokens))
      (parse-call x tokens)
      [x tokens])))

(def ^:private op-syms {:plus '+ :minus '- :mul '* :div '/ :mod '%
                        :eq '= :lt '< :gt '> :lteq '<=
                        :gteq '>= :not-eq '<>})
(def ^:private ops (keys op-syms))

(defn- parse-op [tokens]
  (let [[x y] [(first tokens) (second tokens)]]
    (if (and (some #{x} ops) (= y :closep))
      [(x op-syms) (nthrest tokens 2)]
      [nil tokens])))

(defn- parse-parenths [tokens]
  (if (= (first tokens) :openp)
    (let [[op ts1] (parse-op (rest tokens))]
      (if op
        [op ts1]
        (let [[expr tokens] (parse-expr (rest tokens))]
          (when-not (= (first tokens) :closep)
            (ex (str "missing closing parenthesis: " (tokens->str tokens))))
          [expr (rest tokens)])))
    (parse-fncall tokens)))

(def ^:private infix-fns {:hash '-concat-
                          :semicolon '-conj-
                          :at '-fold-
                          :bang '-filter-
                          :tilde '-map-
                          :fold-incr 'fold-incr
                          :fold-times 'fold-times})

(def ^:private infix-fn-names (keys infix-fns))

(defn- parse-infix-fn [tokens]
  (let [[x ts1] (parse-parenths tokens)]
    (if (and x (seq ts1))
      (let [y (first ts1)]
        (if (some #{y} infix-fn-names)
          (let [[z ts2] (parse-expr (rest ts1))]
            (if (= y :fold-times)
              (let [[w ts3] (parse-expr ts2)]
                [[(y infix-fns) w z x] ts3])
              [[(y infix-fns) z x] ts2]))
          [x ts1]))
      [x ts1])))

(declare parse-term parse-cmpr)

(defn- parse-arith [tokens precede opr-fns]
  (let [[x ts1] (precede tokens)]
    (if x
      (loop [ts1 ts1, exprs x]
        (if (seq ts1)
          (let [y (first ts1)]
            (if (contains? opr-fns y)
              (let [[z ts2] (precede (rest ts1))]
                (recur ts2 [(y opr-fns) exprs z]))
              [exprs ts1]))
          [exprs ts1])))))

(defn- parse-factor [tokens]
  (parse-arith tokens parse-infix-fn {:mul '* :div '/ :mod '%}))

(defn- parse-term [tokens]
  (parse-arith tokens parse-factor {:plus '+ :minus '-}))

(def ^:private cmpr-oprs-map {:eq '=
                              :lt '<
                              :gt '>
                              :lteq '<=
                              :gteq '>=
                              :not-eq '<>})

(def ^:private cmpr-opr-keys (keys cmpr-oprs-map))

(defn- parse-cmpr [tokens]
  (let [[x ts1] (parse-term tokens)]
    (if (and x (seq ts1))
      (let [y (first ts1)]
        (if (some #{y} cmpr-opr-keys)
          (let [[z ts2] (parse-term (rest ts1))]
            [[(get cmpr-oprs-map y) x z] ts2])
          [x ts1]))
      [x nil])))

(defn- parse-logical [tokens]
  (let [[x ts1] (parse-cmpr tokens)]
    (if (and x (seq ts1))
      (let [y (first ts1)]
        (if (or (= :and y) (= :or y))
          (let [[z ts2] (parse-expr (rest ts1))]
            [[y x z] ts2])
          [x ts1]))
      [x nil])))

(defn- parse-load [tokens]
  (let [[e ts] (parse-expr tokens)]
    (when-not e
      (ex (str "invalid load: " (tokens->str tokens))))
    [[:load e] ts]))

(def ^:private reserved-names #{'fn 'if})

(defn- valid-ident [var]
  (when (some #{var} reserved-names)
    (ex (str "reserved name: " var)))
  var)

(defn- multi-define [ns vs]
  (let [bs (map (fn [n i] [:define (valid-ident n) [:call vs [i]]])
                ns (range (count ns)))]
    [:block bs]))

(defn- parse-define [x tokens]
  (let [[e ts] (parse-expr tokens)]
    (when-not e
      (ex (str "no value to bind: " x)))
    (if (and (seqable? x) (= :list (first x)))
      [(multi-define (first (rest x)) e) ts]
      [[:define (valid-ident x) e] ts])))

(defn- parse-defs [tokens]
  (let [x (first tokens)]
    (cond
      (tp/identifier? x) [x (rest tokens)]
      (= :open-sb x) (parse-list (rest tokens))
      :else [nil tokens])))

(defn- parse-stmt [tokens]
  (let [[x ts] (parse-defs tokens)]
    (if x
      (cond
        (= x 'ld) (parse-load (rest ts))
        (= :define (first ts)) (parse-define x (rest ts))
        :else [nil tokens])
      [nil tokens])))

(defn- fetch-expr [[e tokens] next-parser]
  (if e
    (loop [e e, tokens tokens]
      (cond
        (= :openp (first tokens))
        (let [[e tokens] (parse-call e tokens)]
          (recur e tokens))
        :else
        [e tokens]))
    (when next-parser
      (fetch-expr (next-parser tokens) nil))))

(defn- blockify [exprs]
  [:block [(vars->let exprs)]])

(defn- parse-expr [tokens]
  (let [p (fn [tokens]
            (fetch-expr (parse-stmt tokens) parse-logical))
        block? (= :open-cb (first tokens))
        tokens (if block? (rest tokens) tokens)
        [expr tokens] (p tokens)]
    (if block?
      (loop [tokens tokens, exprs [expr]]
        (if (seq tokens)
          (let [t (first tokens)]
            (if (= :close-cb t)
              [(blockify exprs) (rest tokens)]
              (let [[expr tokens] (p tokens)]
                (recur tokens (conj exprs expr)))))
          (ex "code-block not closed")))
      [expr tokens])))

(defn parse [tokens]
  (if (= tokens [:void])
    tokens
    (parse-expr tokens)))
