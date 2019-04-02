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
