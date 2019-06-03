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
  ([s]
   (if (instance? java.sql.Date s)
     (let [cal (Calendar/getInstance)]
       (.setTime cal s)
       cal)
     (dt s nil))))

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
    :dW Calendar/DAY_OF_WEEK
    :dM Calendar/DAY_OF_WEEK_IN_MONTH
    :H Calendar/HOUR_OF_DAY
    :m Calendar/MINUTE
    :s Calendar/SECOND
    Calendar/DAY_OF_YEAR))

(defn add [^Calendar dt field amount]
  (let [f (field->int field)
        dt2 (.clone dt)]
    (.add dt2 f amount)
    dt2))

(def ^:private monnames ['JANUARY 'FEBRUARY
                         'MARCH 'APRIL 'MAY
                         'JUNE 'JULY 'AUGUST
                         'SEPTEMBER 'OCTOBER
                         'NOVEMBER 'DECEMBER])

(def ^:private daynames ['SUNDAY 'MONDAY 'TUESDAY
                         'WEDNESDAY 'THURSDAY 'FRIDAY
                         'SATURDAY])

(defn getf [^Calendar dt field]
  (let [f (field->int field)
        v (.get dt f)]
    (cond
      (= f Calendar/MONTH) (nth monnames v)
      (= f Calendar/DAY_OF_WEEK) (nth daynames (dec v))
      :else v)))
