(ns motto.global-env
  (:require [motto.compile]
            [motto.dbconn]
            [motto.bitvec]
            [motto.lib.csv]
            [motto.lib.burrow]
            [motto.lib.str]
            [motto.lib.math]
            [motto.lib.dt]
            [motto.lib.list]
            [motto.lib.tab]
            [motto.lib.json]
            [motto.lib.http]))

(defn make-eval []
  (eval
   '(do
      (def -neg- -)
      (def dict hash-map)
      (def randi rand-int)
      (def parse read-string)
      (def fst first)
      (def snd second)

      (def str_join motto.lib.str/join)
      (def str_split motto.lib.str/split)

      (def bits motto.bitvec/from-seq)
      (def bools motto.bitvec/to-seq)
      (def count_bits motto.bitvec/length)
      (def bit_on motto.bitvec/on?)
      (def bit_off motto.bitvec/off?)
      (def bits_and motto.bitvec/land)
      (def bits_or motto.bitvec/lor)
      (def bits_and_not motto.bitvec/and-not)
      (def bits_xor motto.bitvec/xor)
      (def bits_intersects motto.bitvec/intersects?)
      (def bit_flip motto.bitvec/flip)
      (def bits_iter motto.bitvec/for-each)

      (def setu clojure.set/union)
      (def seti clojure.set/intersection)
      (def setd clojure.set/difference)
      (def setp clojure.set/project)
      (def setj clojure.set/join)
      (def sets clojure.set/select)

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
      (def pow motto.lib.burrow/pow)

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
      (def take_repeat motto.lib.list/-take-repeat-)
      (def lift motto.lib.list/lift)
      (def dip motto.lib.list/dip)
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
      (def collect_once motto.lib.list/collect-once)
      (def count_f motto.lib.list/count-for)
      (def count_eq motto.lib.list/count-eq)
      (def counts motto.lib.list/counts)
      (def zip motto.lib.list/zip)
      (def pairs motto.lib.list/pairs)
      (def listf motto.lib.list/listf)
      (def pos motto.lib.list/index-of)
      (def truths motto.lib.list/truths)
      (def dim motto.lib.list/dim)
      (def sel motto.lib.list/sel)
      (def in motto.lib.list/in?)
      (def inf motto.lib.list/inf)
      (def dig motto.lib.list/dig)

      (def tab motto.lib.tab/mktab)
      (def cols motto.lib.tab/cols)
      (def top motto.lib.tab/top)
      (def group motto.lib.tab/group)
      (def count_group motto.lib.tab/count-grp)
      (def club motto.lib.tab/tab-merge)

      (def data_source motto.dbconn/data-source)
      (def open motto.dbconn/open)
      (def close motto.dbconn/close)
      (def stmt motto.dbconn/stmt)
      (def qry motto.dbconn/qry)
      (def cmd motto.dbconn/cmd)

      (def csv motto.lib.csv/csv)
      (def csv_fmt motto.lib.csv/fmt)
      (def csv_ahdr motto.lib.csv/with-auto-header)
      (def csv_hdr motto.lib.csv/with-header)
      (def csv_delim motto.lib.csv/with-delim)
      (def csv_rd motto.lib.csv/rd)

      (def http_get motto.lib.http/http-get)
      (def http_res motto.lib.http/http-res)

      (def json motto.lib.json/json-str)
      (def json_parse motto.lib.json/parse)

      (fn [expr] (eval expr)))))
