(defproject motto "0.1.0-SNAPSHOT"
  :main motto.core
  :aot [motto.core]
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/math.combinatorics "0.1.5"]
                 [incanter "1.9.3"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.1"]
                 [cheshire "5.8.1"]
                 [org.hsqldb/hsqldb "2.4.1"]
                 [com.mchange/c3p0 "0.9.5.4"]
                 [com.taoensso/nippy "2.14.0"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.apache.commons/commons-csv "1.6"]
                 [org.slf4j/slf4j-log4j12 "1.7.9"]
                 [log4j/log4j "1.2.17"
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jmdk/jmxtools
                               com.sun.jmx/jmxri]]])
