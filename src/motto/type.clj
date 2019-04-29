(ns motto.type
  (:require [clojure.set :as set]))

(def identifier? symbol?)

(defn quoted? [x]
  (and (seqable? x)
       (= 'quote (first x))))

(defn bitvec-lit? [x]
  (and (seqable? x)
       (= (first x) :bits)))

(defn literal? [x]
  (or (number? x)
      (string? x)
      (boolean? x)
      (char? x)
      (quoted? x)))

(def maybe-fn? identifier?)

(defn make-fn [params body]
  {:fn {:params params :body body}})

(defn function? [x]
  (and (map? x) (:fn x)))

(defn closure [f env]
  (let [obj (:fn f)]
    {:fn (assoc obj :env env)}))

(defn fnparams [f]
  (:params (:fn f)))

(defn fnbody [f]
  (:body (:fn f)))

(defn fnenv [f]
  (:env (:fn f)))

(defn tabify [col-names table]
  (let [tab (into {} table)]
    (assoc tab :-meta-
           {:table true
            :columns (set col-names)})))

(defn- tab-table [tab]
  (dissoc tab :-meta-))

(defn- tab-meta [tab]
  (:-meta- tab))

(defn tab? [x]
  (and (:table (tab-meta x))
       true))

(defn tab-data [tab]
  (when (tab? tab)
    (let [meta (tab-meta tab)]
      [(:columns meta) (tab-table tab)])))

(defn tab-cols [tab]
  (when (tab? tab)
    (:columns (tab-meta tab))))

(defn tab-merge [tab1 tab2]
  (let [cols1 (:columns (tab-meta tab1))
        cols2 (:columns (tab-meta tab2))
        newcols (set/union cols1 cols2)
        newtable (merge (tab-table tab1) (tab-table tab2))]
    (tabify newcols newtable)))

(defn err [x]
  {:error x})

(defn err? [x]
  (and (:error x)
       true))

(defn err-data [x]
  (:error x))

(defn copy [^Cloneable obj]
  (.clone obj))
