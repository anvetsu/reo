(ns motto.global-env
  (:require [motto.compile]
            [motto.dbconn]
            [motto.csv]
            [motto.lib.burrow]
            [motto.lib.dt]
            [motto.lib.list]
            [motto.lib.tab]))

(defn make-eval []
  (eval
   '(do
      (def -neg- -)
      (def dict hash-map)

      (def parse read-string)

      (def -+- motto.lib.burrow/add)
      (def --- motto.lib.burrow/sub)
      (def -*- motto.lib.burrow/mul)
      (def -div- motto.lib.burrow/div)
      (def -=- motto.lib.burrow/eq)
      (def ->- motto.lib.burrow/gt)
      (def -<- motto.lib.burrow/lt)
      (def ->=- motto.lib.burrow/gteq)
      (def -<=- motto.lib.burrow/lteq)
      (def <> motto.lib.burrow/neq)
      (def big motto.lib.burrow/big)
      (def sml motto.lib.burrow/small)

      (def dt motto.lib.dt/dt)
      (def sdt motto.lib.dt/sdt)
      (def now motto.lib.dt/now)
      (def dt_add motto.lib.dt/add)
      (def dt_get motto.lib.dt/getf)

      (def cf motto.compile/compile-file)

      (def -take- motto.lib.list/-take-)
      (def -conj- motto.lib.list/-conj-)
      (def -fold- motto.lib.list/-fold-)
      (def -map- motto.lib.list/-map-)
      (def -filter- motto.lib.list/-filter-)
      (def fold-incr motto.lib.list/fold-incr)
      (def fold-times motto.lib.list/fold-times)
      (def sum motto.lib.list/sum)
      (def dif motto.lib.list/diff)
      (def prd motto.lib.list/prd)
      (def qt motto.lib.list/-quot-)
      (def mx motto.lib.list/-max-)
      (def mn motto.lib.list/-min-)
      (def sums motto.lib.list/sums)
      (def difs motto.lib.list/diffs)
      (def prds motto.lib.list/prds)
      (def qts motto.lib.list/quots)
      (def mxs motto.lib.list/maxs)
      (def mns motto.lib.list/mins)
      (def til motto.lib.list/til)
      (def twins motto.lib.list/twins)
      (def collect motto.lib.list/collect)
      (def count_f motto.lib.list/count-for)
      (def count_eq motto.lib.list/count-eq)

      (def tab motto.lib.tab/mktab)
      (def cols motto.lib.tab/cols)
      (def top motto.lib.tab/top)
      (def group motto.lib.tab/group)

      (def data_source motto.dbconn/data-source)
      (def open motto.dbconn/open)
      (def close motto.dbconn/close)
      (def stmt motto.dbconn/stmt)
      (def qry motto.dbconn/qry)
      (def cmd motto.dbconn/cmd)

      (def csv motto.csv/csv)
      (def csv_fmt motto.csv/fmt)
      (def csv_ahdr motto.csv/with-auto-header)
      (def csv_hdr motto.csv/with-header)
      (def csv_delim motto.csv/with-delim)
      (def csv_rd motto.csv/rd)

      (fn [expr] (eval expr)))))
