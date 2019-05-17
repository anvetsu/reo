(ns motto.lib.sys)

(defn exit
  ([n] (System/exit n))
  ([] (System/exit 0)))
