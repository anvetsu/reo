(ns motto.dbconn
  (:require [motto.util :as u]
            [motto.lib.tab :as t])
  (:import [java.sql Connection ResultSet ResultSetMetaData
            PreparedStatement Statement Types]
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

(def ^:private default-ds-config {:jdbc-url "jdbc:hsqldb:file:db/mottodb"
                                  :user "SA"
                                  :password ""})
(def ^:private ds (mk-data-source default-ds-config))

(defn data-source
  ([config]
   (if (seq config)
     (into {} (map (fn [[k v]] [(keyword k) v])) config)
     ds))
  ([] ds))

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

(defn stmt [^Connection conn ^String sql]
  (.prepareStatement conn sql))

(defn- get-int [^ResultSet rs ^Integer i]
  (.getInt rs i))

(defn- get-boolean [^ResultSet rs ^Integer i]
  (.getBoolean rs i))

(defn- get-int [^ResultSet rs ^Integer i]
  (.getInt rs i))

(defn- get-float [^ResultSet rs ^Integer i]
  (.getFloat rs i))

(defn- get-double [^ResultSet rs ^Integer i]
  (.getDouble rs i))

(defn- get-decimal [^ResultSet rs ^Integer i]
  (.getDecimal rs i))

(defn- get-string [^ResultSet rs ^Integer i]
  (or (.getString rs i) ""))

(def ^:private date-null (java.sql.Date. 0))
(def ^:private time-null (java.sql.Time. 0))
(def ^:private timestamp-null (java.sql.Timestamp. 0))

(defn- get-date [^ResultSet rs ^Integer i]
  (or (.getDate rs i) date-null))

(defn- get-time [^ResultSet rs ^Integer i]
  (or (.getTime rs i) time-null))

(defn- get-timestamp [^ResultSet rs ^Integer i]
  (or (.getTimeStamp rs i) timestamp-null))

(defn- col-type [^ResultSetMetaData rmd i]
  (let [t (.getColumnType rmd i)]
    (cond
      (= t Types/BIT) get-int
      (= t Types/BOOLEAN) get-boolean
      (= t Types/INTEGER) get-int
      (= t Types/FLOAT) get-float
      (= t Types/DOUBLE) get-double
      (= t Types/DECIMAL) get-decimal
      (= t Types/TIME) get-time
      (= t Types/DATE) get-date
      (= t Types/TIMESTAMP) get-timestamp
      :else get-string)))

(defn- column-infos [^ResultSetMetaData rmd]
  (let [c (.getColumnCount rmd)]
    (loop [i 1, tps []]
      (if (<= i c)
        (recur (inc i) (conj tps [i (.getColumnName rmd i) (col-type rmd i)]))
        tps))))

(defn- fetch-row [^ResultSet rs col-types]
  (map (fn [[i _ f]] (f rs i)) col-types))

(defn- column-names [col-infos]
  (into [] (map #(symbol (.toLowerCase (second %))) col-infos)))

(defn- set-params! [^PreparedStatement stmt params]
  (loop [i 1, params params]
    (when (seq params)
      (let [v (first params)]
        (cond
          (int? v) (.setInt stmt i v)
          (integer? v) (.setLong stmt i v)
          (float? v) (.setFloat stmt i v)
          (double? v) (.setDouble stmt i v)
          (boolean? v) (.setBoolean stmt i v)
          (string? v) (.setString stmt i v)
          (symbol? v) (.setString stmt i (name v))
          :else (u/ex (str "dbconn: invalid value: " v))))
      (recur (inc i) (rest params)))))

(defn query
  ([^Statement stmt params]
   (when (seq params)
     (set-params! stmt params))
   (let [^ResultSet rs (.executeQuery stmt)
         ^ResultSetMetaData rmd (.getMetaData rs)
         col-cnt (.getColumnCount rmd)
         col-infos (column-infos rmd)
         data
         (loop [rows (into [] (repeat col-cnt []))]
           (if (.next rs)
             (recur (u/spread rows (fetch-row rs col-infos)))
             rows))]
     (t/mktab (column-names col-infos) data)))
  ([^Statement stmt]
   (query stmt nil)))

(defn command
  ([^Statement stmt params]
   (when (seq params)
     (set-params! stmt params))
   (.executeUpdate stmt))
  ([^Statement stmt]
   (command stmt nil)))

(defn- qry-cmd
  [s params f]
  (let [mkstmt? (string? s)
        conn (when mkstmt?
               (open ds))]
    (try
      (let [^Statement stmt (if mkstmt?
                              (stmt conn s)
                              s)]
        (f stmt params))
      (finally
        (when conn
          (close conn))))))

(defn qry
  ([s params]
   (qry-cmd s params query))
  ([s]
   (qry-cmd s nil query)))

(defn cmd
  ([s params]
   (qry-cmd s params command))
  ([s]
   (qry-cmd s nil command)))
