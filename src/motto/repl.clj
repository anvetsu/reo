(ns motto.repl
  (:require [clojure.string :as str]
            [motto.flag :as flag]
            [motto.config :as config]
            [motto.global-env :as env]
            [motto.eval-native :as e]
            [motto.compile :as c]
            [motto.expr-io :as eio]))

(defn- multiln-prompt []
  (print (config/prompt2))
  (flush))

(defn- fmt-errmsg [^String s]
  (let [^String p "class "
        s (if (.startsWith s p)
            (.substring s (.length p))
            s)]
    (str/replace s #"java." "")))

(defn- force-err-msg [ex]
  (fmt-errmsg
   (or (:cause (Throwable->map ex))
       (.getMessage ex)
       (str (type ex)))))

(defn repl []
  (let [eval (env/make-eval)]
    (loop []
      (do (print (config/prompt)) (flush)
          (try
            (let [s (eio/read-multiln multiln-prompt)]
              (if s
                (when (seq s)
                  (let [exprs (c/compile-string s)
                        val (e/evaluate-all exprs eval)]
                    (eio/writeln val)))
                (System/exit 0)))
            (catch Exception ex
              (println (str "ERROR: " (force-err-msg ex)))
              (when (flag/verbose?)
                (.printStackTrace ex))))
          (recur)))))
