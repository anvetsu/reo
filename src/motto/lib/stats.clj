(ns motto.lib.stats
  (:require [incanter.stats :as s]
            [incanter.core :as ic]
            [motto.tab :as tab]
            [motto.util :as u]))

(defn cdf-beta
  ([x options]
   (let [alpha (get options 'alpha)
         beta (get options 'beta)
         lower-tail (get options 'lowertail true)]
     (s/cdf-beta x :alpha alpha :beta beta :lower-tail? lower-tail)))
  ([x] (s/cdf-beta x)))

(defn cdf-binomial
  ([x options]
   (let [size (get options 'size)
         prob (get options 'prob)
         lower-tail (get options 'lowertail true)]
     (s/cdf-binomial x :size size :prob prob :lower-tail? lower-tail)))
  ([x] (s/cdf-binomial x)))

(defn cdf-chisq
  ([x options]
   (let [df (get options 'df)
         lower-tail (get options 'lowertail true)]
     (s/cdf-chisq x :df df :lower-tail? lower-tail)))
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
         lower-tail (get options 'lowertail true)]
     (s/cdf-f x :df1 df1 :df2 df2 :lower-tail? lower-tail)))
  ([x] (s/cdf-f x)))

(defn cdf-gamma
  ([x options]
   (let [shape (get options 'shape)
         scale (get options 'scale)
         rate (get options 'rate)
         lower-tail (get options 'lowertail true)]
     (s/cdf-gamma x :shape shape :scale scale
                  :rate rate :lower-tail? lower-tail)))
  ([x] (s/cdf-gamma x)))

(defn cdf-neg-binomial
  ([x options]
   (let [size (get options 'size)
         prob (get options 'prob)
         lower-tail (get options 'lowertail true)]
     (s/cdf-neg-binomial x :size size :prob prob :lower-tail? lower-tail)))
  ([x] (s/cdf-neg-binomial x)))

(defn cdf-normal
  ([x options]
   (let [mean (get options 'mean)
         sd (get options 'sd)]
     (s/cdf-normal x :mean mean :sd sd)))
  ([x] (s/cdf-normal x)))

(defn cdf-poisson
  ([x options]
   (let [lambda (get options 'lambda)
         lower-tail (get options 'lowertail true)]
     (s/cdf-poisson x :lambda lambda :lower-tail? lower-tail)))
  ([x] (s/cdf-poisson x)))

(defn cdf-t
  ([x options]
   (let [df (get options 'df)]
     (s/cdf-t x :df df)))
  ([x] (s/cdf-t x)))

(defn cdf-uniform
  ([x options]
   (let [min (get options 'min)
         max (get options 'max)]
     (s/cdf-uniform x :min min :max max)))
  ([x] (s/cdf-uniform x)))

(defn cdf-weibull
  ([x options]
   (let [shape (get options 'shape)
         scale (get options 'scale)]
     (s/cdf-weibull x :shape shape :scale scale)))
  ([x] (s/cdf-weibull x)))

(defn pdf-beta
  ([x options]
   (let [alpha (get options 'alpha)
         beta (get options 'beta)]
     (s/pdf-beta x :alpha alpha :beta beta)))
  ([x] (s/pdf-beta x)))

(defn pdf-binomial
  ([x options]
   (let [size (get options 'size)
         prob (get options 'prob)]
     (s/pdf-binomial x :size size :prob prob)))
  ([x] (s/pdf-binomial x)))

(defn pdf-chisq
  ([x options]
   (let [df (get options 'df)]
     (s/pdf-chisq x :df df)))
  ([x] (s/pdf-chisq x)))

(defn pdf-exp
  ([x options]
   (let [rate (get options 'date)]
     (s/pdf-exp x :rate rate)))
  ([x] (s/pdf-exp x)))

(defn pdf-f
  ([x options]
   (let [df1 (get options 'df1)
         df2 (get options 'df2)]
     (s/pdf-f x :df1 df1 :df2 df2)))
  ([x] (s/pdf-f x)))

(defn pdf-gamma
  ([x options]
   (let [shape (get options 'shape)
         scale (get options 'scale)
         rate (get options 'rate)]
     (s/pdf-gamma x :shape shape :scale scale
                  :rate rate)))
  ([x] (s/pdf-gamma x)))

(defn pdf-neg-binomial
  ([x options]
   (let [size (get options 'size)
         prob (get options 'prob)]
     (s/pdf-neg-binomial x :size size :prob prob)))
  ([x] (s/pdf-neg-binomial x)))

(defn pdf-normal
  ([x options]
   (let [mean (get options 'mean)
         sd (get options 'sd)]
     (s/pdf-normal x :mean mean :sd sd)))
  ([x] (s/pdf-normal x)))

(defn pdf-poisson
  ([x options]
   (let [lambda (get options 'lambda)]
     (s/pdf-poisson x :lambda lambda)))
  ([x] (s/pdf-poisson x)))

(defn pdf-t
  ([x options]
   (let [df (get options 'df)]
     (s/pdf-t x :df df)))
  ([x] (s/pdf-t x)))

(defn pdf-uniform
  ([x options]
   (let [min (get options 'min)
         max (get options 'max)]
     (s/pdf-uniform x :min min :max max)))
  ([x] (s/pdf-uniform x)))

(defn pdf-weibull
  ([x options]
   (let [shape (get options 'shape)
         scale (get options 'scale)]
     (s/pdf-weibull x :shape shape :scale scale)))
  ([x] (s/pdf-weibull x)))

(defn chisq-test
  ([x options]
   (let [y (get options 'y)
         correct (get options 'correct true)
         table (get options 'table)
         probs (get options 'probs)
         freq (get options 'freq)]
     (s/chisq-test :x x :y y :correct correct
                   :table table :probs probs
                   :freq freq)))
  ([x] (s/chisq-test :x x)))

(defn distance [tp x & xs]
  (let [y (first xs)]
    (case tp
      euclidean (s/euclidean-distance x y)
      chebyshev (s/chebyshev-distance x y)
      hamming (s/hamming-distance x y)
      jaccard (s/jaccard-distance x y)
      levenshtein (s/levenshtein-distance x y)
      lee (s/lee-distance x y (second xs))
      manhattan (s/manhattan-distance x y)
      minkowski (s/minkowski-distance x y (second xs))
      normalized_kendall_tau (s/normalized-kendall-tau-distance x y)
      mahalanobis (s/mahalanobis-distance x :y (get y 'y)
                                          :W (get y 'W)
                                          :centroid (get y 'centroid))
      (u/ex (str "stats: invalid distance type: " tp)))))

(defn sweep
  ([x options]
   (let [stat (get options 'stat)
         fun (get options 'fun)]
     (s/sweep x :stat stat :fun fun)))
  ([x] (s/sweep x)))

(defn cdf [tag & args]
  (let [f (case tag
            beta cdf-beta
            binomial cdf-binomial
            chisq cdf-chisq
            empirical s/cdf-empirical
            exp cdf-exp
            f cdf-f
            gamma cdf-gamma
            negbinomial cdf-neg-binomial
            normal cdf-normal
            poisson cdf-poisson
            t cdf-t
            uniform cdf-uniform
            weibull cdf-weibull
            (u/ex (str "stats: invalid tag: " tag)))]
    (apply f args)))

(defn pdf [tag & args]
  (let [f (case tag
            beta pdf-beta
            binomial pdf-binomial
            chisq pdf-chisq
            exp pdf-exp
            f pdf-f
            gamma pdf-gamma
            negbinomial pdf-neg-binomial
            normal pdf-normal
            poisson pdf-poisson
            t pdf-t
            uniform pdf-uniform
            weibull pdf-weibull
            (u/ex (str "stats: invalid tag: " tag)))]
    (apply f args)))

(defn summary [xs]
  (let [dat (if (vector? xs) (tab/mkt ['NA] [xs]) xs)]
    (map u/keys->nsyms (s/summary (tab/maybe-dset dat)))))
