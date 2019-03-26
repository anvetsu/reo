(ns motto.env
  (:require [motto.util :as u]
            [motto.lib.list :as mll]
            [motto.lib.burrow :as mlb]))

(defn- ex [msg]
  (u/ex (str "env: " msg)))

(defn make
  ([parent bindings]
   {:parent parent
    :bindings bindings})
  ([bindings]
   (make nil bindings))
  ([]
   (make {})))

(defn link [e1 e2]
  (assoc e1 :parent e2))

(defn amend [env var val]
  (let [b (:bindings env)]
    (assoc env :bindings (assoc b var val))))

(defn amend-all [env vars vals]
  (if (and (seq vars) (seq vals))
    (let [l1 (count vars)
          l2 (count vals)]
      (cond
        (> l1 l2) (ex (str "more variables than values: " vars))
        (> l2 l1) (ex (str "more values than variables: " vals))
        :else
        (let [bs (into {} (map vector vars vals))]
          (assoc env :bindings (merge (:bindings env) bs)))))
    env))

(defn lookup [env var]
  (loop [env env]
    (let [b (:bindings env)]
      (let [val (get b var)]
        (if-not (nil? val)
          val
          (when-let [p (:parent env)]
            (recur p)))))))

(defn global []
  (let [bindings {'+ mlb/add '- mlb/sub
                  '* mlb/mul '/ mlb/div
                  '= mlb/eq '> mlb/gt
                  '< mlb/lt '>= mlb/gteq
                  '<= mlb/lteq '<> mlb/neq
                  '-get- get '-neg- - 'til
                  mll/til}]
    (make bindings)))
