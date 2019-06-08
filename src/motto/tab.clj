(ns motto.tab
  (:require [incanter.core :as ic]
            [motto.util :as u]
            [motto.list :as ls]))

(declare mkt)

(defn as-t [col-names data]
  (when-not (= (count col-names) (count (set col-names)))
    (u/ex (str "tab: duplicate columns: " col-names)))
  (let [t (into {} data)]
    (assoc t :-meta-
           {:coldict true
            :columns col-names})))

(defn tdata
  ([t]
   (dissoc t :-meta-))
  ([t cols]
   (map #(get t %) cols)))

(defn- tmeta [t]
  (:-meta- t))

(defn t? [x]
  (and (map? x)
       (:coldict (tmeta x))
       true))

(defn tcols [t]
  (when (t? t)
    (:columns (tmeta t))))

(defn- merge-cols [cols1 cols2]
  (loop [cs cols2, cols cols1]
    (if (seq cs)
      (let [c (first cs)]
        (if (some #{c} cols)
          (recur (rest cs) cols)
          (recur (rest cs) (conj cols c))))
      cols)))

(defn tmerge [t1 t2]
  (let [cols1 (:columns (tmeta t1))
        cols2 (:columns (tmeta t2))
        newcols (merge-cols cols1 cols2)
        newt (merge (tdata t1) (tdata t2))]
    (as-t newcols newt)))

(defn- dicts->t [dicts]
  (let [colnames (keys (first dicts))]
    (loop [ds dicts
           cols (into [] (repeat (count colnames) []))]
      (if (seq ds)
        (let [cs (map #(get (first ds) %) colnames)]
          (recur (rest ds) (u/spread cols cs)))
        (mkt (map symbol colnames) cols)))))

(defn- seqs->t [col-names col-vals]
  (loop [ns col-names, vs col-vals, data []]
    (cond
      (and (seq ns) (seq vs))
      (recur (rest ns) (rest vs) (conj data [(first ns) (first vs)]))

      (seq ns)
      (u/ex "tab: not enough values")

      (seq vs)
      (u/ex "tab: not enough columns")

      :else data)))

(defn dset->t [dset]
  (let [colnames (map symbol (:column-names dset))
        cols (:columns dset)]
    (mkt colnames cols)))

(defn t->dset [t]
  (let [colnames (vec (tcols t))
        cols (tdata t)]
    (ic/dataset colnames cols)))

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

(defn mkt
  ([col-names col-vals]
   (if (int? (first col-names))
     (reshape col-names col-vals)
     (let [t (seqs->t col-names col-vals)]
       (as-t col-names t))))
  ([dicts]
   (dicts->t dicts)))

(defn -t- [x y] (mkt (u/in-seq x) (u/in-seq y)))

(defn top [n t]
  (let [[colnames data] [(tcols t) (tdata t)]]
    (loop [cs colnames, rs []]
      (if (seq cs)
        (let [k (first cs)
              v (get data k)]
          (recur (rest cs) (conj rs (take n v))))
        (mkt colnames rs)))))

(defn group
  ([f default col by]
   (loop [xs col, ys by, rs {}]
     (if (seq ys)
       (let [y (first ys)
             r1 (get rs y default)
             r2 (f (first xs) r1)]
         (recur (rest xs) (rest ys)
                (assoc rs y r2)))
       (into {} rs))))
  ([f default by]
   (group f default (set by) by)))

(def group-count (partial group #(inc %2) 0))

(defn- as-row [colnames colvals]
  (into {} (map vector colnames colvals)))

(defn -where- [predic t]
  (let [colnames (tcols t)
        cols (vals (tdata t))
        rows (apply map (fn [x & xs] (as-row colnames (apply list x xs))) cols)]
    (loop [rows rows, rs (into [] (repeat (count colnames) []))]
      (if (seq rows)
        (let [r (first rows)]
          (if (predic r)
            (recur (rest rows) (u/spread rs (vals r)))
            (recur (rest rows) rs)))
        (mkt colnames rs)))))

(defn -filter- [f xs]
  (if (t? xs)
    (-where- f xs)
    (filter f xs)))

(defn- rtmeta [x]
  (:-meta- x))

(defn rt? [x]
  (and (map? x)
       (:table (rtmeta x))
       true))

(defn rtcols [x]
  (:columns (rtmeta x)))

(defn rtdata [x]
  (into [] (:data (dissoc x :-meta-))))

(defn rtsize [x]
  (:count (rtmeta x)))

(defn mkrt [colnames rows]
  {:data rows
   :-meta- {:table true
            :columns colnames
            :count (count rows)}})

(defn- t->rt [t]
  (let [colnames (tcols t)
        cols (tdata t)
        data (map #(get cols %) colnames)
        rows (apply map vector data)]
    (mkrt colnames rows)))

(defn- rt->t [rt]
  (let [colnames (rtcols rt)]
    (loop [rows (:data rt), rs (into [] (repeat (count colnames) []))]
      (if (seq rows)
        (recur (rest rows) (u/spread rs (first rows)))
        (mkt colnames rs)))))

(defn- flip-seqs [xs]
  (apply map vector xs))

;; Convert a coldict (t) to a records table and vice versa.
(defn flip [x]
  (cond
    (t? x) (t->rt x)
    (rt? x) (rt->t x)
    :else (flip-seqs x)))

(defn size [x]
  (cond
    (t? x) (count (first (vals (tdata x))))
    (rt? x) (rtsize x)
    :else nil))

(defn cols [x]
  (cond
    (t? x) (tcols x)
    (rt? x) (rtcols x)
    :else []))

(defn rows [x]
  (cond
    (t? x) (vals (tdata x))
    (rt? x) (rtdata x)
    :else []))

(defn rt->dset [t]
  (let [colnames (vec (rtcols t))
        cols (rtdata t)]
    (ic/dataset colnames cols)))

(defn tmap [f t]
  (let [colnames (tcols t)
        cols (vals (tdata t))
        rows (apply map (fn [x & xs] (as-row colnames (apply list x xs))) cols)]
    (loop [rows rows, rs (into [] (repeat (count colnames) []))]
      (if (seq rows)
        (let [r (first rows)
              r2 (f r)
              nr (merge r r2)]
          (recur (rest rows) (u/spread rs (vals nr))))
        (mkt colnames rs)))))

(defn maybe-dset [x]
  (cond
    (t? x) (t->dset x)
    (rt? x) (rt->dset x)
    :else x))

(defn save
  ([x filename options]
   (let [dat (maybe-dset x)
         delim (get options 'delim)
         header (get options 'header)
         append (get options 'append)]
     (ic/save dat filename :delim delim
              :header header :append append)))
  ([x filename] (save x filename nil)))
