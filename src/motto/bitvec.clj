(ns motto.bitvec
  (:import [java.util BitSet]))

(defn from-seq [xs]
  (let [bv (BitSet.)]
    (loop [xs xs, i 0]
      (when (seq xs)
        (.set bv i (first xs))
        (recur (rest xs) (inc i))))
    bv))

(defn to-seq
  ([^BitSet bv]
   (let [len (.length bv)]
     (loop [i 0, rs []]
       (if (>= i len)
         rs
         (recur (inc i)
                (conj rs (.get bv i)))))))
  ([^BitSet bv len]
   (let [bs (to-seq bv)
         c (count bs)]
     (if (< c len)
       (loop [i c, rs bs]
         (if (< i len)
           (recur (inc i) (conj rs false))
           rs))
       bs))))

(defn bitvec? [obj]
  (instance? BitSet obj))

(defn size [^BitSet bv]
  (.size bv))

(defn length [^BitSet bv]
  (.length bv))

(defn on? [^BitSet bv i]
  (.get bv i))

(def off? (comp on?))

(defn land [^BitSet bv1 ^BitSet bv2]
  (.and bv1 bv2))

(defn and-not [^BitSet bv1 ^BitSet bv2]
  (.andNot bv1 bv2))

(defn flip [^BitSet bv i]
  (.flip bv i))

(defn intersects? [^BitSet bv1 ^BitSet bv2]
  (.intersects bv1 bv2))

(defn lor [^BitSet bv1 ^BitSet bv2]
  (.or bv1 bv2))

(defn xor [^BitSet bv1 ^BitSet bv2]
  (.xor bv1 bv2))

(defn for-each [f ^BitSet bv]
  (let [len (.length bv)]
    (loop [i 0]
      (when (< i len)
        (f (.get bv i))
        (recur (inc i))))))
