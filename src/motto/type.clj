(ns motto.type)

(def identifier? symbol?)

(defn quoted? [x]
  (and (seqable? x)
       (= 'quote (first x))))

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
            :columns col-names})))

(defn tab? [x]
  (and (:table (:-meta- x))
       true))

(defn tab-data [tab]
  (when (tab? tab)
    (let [meta (:-meta- tab)]
      [(:columns meta) (dissoc tab :-meta-)])))

(defn tab-cols [tab]
  (when (tab? tab)
    (:columns (:-meta- tab))))

(defn err [x]
  {:error x})

(defn err? [x]
  (and (:error x)
       true))

(defn err-data [x]
  (:error x))
