(ns motto.lib.tab
  (:require [motto.util :as u]
            [motto.type :as t]))

(defn mktab [col-names col-vals]
  (loop [ns col-names, vs col-vals, table []]
    (cond
      (and (seq ns) (seq vs))
      (recur (rest ns) (rest vs) (conj table [(first ns) (first vs)]))

      (seq ns)
      (u/ex "tab: not enough values")

      (seq vs)
      (u/ex "tab: not enough columns")

      :else (t/tabify col-names table))))

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
