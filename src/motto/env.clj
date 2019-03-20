(ns motto.env
  (:require [motto.lib.list :as mll]
            [motto.lib.arith :as mla]))

(defn make
  ([bindings]
   {:parent nil
    :bindings bindings})
  ([]
   (make {})))

(defn amend [env var val]
  (let [b (:bindings env)]
    (assoc env :bindings (assoc b var val))))

(defn lookup [env var]
  (loop [env env]
    (let [b (:bindings env)]
      (if-let [val (get b var)]
        val
        (when-let [p (:parent env)]
          (recur p))))))

(defn global []
  (let [bindings {'+ mla/add '- mla/sub
                  '* mla/mul '/ mla/div
                  '= =
                  'til mll/til}]
    (make bindings)))
