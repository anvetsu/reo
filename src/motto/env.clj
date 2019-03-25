(ns motto.env
  (:require [motto.lib.list :as mll]
            [motto.lib.arith :as mla]))

(defn- ex [msg]
  (throw (Exception. (str "env: " msg))))

(defn make
  ([parent bindings]
   {:parent parent
    :bindings bindings})
  ([bindings]
   (make nil bindings))
  ([]
   (make {})))

(defn amend [env var val]
  (let [b (:bindings env)]
    (assoc env :bindings (assoc b var val))))

(defn lookup [env var]
  (loop [env env]
    (let [b (:bindings env)]
      (let [val (get b var)]
        (if-not (nil? val)
          val
          (when-let [p (:parent env)]
            (recur p)))))))

(defn extended [env vars vals]
  (if (and (seq vars) (seq vals))
    (let [l1 (count vars)
          l2 (count vals)]
      (cond
        (> l1 l2) (ex (str "more variables than values: " vars))
        (> l2 l1) (ex (str "more values than variables: " vals))
        :else
        (let [bs (into {} (map vector vars vals))]
          (make env bs))))
    env))

(defn global []
  (let [bindings {'+ mla/add '- mla/sub
                  '* mla/mul '/ mla/div
                  '= = '> > '< < '>= >=
                  '<= <= '<> mla/not-eq
                  '-get- get
                  '-neg- - 'til mll/til}]
    (make bindings)))
