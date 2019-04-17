(ns motto.lib.tab
  (:require [motto.util :as u]
            [motto.type :as t]))

(declare mktab)

(defn- dicts->tab [dicts]
  (let [colnames (keys (first dicts))]
    (loop [ds dicts
           cols (into [] (repeat (count colnames) []))]
      (if (seq ds)
        (let [cs (map #(get (first ds) %) colnames)]
          (recur (rest ds) (u/spread cols cs)))
        (mktab (map symbol colnames) cols)))))

(defn mktab
  ([col-names col-vals]
   (loop [ns col-names, vs col-vals, table []]
     (cond
       (and (seq ns) (seq vs))
       (recur (rest ns) (rest vs) (conj table [(first ns) (first vs)]))

       (seq ns)
       (u/ex "tab: not enough values")

       (seq vs)
       (u/ex "tab: not enough columns")

       :else (t/tabify col-names table))))
  ([dicts]
   (dicts->tab dicts)))

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
