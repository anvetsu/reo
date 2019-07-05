(ns reo.math)

(defn pow [x y & ys]
  (let [r (Math/pow x y)
        rs (seq (map #(Math/pow x %) ys))]
    (if (seq rs)
      (conj rs r)
      r)))

(defn ceil [^Double x]
  (if (seqable? x)
    (map #(Math/ceil %) x)
    (Math/ceil x)))

(defn floor [^Double x]
  (if (seqable? x)
    (map #(Math/floor %) x)
    (Math/floor x)))

(defn sqrt [x]
  (Math/sqrt x))

(defn nthrt [x n]
  (Math/pow Math/E (/ (Math/log x) n)))

(defn abs [^Double x]
  (Math/abs x))

(defn acos [^Double x]
  (Math/acos x))

(defn asin [^Double x]
  (Math/asin x))

(defn atan [^Double x]
  (Math/atan x))

(defn atan2 [^Double x ^Double y]
  (Math/atan2 x y))

(defn cbrt [^Double x]
  (Math/cbrt x))

(defn cos [^Double x]
  (Math/cos x))

(defn cosh [^Double x]
  (Math/cosh x))

(defn exp [^Double x]
  (Math/exp x))

(defn expm1 [^Double x]
  (Math/expm1 x))

(defn hypot [^Double x ^Double y]
  (Math/hypot x y))

(defn log
  ([^Double x]
   (Math/log x))
  ([^Double base ^Double x]
   (/ (Math/log x) (Math/log base))))

(defn log10 [^Double x]
  (Math/log10 x))

(defn log1p [^Double x]
  (Math/log1p x))

(defn sin [^Double x]
  (Math/sin x))

(defn sinh [^Double x]
  (Math/sinh x))

(defn tan [^Double x]
  (Math/tan x))

(defn tanh [^Double x]
  (Math/tanh x))

(defn degrees [^Double x]
  (Math/toDegrees x))

(defn radians [^Double x]
  (Math/toRadians x))

(defn nan? [^Double x]
  (Double/isNaN x))

(defn inf? [^Double x]
  (Double/isInfinite x))
