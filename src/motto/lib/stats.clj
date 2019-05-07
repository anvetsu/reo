(ns motto.lib.stats
  (:require [incanter.stats :as s]))

(defn cdf-beta
  ([x options]
   (let [alpha (get options 'alpha)
         beta (get options 'beta)
         lower-tail (get options 'lower_tail true)]
     (s/cdf-beta x :alpha alpha :beta beta :lower-tail? lower-tail)))
  ([x] (s/cdf-beta x)))

(defn cdf-binomial
  ([x options]
   (let [size (get options 'size)
         probe (get options 'probe)
         lower-tail (get options 'lower_tail true)]
     (s/cdf-binomial x :size size :probe probe :lower-tail? lower_tail)))
  ([x] (s/cdf-binomial x)))

(defn cdf-chisq
  ([x options]
   (let [df (get options 'df)
         lower-tail (get options 'lower_tail true)]
     (s/cdf-chisq x :df df :lower-tail? lower_tail)))
  ([x] (s/cdf-chisq x)))

(defn cdf-exp
  ([x options]
   (let [rate (get options 'date)]
     (s/cdf-exp x :rate rate)))
  ([x] (s/cdf-exp x)))

(defn cdf-f
  ([x options]
   (let [df1 (get options 'df1)
         df2 (get options 'df2)
         lower-tail (get options 'lower_tail true)]
     (s/cdf-f x :df1 df1 :df2 df2 :lower-tail? lower_tail)))
  ([x] (s/cdf-f x)))

(defn cdf-gamma
  ([x options]
   (let [shape (get options 'shape)
         scale (get options 'scale)
         rate (get options 'rate)
         lower-tail (get options 'lower_tail true)]
     (s/cdf-gamma x :shape shape :scale scale :rate rate :lower-tail? lower_tail)))
  ([x] (s/cdf-gamma x)))
