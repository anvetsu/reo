(ns motto.expr-io
  (:require [clojure.string :as str]
            [motto.type :as tp]))

(declare write)

(defn- writable? [x]
  (not (or (nil? x) (= x :void))))

(defn write-vec [v]
  (print "[")
  (when (seq v)
    (loop [v v]
      (let [x (first v)
            r (rest v)]
        (write x)
        (when (seq r)
          (do (when-not (nil? (first r)) (print " "))
              (recur r))))))
  (print "]"))

(defn- write-tab [tab]
  (let [[col-names data] (tp/tab-data tab)]
    (loop [cs col-names]
      (when (seq cs)
        (let [k (first cs)
              v (get data k)
              r (rest cs)]
          (write k)
          (print ": ")
          (write v)
          (when (seq r)
            (println))
          (recur r))))))

(defn- write-dict [m]
  (print "[")
  (loop [m m]
      (when (seq m)
        (let [[k v] (first m)]
          (write k)
          (print ":")
          (write v)
          (when (seq (rest m))
            (print " "))
          (recur (rest m)))))
  (print "]"))

(defn write-err [e]
  (print (str "ERROR: " (tp/err-data e))))

(defn write [x]
  (when (writable? x)
    (let [v  (cond
               (boolean? x) (if x 't 'f)
               (or (tp/function? x)
                   (fn? x)) '<fn>
               :else x)]
      (cond
        (tp/tab? x) (write-tab x)
        (tp/err? x) (write-err x)
        (string? v) (print v)
        (map? v) (write-dict v)
        (seqable? v) (write-vec v)
        :else (print v)))))

(defn writeln [x]
  (when (writable? x)
    (write x)
    (println)))

(defn- match-curlies [s]
  (loop [ss (seq s), opn 0, cls 0]
    (if (seq ss)
      (let [c (first ss)
            [opn cls]
            (cond
              (= \{ c) [(inc opn) cls]
              (= \} c) [opn (inc cls)]
              :else [opn cls])]
        (recur (rest ss) opn cls))
      (- opn cls))))

(defn readln []
  (if-let [s (read-line)]
    (if (str/ends-with? s "  ")
      [:more s]
      (let [c (match-curlies s)]
        (cond
          (<= c 0) [:done s]
          (pos? c) [:more s])))
    [:eof nil]))

(defn read-multiln
  ([stepper]
   (loop [[flag s] (readln)
          ss []]
     (case flag
       :more (do (when stepper (stepper))
                 (recur (readln)
                        (conj ss s)))
       :done (str/join (conj ss s))
       :eof nil)))
  ([] (read-multiln nil)))
