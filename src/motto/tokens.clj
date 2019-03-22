(ns motto.tokens
  (:require [motto.str-util :as su]))

(def ^:private ident-graphs #{\_ \@ \$})

(def ^:private oprs-kw {\( :openp
                        \) :closep
                        \[ :open-sb
                        \] :close-sb
                        \: :define
                        \+ :plus
                        \- :minus
                        \* :mul
                        \/ :div
                        \= :eq
                        \< :lt
                        \> :gt
                        \& :and
                        \| :or})

(def ^:private oprs (keys oprs-kw))

(def ^:private comment-char \/)

(defn- ex [s]
  (throw (Exception. (str "tokens: " s))))

(defn- ident-start-char? [ch]
  (or (Character/isAlphabetic (int ch))
      (some #{ch} ident-graphs)))

(defn- opr-char? [ch]
  (some #{ch} oprs))

(defn- num-char? [ch]
  (or (Character/isDigit (int ch))
      (= ch \.)))

(defn- str-start-char? [ch]
  (= ch \"))

(def ^:private str-end-char? str-start-char?)

(defn- ident-char? [ch]
  (or (ident-start-char? ch)
      (Character/isDigit (int ch))))

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
  (let [need-z? (= (first cs) \.)
        s1 (su/implode cs)
        s2 (if need-z? (str "0" s1) s1)
        v (read-string s2)]
    (when-not (number? v)
      (ex (str "invalid numeric input: " s1)))
    v))

(defn- identifier [s]
  (multichar-token s ident-char? ident))

(defn- operator [s]
  (let [c (first s)
        multopr
        (cond
          (= c \>) (when (= (second s) \=)
                     :gteq)
          (= c \<) (let [c2 (second s)]
                     (cond
                       (= c2 \=) :lteq
                       (= c2 \>) :not-eq)))]
    (if multopr
      [(nthrest s 2) multopr]
      [(rest s) (get oprs-kw c)])))

(defn- num-literal [s]
  (multichar-token s num-char? number))

(defn- str-literal [s]
  (loop [s (rest s), prev-ch \space, cs []]
    (if (seq s)
      (let [ch (first s)]
        (if (str-end-char? ch)
          (if (= prev-ch \\)
            (recur (rest s) ch (conj cs ch))
            [(rest s) (su/implode cs)])
          (recur (rest s) ch (conj cs ch))))
      (ex (str "string not terminated: " (su/implode cs))))))

(defn- tokenizer [ch]
  (cond
    (ident-start-char? ch) identifier
    (opr-char? ch) operator
    (num-char? ch) num-literal
    (str-start-char? ch) str-literal
    :else (ex (str "invalid character in input: " ch))))

(defn- comment? [c1 c2]
  (and (= c1 comment-char)
       (= c2 comment-char)))

(defn tokens [s]
  (loop [s s, ts []]
    (if (seq s)
      (let [c (first s)]
        (if (comment? c (second s))
          ts
          (if (Character/isWhitespace (int c))
            (recur (rest s) ts)
            (let [tf (tokenizer c)
                  [s t] (tf s)]
              (recur s (conj ts t))))))
      ts)))
