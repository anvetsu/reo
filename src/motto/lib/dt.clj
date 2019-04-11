(ns motto.lib.dt
  (:import [java.text SimpleDateFormat]
           [java.util Calendar GregorianCalendar]))

(def ^:private default-dfmt (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss"))

(defn dt
  ([s fmt]
   (let [dfmt (if fmt
                (SimpleDateFormat. fmt)
                default-dfmt)
         cal (GregorianCalendar.)]
     (.setTime cal (.parse dfmt s))
     cal))
  ([s] (dt s nil)))

(defn sdt
  ([^Calendar d fmt]
   (let [dfmt (if fmt
                (SimpleDateFormat. fmt)
                default-dfmt)]
     (.setTimeZone dfmt (.getTimeZone d))
     (.format dfmt (.getTime d))))
  ([d] (sdt d nil)))

(defn now [] (GregorianCalendar.))

(defn- field->int [field]
  (case (keyword field)
    :y Calendar/YEAR
    :M Calendar/MONTH
    :d Calendar/DAY_OF_MONTH
    :D Calendar/DAY_OF_YEAR
    :H Calendar/HOUR_OF_DAY
    :m Calendar/MINUTE
    :s Calendar/SECOND
    Calendar/DAY_OF_YEAR))

(defn add [^Calendar dt field amount]
  (let [f (field->int field)
        dt2 (.clone dt)]
    (.add dt2 f amount)
    dt2))

(defn getf [^Calendar dt field]
  (let [f (field->int field)
        v (.get dt f)]
    (if (= f Calendar/MONTH) (+ v 1) v)))
