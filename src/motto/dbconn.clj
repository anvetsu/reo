(ns motto.dbconn
  (:require [motto.util :as u])
  (:import [java.sql Connection ResultSet ResultSetMetaData
            Statement Types]
           [javax.sql DataSource]
           [com.mchange.v2.c3p0 ComboPooledDataSource
            PooledDataSource DataSources]))

(def ^:private data-sources (atom {}))

(defn- cached-ds [config]
  (let [k (:jdbc-url config)]
    (get @data-sources k)))

(defn- cache-ds! [ds config]
  (let [k (:jdbc-url config)]
    (swap! data-sources assoc k ds)))

(defn- mk-data-source [config]
  (if-let [ds (cached-ds config)]
    ds
    (let [^ComboPooledDataSource ds (ComboPooledDataSource.)]
      (if-let [url (:jdbc-url config)]
        (.setJdbcUrl ds url)
        (u/ex (str "dbconn: open: jdbc-url is required")))
      (u/for-data config {:driver-class  #(.setDriverClass ds %)
                          :user #(.setUser ds %)
                          :password #(.setPassword ds %)
                          :min-pool-size #(.setMinPoolSize ds %)
                          :max-pool-size #(.setMaxPoolSize ds %)
                          :acquire-increment #(.setAcquireIncrement ds %)})
      (cache-ds! ds config)
      ds)))

(defn data-source [config]
  (into {} (map (fn [[k v]] [(keyword k) v])) config))

(defn cleanup []
  (doseq [^PooledDataSource ds (vals @data-sources)]
    (.close ds))
  (reset! data-sources {})
  true)

(defn open
  ([^DataSource ds user password]
   (if (and user password)
     (.getConnection ds user password)
     (.getConnection ds)))
  ([ds]
   (open ds nil nil)))

(defn close [^Connection conn]
  (.close conn))

(def conn (let [ds (mk-data-source {:jdbc-url "jdbc:hsqldb:file:db/mottodb"
                                    :user "SA"
                                    :password ""})]
            (open ds)))

(defn stmt [^Connection conn ^String sql]
  (.prepareStatement conn sql))

(defn- get-int [^ResultSet rs]
  (.getInt rs))

(defn- get-boolean [^ResultSet rs]
  (.getBoolean rs))

(defn- get-int [^ResultSet rs]
  (.getInt rs))

(defn- get-float [^ResultSet rs]
  (.getFloat rs))

(defn- get-double [^ResultSet rs]
  (.getDouble rs))

(defn- get-decimal [^ResultSet rs]
  (.getDecimal rs))

(defn- get-string [^ResultSet rs]
  (.getString rs))

(defn- col-type [^ResultSetMetaData rmd i]
  (case (.getColumnType rmd i)
    Types/BIT get-int
    Types/BOOLEAN get-boolean
    Types/INTEGER get-int
    Types/FLOAT get-float
    Types/DOUBLE get-double
    Types/DECIMAL get-decimal
    get-string))

(defn- column-types [^ResultSetMetaData rmd]
  (let [c (.getColumnCount rmd)]
    (loop [i 1, tps []]
      (if (<= i c)
        (recur (inc i) (conj tps [i (col-type rmd i)]))
        tps))))

(defn- fetch-row [^ResultSet rs col-types]
  (map (fn [[_ f]] (f rs)) col-types))

(defn query [^Statement stmt]
  (let [^ResultSet rs (.executeQuery stmt)
        ^ResultSetMetaData rmd (.getMetaData rs)
        col-cnt (.getColumnCount rmd)
        col-types (column-types rmd)]
    (loop [rows (repeat col-cnt [])]
      (if (.next rs)
        (recur (u/spread rows (fetch-row rs col-types)))
        rows))))
