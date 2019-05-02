(ns motto.lib.tab
  (:require [motto.util :as u]
            [motto.type :as t]
            [motto.lib.list :as ls]))

(declare mktab)

(defn- dicts->tab [dicts]
  (let [colnames (keys (first dicts))]
    (loop [ds dicts
           cols (into [] (repeat (count colnames) []))]
      (if (seq ds)
        (let [cs (map #(get (first ds) %) colnames)]
          (recur (rest ds) (u/spread cols cs)))
        (mktab (map symbol colnames) cols)))))

(defn- seqs->table [col-names col-vals]
  (loop [ns col-names, vs col-vals, table []]
    (cond
      (and (seq ns) (seq vs))
      (recur (rest ns) (rest vs) (conj table [(first ns) (first vs)]))

      (seq ns)
      (u/ex "tab: not enough values")

      (seq vs)
      (u/ex "tab: not enough columns")

      :else table)))

(defn- tk [n ys nextn]
  (loop [xs ys, i 0, rs []]
    (if (and nextn (>= i nextn))
      rs
      (if (seq xs)
        (let [[ts xs] (ls/take-repeat n xs ys)]
          (recur xs (inc i) (conj rs ts)))
        rs))))

(defn- reshape [dim vals]
  (loop [is (reverse dim), rs vals]
    (if (seq is)
      (let [s (second is)
            t (get (vec is) 2)]
        (recur (rest is) (tk (first is) rs
                             (cond
                               (and s t) nil
                               s s
                               :else 1))))
        (first rs))))

(defn mktab
  ([col-names col-vals]
   (if (int? (first col-names))
     (reshape col-names col-vals)
     (let [t (seqs->table col-names col-vals)]
       (t/tabify col-names t))))
  ([dicts]
   (dicts->tab dicts)))

(defn -tab- [x y] (mktab (u/in-seq y) (u/in-seq x)))

(def tab-merge t/tab-merge)

(defn cols [tab]
  (t/tab-cols tab))

(defn top [n tab]
  (let [[colnames data] (t/tab-data tab)]
    (loop [cs colnames, rs []]
      (if (seq cs)
        (let [k (first cs)
              v (get data k)]
          (recur (rest cs) (conj rs (take n v))))
        (mktab colnames rs)))))

(defn group [f default col by]
  (loop [xs col, ys by, rs {}]
    (if (seq ys)
      (let [y (first ys)
            r1 (get rs y default)
            r2 (f (first xs) r1)]
        (recur (rest xs) (rest ys)
               (assoc rs y r2)))
      (into {} rs))))

(defn- count-grp-inc [_ c]
  (+ c 1))

(defn count-grp [col by]
  (group count-grp-inc 0 col by))
