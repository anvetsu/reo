(ns reo.tokens
  (:require [clojure.string :as s]
            [reo.str-util :as su]
            [reo.const :as c]
            [reo.util :as u]))

(def ^:private oprs-kw {\( :openp
                        \) :closep
                        \[ :open-sb
                        \] :close-sb
                        \{ :open-cb
                        \} :close-cb
                        \, :comma
                        \: :define
                        \+ :plus
                        \- :minus
                        \* :mul
                        \/ :div
                        \% :mod
                        \= :eq
                        \< :lt
                        \> :gt
                        \# :hash
                        \& :and
                        \| :or
                        \; :semicolon
                        \@ :at
                        \~ :tilde
                        \! :bang
                        \$ :dollar
                        \^ :caret})

(def ^:private oprs (keys oprs-kw))

(defn- opr->char [opr]
  (when (keyword? opr)
    (loop [oks oprs-kw]
      (when (seq oks)
        (let [[c o] (first oks)]
          (if (= o opr)
            c
            (recur (rest oks))))))))

(defn tokens->str
  ([tokens n]
   (loop [tokens tokens, c 0, s ""]
     (if (and (seq tokens)
              (and (pos? n) (< c n)))
       (let [t (first tokens)
             opr (opr->char t)
             s1 (str s (or opr t))
             s2 (if (and (not opr) (not (keyword? (second tokens))))
                  (str s1 " ") s1)]
         (recur (rest tokens) (inc c) s2))
       s)))
  ([tokens]
   (tokens->str tokens -1)))

(defn- ex [s]
  (u/ex (str "tokens: " s)))

(defn- ident-start-char? [ch]
  (or (Character/isAlphabetic (int ch))
      (= ch \_)))

(defn- opr-char? [ch]
  (some #{ch} oprs))

(defn- num-char-beg? [ch]
  (or (Character/isDigit (int ch))
      (= ch \.)))

(defn- num-char? [ch prev]
  (or (num-char-beg? ch)
      (= ch \_)
      (= ch \e)
      (= ch \E)
      (= ch \b)
      (and (= ch \-)
           (= (Character/toLowerCase prev) \e))))

(defn- str-start-char? [ch]
  (= ch \"))

(def ^:private str-end-char? str-start-char?)

(defn- ticked-start-char? [ch]
  (= ch \`))

(def ^:private ticked-end-char? ticked-start-char?)

(defn- ident-char? [ch _]
  (or (ident-start-char? ch)
      (Character/isDigit (int ch))))

(defn- multichar-token [s predic mk]
  (loop [s s, prev \space cs []]
    (if (seq s)
      (let [c (first s)]
        (if (predic c prev)
          (recur (rest s) c (conj cs c))
          [s (mk cs)]))
      [s (mk cs)])))

(defn- ident [cs]
  (symbol (su/implode cs)))

(def ^:private uscore-p #"_")

(defn- norm-num [s]
  (s/replace s uscore-p ""))

(defn- based-num-char? [ch _]
  (let [i (int ch)]
    (or (Character/isDigit i)
        (= ch \_)
        (Character/isAlphabetic i))))

(defn- as-bitvec [^String s]
  [:bits (.substring s 0 (dec (.length s)))])

(declare number)

(defn- based-number [cs]
  (let [^String ss (s/join cs)
        i (.indexOf ss "_")]
    (if (> i 0)
      (let [bs (.substring ss 0 i)
            ns (.substring ss (inc i))
            radix (Integer/parseInt bs)]
        (Integer/parseInt (norm-num ns) radix))
      (number cs))))

(defn- number [cs]
  (let [need-z? (= (first cs) \.)
        s1 (su/implode cs)
        s2 (if need-z? (str "0" s1) s1)
        ^String s3 (norm-num s2)]
    (if (.endsWith s3 "b")
      (as-bitvec s3)
      (let [v (read-string s3)]
        (when-not (number? v)
          (ex (str "invalid numeric input: " s1)))
        v))))

(defn- identifier [s]
  (multichar-token s ident-char? ident))

(defn- operator [s]
  (let [c (first s)]
    (let [c2 (second s)
          multopr
          (cond
            (= c \>) (when (= c2 \=)
                       :gteq)
            (= c \<) (cond
                       (= c2 \=) :lteq
                       (= c2 \>) :not-eq)
            (= c \@) (cond
                       (= c2 \~) :fold-incr
                       (= c2 \>) :fold-times))]
      (if multopr
        [(nthrest s 2) multopr]
        [(rest s) (get oprs-kw c)]))))

(defn- num-literal [s]
  (let [[n b] [(first s) (second s)]]
    (cond
      (and (or (= n \1) (= \0))
           (= b \b))
      [(nthrest s 2) (if (= n \1) c/t c/f)]

      (and (= n \0) b (Character/isDigit (int b)))
      (multichar-token s based-num-char? based-number)

      :else
      (multichar-token s num-char? number))))

(defn- quoted-literal [end-char? proc s]
  (loop [s (rest s), prev-ch \space, cs []]
    (if (seq s)
      (let [ch (first s)]
        (if (end-char? ch)
          (if (= prev-ch \\)
            (recur (rest s) ch (conj cs ch))
            [(rest s) (proc (su/implode cs))])
          (recur (rest s) ch (conj cs ch))))
      (ex (str "literal not terminated: " (su/implode cs))))))

(def ^:private str-literal (partial quoted-literal str-end-char? identity))
(def ^:private ticked-sym-literal (partial quoted-literal ticked-end-char? symbol))

(defn- escaped-literal [tp s]
  (if (and (= :sym tp)
           (ticked-start-char? (second s)))
    (let [[s sym] (ticked-sym-literal (rest s))]
      [s `(quote ~sym)])
    (let [[s cs]
          (loop [s s, cs []]
            (if (seq s)
              (let [c (first s)]
                (cond
                  (Character/isWhitespace (int c))
                  [s cs]

                  (opr-char? c)
                  (if (= :char tp)
                    (if (>= (count cs) 2)
                      [s cs]
                      (recur (rest s) (conj cs c)))
                    [s cs])

                  :else
                  (recur (rest s) (conj cs c))))
              [s cs]))]
      [s (read-string (su/implode cs))])))

(def ^:private char-literal (partial escaped-literal :char))
(def ^:private sym-literal (partial escaped-literal :sym))

(defn- tokenizer [ch]
  (cond
    (ident-start-char? ch) identifier
    (opr-char? ch) operator
    (num-char-beg? ch) num-literal
    (str-start-char? ch) str-literal
    (ticked-start-char? ch) ticked-sym-literal
    (= ch \\) char-literal
    (= ch \') sym-literal
    :else (ex (str "invalid character in input: " ch))))

(defn- normalize [tokens]
  (or (seq tokens)
      [:void]))

(defn tokens [s]
  (loop [s s, ts []]
    (if (seq s)
      (let [c (first s)]
        (if (Character/isWhitespace (int c))
          (recur (rest s) ts)
          (let [tf (tokenizer c)
                [s t] (tf s)]
            (recur s (conj ts t)))))
      (normalize ts))))
