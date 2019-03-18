(ns motto.parse)

(def identifier? symbol?)

(defn literal? [x]
  (or (number? x)
      (string? x)
      (boolean? x)))

(defn- ex [s]
  (throw (Exception. (str "parser: " s))))

(defn- parse-literal [tokens]
  (let [x (first tokens)]
    (if (literal? x)
      [x (rest tokens)]
      [nil tokens])))

(declare parse-expr)

(defn- parse-define [tokens]
  (let [[x y] [(first tokens) (second tokens)]]
    (if (identifier? x)
      (if (= :define y)
        (let [[e ts] (parse-expr (nthrest tokens 2))]
          (when-not e
            (ex (str "no value to bind: " x)))
          [[:define x e] ts])
        [x (rest tokens)])
      [nil tokens])))

(defn- fetch-expr [[e tokens] next-parser]
  (if e
    [e tokens]
    (when next-parser
      (fetch-expr (next-parser tokens) nil))))

(defn parse-expr [tokens]
  (fetch-expr (parse-define tokens) parse-literal))
