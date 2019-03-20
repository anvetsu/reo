(ns motto.parse)

(def identifier? symbol?)

(defn literal? [x]
  (or (number? x)
      (string? x)
      (boolean? x)))

(def maybe-fn? identifier?)

(defn- ex [s]
  (throw (Exception. (str "parser: " s))))

(declare parse-expr fetch-expr)

(defn- parse-val [tokens]
  (let [x (first tokens)]
    (if (or (identifier? x) (literal? x))
      (let [s (str x)]
        (cond
          (= s "true") [:true (rest tokens)]
          (= s "false") [:false (rest tokens)]
          :else [x (rest tokens)]))
      [nil tokens])))

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
    (if (and (maybe-fn? x) (seq tokens))
      (let [[args ts] (parse-args tokens)]
        (if args
          [[:call x args] ts]
          [x tokens]))
      [x tokens])))

(defn- parse-parenths [tokens]
  (if (= (first tokens) :openp)
    (let [[expr tokens] (parse-expr (rest tokens))]
      (when-not (= (first tokens) :closep)
        (throw (Exception. (str "missing closing parenthesis: " tokens))))
      [expr (rest tokens)])
    (parse-fncall tokens)))

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
  (parse-arith tokens parse-parenths :mul :div '* '/))

(defn- parse-term [tokens]
  (parse-arith tokens parse-factor :plus :minus '+ '-))

(defn- parse-eq [tokens]
  (let [[x ts1] (parse-term tokens)]
    (if (and x (seq ts1))
      (let [y (first ts1)]
        (if (= :eq y)
          (let [[z ts2] (parse-expr (rest ts1))]
            [['= x z] ts2])
          [x ts1]))
      [x nil])))

(defn- parse-define [tokens]
  (let [[x y] [(first tokens) (second tokens)]]
    (if (identifier? x)
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
  (fetch-expr (parse-define tokens) parse-eq))
