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
