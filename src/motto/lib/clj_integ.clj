(ns motto.lib.clj-integ
  (:require [clojure.string :as s]))

(defn clj-refer
  ([s filters]
   (let [s (if (symbol? s) s (symbol s))
         ex (get filters 'exclude)
         only (get filters 'only)
         rn (get filters 'rename)]
     (refer s :only only :exclude ex :rename rn)))
  ([s] (clj-refer s nil)))

(def ^:private reserved-syms #{'as 'only 'rename 'exclude
                               'refer 'reload 'reload_all
                               'verbose})

(defn- reserved-sym? [x]
  (some #{x} reserved-syms))

(defn- normalize-sym [s]
  (str (s/replace (name s) #"_" "-")))

(defn- preproc-declspec [spec]
  (let [fs (first spec)
        s (if (symbol? fs) fs (symbol fs))]
    (loop [spec (rest spec)
           rspec [s]]
      (if (seq spec)
        (let [fs (first spec)
              s (if (reserved-sym? fs)
                  (keyword (normalize-sym fs))
                  fs)]
          (recur (rest spec)
                 (conj rspec s)))
        rspec))))

(defn clj-use [& args]
  (apply use (map preproc-declspec args)))

(defn clj-require [& args]
  (apply require (map preproc-declspec args)))
