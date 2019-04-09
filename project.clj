(defproject motto "0.1.0-SNAPSHOT"
  :main motto.core
  :aot [motto.core]
  :dependencies [[org.clojure/clojure "1.9.0"]
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
