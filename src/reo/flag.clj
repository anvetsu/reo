(ns reo.flag)

(def ^:private verbose (atom false))

(defn verbose! []
  (reset! verbose (not @verbose)))

(defn verbose? [] @verbose)
