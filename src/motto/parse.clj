(ns motto.parse
  (:require [motto.type :as tp]))

(defn- ex [s]
  (throw (Exception. (str "parser: " s))))

(declare parse-expr parse-val fetch-expr)

(defn- parse-params [tokens]
  (if (= :openp (first tokens))
    (loop [ts (rest tokens)
           params []]
      (if (seq ts)
        (let [t (first ts)]
          (cond
            (tp/identifier? t) (recur (rest ts) (conj params t))
            (= :closep t) [params (rest ts)]
            :else (ex (str "invalid parameter: " t))))
        (ex "missing closing parenthesis in function definition")))
    (ex (str "missing open parenthesis: " tokens))))

(defn- parse-fn [tokens]
  (let [[params ts1] (parse-params tokens)
        [body ts2] (parse-expr ts1)]
    [(tp/make-fn params body) ts2]))

(defn- parse-atom [x tokens]
  (cond
    (= x 't) [:true (rest tokens)]
    (= x 'f) [:false (rest tokens)]
    (= x 'fn) (parse-fn (rest tokens))
    :else [x (rest tokens)]))

(defn- parse-list [tokens]
  (loop [tokens tokens, xs []]
    (if (seq tokens)
      (if (= :close-sb (first tokens))
        [[:list xs] (rest tokens)]
        (let [[x tokens] (parse-expr tokens)]
          (recur tokens (conj xs x))))
      (ex (str "invalid list: " tokens)))))

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
      :else [nil tokens])))

(defn- parse-args [tokens]
  (if (= (first tokens) :openp)
    (let [[proper? args ts]
          (loop [tokens (rest tokens), args []]
            (if (seq tokens)
              (if (= (first tokens) :closep)
                [true args (rest tokens)]
                (let [[expr tokens] (parse-expr tokens)]
                  (recur tokens (conj args expr))))
              [false args nil]))]
      (when-not proper?
        (ex (str "invalid argument list: " tokens)))
      [args ts])
    [nil tokens]))

(defn- parse-fncall [tokens]
  (let [[x tokens] (parse-val tokens)]
    (if (and (tp/maybe-fn? x) (seq tokens))
      (let [[args ts] (parse-args tokens)]
        (if args
          [[:call x args] ts]
          [x tokens]))
      [x tokens])))

(defn- parse-parenths [tokens]
  (if (= (first tokens) :openp)
    (let [[expr tokens] (parse-expr (rest tokens))]
      (when-not (= (first tokens) :closep)
        (ex (str "missing closing parenthesis: " tokens)))
      [expr (rest tokens)])
    (parse-fncall tokens)))

(defn- parse-accessor [tokens]
  (let [[x ts1] (parse-parenths tokens)]
    (if (and x (seq ts1))
      (let [y (first ts1)]
        (if (= y :hash)
          (let [[z ts2] (parse-expr (rest ts1))]
            [['-get- z x] ts2])
          [x ts1]))
      [x ts1])))

(defn- parse-arith [tokens precede opr1 opr2 f1 f2]
  (let [[x ts1] (precede tokens)]
    (if (and x (seq ts1))
      (let [y (first ts1)]
        (if (or (= opr1 y) (= opr2 y))
          (let [[z ts2] (parse-expr (rest ts1))]
            [[(if (= opr1 y) f1 f2) x z] ts2])
          [x ts1]))
      [x nil])))

(defn- parse-factor [tokens]
  (parse-arith tokens parse-accessor :mul :div '* '/))

(defn- parse-term [tokens]
  (parse-arith tokens parse-factor :plus :minus '+ '-))

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

(defn- parse-define [tokens]
  (let [[x y] [(first tokens) (second tokens)]]
    (if (tp/identifier? x)
      (if (= :define y)
        (let [[e ts] (parse-expr (nthrest tokens 2))]
          (when-not e
            (ex (str "no value to bind: " x)))
          [[:define x e] ts])
        [nil tokens])
      [nil tokens])))

(defn- fetch-expr [[e tokens] next-parser]
  (if e
    [e tokens]
    (when next-parser
      (fetch-expr (next-parser tokens) nil))))

(defn parse-expr [tokens]
  (fetch-expr (parse-define tokens) parse-logical))
