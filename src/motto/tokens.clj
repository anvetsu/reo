(ns motto.tokens
  (:require [motto.str-util :as su]))

(def ^:private ident-graphs #{\_ \@ \$})
(def ^:private oprs #{\: \+ \- \* \/})

(def ^:private oprs-kw {\: :define
                        \+ :plus
                        \- :minus
                        \* :mul
                        \/ :div})

(defn- ex [s]
  (throw (Exception. (str "tokens: " s))))

(defn- ident-start-char? [ch]
  (or (Character/isAlphabetic ch)
      (some #{ch} ident-graphs)))

(defn- opr-char? [ch]
  (some #{ch} oprs))

(defn- num-char? [ch]
  (or (Character/isDigit ch)
      (= ch \.)))

(defn- str-start-char? [ch]
  (= ch \"))

(defn- ident-char? [ch]
  (or (ident-start-char? ch)
      (Character/isDigit ch)))

(defn- multichar-token [s predic mk]
  (loop [s s, cs []]
    (if (seq s)
      (let [c (first s)]
        (if (predic c)
          (recur (rest s) (conj cs c))
          [s (mk cs)]))
      [s (mk cs)])))

(defn- ident [cs]
  (symbol (su/implode cs)))

(defn- number [cs]
  (let [s (su/implode cs)
        v (read-string s)]
    (when-not (number? v)
      (ex (str "invalid numeric input: " s)))
    v))

(defn- identifier [s]
  (multichar-token s ident-char? ident))

(defn- operator [s]
  [(rest s) (get oprs-kw (first s))])

(defn- num-literal [s]
  (multichar-token s num-char? number))

(defn- str-literal [s]
  ;; TODO
  )

(defn- tokenizer [ch]
  (cond
    (ident-start-char? ch) identifier
    (opr-char? ch) operator
    (num-char? ch) num-literal
    (str-start-char? ch) str-literal
    :else (ex (str "invalid character in input: " ch))))

(defn tokens [s]
  (loop [s s, ts []]
    (if (seq s)
      (let [c (first s)]
        (if (Character/isWhitespace c)
          (recur (rest s) ts)
          (let [tf (tokenizer c)
                [s t] (tf s)]
            (recur s (conj ts t)))))
      ts)))
