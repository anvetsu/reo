(ns motto.dbconn
  (:require [motto.util :as u])
  (:import [java.sql Connection]
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
