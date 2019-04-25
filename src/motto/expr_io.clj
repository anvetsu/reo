(ns motto.expr-io
  (:require [clojure.string :as str]
            [motto.type :as tp]
            [motto.bitvec :as bv]
            [motto.lib.dt :as dt]))

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
        (when (and (seqable? x) (seq r))
          (println))
        (when (seq r)
          (do (when-not (nil? (first r)) (print " "))
              (recur r))))))
  (print "]"))

(defn- print-bval [b]
  (if b
    (print "1")
    (print "0")))

(defn write-bitvec [bv]
  (bv/for-each print-bval bv)
  (print "b"))

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

(defn write-dt [dt]
  (print (str "dt(\"" (dt/sdt dt) "\")")))

(def ^:private t (symbol "1"))
(def ^:private f (symbol "0"))

(defn write [x]
  (when (writable? x)
    (let [v  (cond
               (boolean? x) (if x t f)
               (or (tp/function? x)
                   (fn? x)) '<fn>
               :else x)]
      (cond
        (tp/tab? x) (write-tab x)
        (tp/err? x) (write-err x)
        (string? v) (print v)
        (bv/bitvec? x) (write-bitvec x)
        (map? v) (write-dict v)
        (instance? java.util.Calendar v) (write-dt v)
        (seqable? v) (write-vec v)
        :else (print v)))))

(defn writeln [x]
  (when (writable? x)
    (write x)
    (println)))

(defn- match-braces [s opns]
  (loop [ss (seq s), opns opns, clss [0 0 0]]
    (if (seq ss)
      (let [c (first ss)
            [pc bc cc] opns
            [pcc bcc ccc] clss
            [n-opns n-clss]
            (cond
              (= \{ c) [[pc bc (inc cc)] clss]
              (= \} c) [opns [pcc bcc (inc ccc)]]
              (= \( c) [[(inc pc) bc cc] clss]
              (= \) c) [opns [(inc pcc) bcc ccc]]
              (= \[ c) [[pc (inc bc) cc] clss]
              (= \] c) [opns [pcc (inc bcc) ccc]]
              :else [opns clss])]
        (recur (rest ss) n-opns n-clss))
      (map - opns clss))))

(defn readln
  ([brace-counts]
   (if-let [s (read-line)]
     (if (str/ends-with? s "  ")
       [:more s]
       (let [c (match-braces s brace-counts)]
         (cond
           (every? #(<= % 0) c) [:done s]
           (some pos? c) [:more (str s " ") c])))
     [:eof nil]))
  ([] (readln [0 0 0])))

(defn read-multiln
  ([stepper]
   (loop [[flag s counts] (readln)
          ss []]
     (case flag
       :more (do (when stepper (stepper))
                 (recur (readln counts)
                        (conj ss s)))
       :done (str/join (conj ss s))
       :eof nil)))
  ([] (read-multiln nil)))
