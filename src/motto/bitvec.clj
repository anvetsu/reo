(ns motto.bitvec
  (:import [java.util BitSet]))

(defn bitvec? [obj]
  (instance? BitSet obj))

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

(defn from-str [s]
  (from-seq (map #(if (= % \0) false true) s)))

(defn bitvec? [obj]
  (instance? BitSet obj))

(defn size [^BitSet bv]
  (.size bv))

(defn length [^BitSet bv]
  (.length bv))

(defn at [^BitSet bv i]
  (.get bv i))

(defn land
  ([^BitSet bv1 ^BitSet bv2 clone?]
   (let [bv (if clone? (.clone bv1) bv1)]
     (.and bv bv2)
     bv))
  ([^BitSet bv1 ^BitSet bv2]
   (land bv1 bv2 true)))

(defn and-not
  ([^BitSet bv1 ^BitSet bv2 clone?]
   (let [bv (if clone? (.clone bv1) bv1)]
     (.andNot bv bv2)
     bv))
  ([^BitSet bv1 ^BitSet bv2]
   (and-not bv1 bv2 true)))

(defn flip [^BitSet bv i]
  (.flip bv i)
  bv)

(defn intersects? [^BitSet bv1 ^BitSet bv2]
  (.intersects bv1 bv2))

(defn lor
  ([^BitSet bv1 ^BitSet bv2 clone?]
   (let [bv (if clone? (.clone bv1) bv1)]
     (.or bv bv2)
     bv))
  ([^BitSet bv1 ^BitSet bv2]
   (lor bv1 bv2 true)))

(defn xor
  ([^BitSet bv1 ^BitSet bv2 clone?]
   (let [bv (if clone? (.clone bv1) bv1)]
     (.xor bv bv2)
     bv))
  ([^BitSet bv1 ^BitSet bv2]
   (xor bv1 bv2 true)))

(defn for-each [f ^BitSet bv]
  (let [len (.length bv)]
    (loop [i 0]
      (when (< i len)
        (f (.get bv i))
        (recur (inc i))))))

(defn flip-all [^BitSet bv]
  (.flip bv 0 (.length bv))
  bv)

(defn _band
  ([a b clone?]
   (land a b clone?))
  ([a b]
   (land a b true)))

(defn _bor
  ([a b clone?]
   (lor a b clone?))
  ([a b]
   (lor a b true)))

(defn _bxor
  ([a b clone?]
   (xor a b clone?))
  ([a b]
   (xor a b true)))

(defn _band-not
  ([a b clone?]
   (and-not a b clone?))
  ([a b]
   (and-not a b true)))
