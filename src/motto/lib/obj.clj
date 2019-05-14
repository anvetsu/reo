(ns motto.lib.obj
  (:require [slingshot.slingshot :as ss]
            [motto.tab :as tab]))

(defn- =? [x y]
  (= 0 (compare x y)))

(defn- <? [x y]
  (neg? (compare x y)))

(defn- >? [x y]
  (pos? (compare x y)))

(defn- <=? [x y]
  (let [i (compare x y)]
    (or (= 0 i) (neg? i))))

(defn- >=? [x y]
  (let [i (compare x y)]
    (or (= 0 i) (pos? i))))

(defn- cmpr [predic x y & ys]
  (try
    (if (predic x y)
      (loop [ys (conj ys y)]
        (if (and (seq ys) (seq (rest ys)))
          (if (predic (first ys) (second ys))
            (recur (rest ys))
            false)
          true))
      false)
    (catch ClassCastException _
      false)))

(def eq (partial cmpr =?))
(def lt (partial cmpr <?))
(def gt (partial cmpr >?))
(def lteq (partial cmpr <=?))
(def gteq (partial cmpr >=?))

(defn size [x]
  (or (tab/size x) (count x)))

(defn void? [x] (= :void x))

(defn ex [obj]
  (ss/throw+ {:type :motto-ex :obj obj}))

(defn with-ex [handler f]
  (ss/try+
   (f)
   (catch [:type :motto-ex] {:keys [obj]}
     (handler obj))
   (catch Object obj
     (handler obj))))
