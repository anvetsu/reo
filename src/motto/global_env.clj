(ns motto.global-env
  (:require [clojure.math.combinatorics]
            [incanter.distributions]
            [incanter.stats]
            [incanter.charts]
            [motto.compile]
            [motto.dbconn]
            [motto.type]
            [motto.bitvec]
            [motto.tab]
            [motto.lib.io]
            [motto.lib.obj]
            [motto.lib.csv]
            [motto.lib.burrow]
            [motto.lib.str]
            [motto.lib.math]
            [motto.lib.dt]
            [motto.lib.list]
            [motto.lib.xls]
            [motto.lib.dct]
            [motto.lib.charts]
            [motto.lib.stats]
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
      (def isOdd odd?)
      (def isEven even?)

      (def neq =)
      (def nlt <)
      (def ngt >)
      (def nlteq <=)
      (def ngteq >=)

      (def eq motto.lib.obj/eq)
      (def lt motto.lib.obj/lt)
      (def lteq motto.lib.obj/lteq)
      (def gt motto.lib.obj/gt)
      (def gteq motto.lib.obj/gteq)
      (def size motto.lib.obj/size)
      (def is_nil nil?)
      (def is_void motto.lib.obj/void?)

      ;; math
      (def ceil motto.lib.math/ceil)
      (def floor motto.lib.math/floor)
      (def sqrt motto.lib.math/sqrt)
      (def E java.lang.Math/E)
      (def PI java.lang.Math/PI)
      (def abs motto.lib.math/abs)
      (def acos motto.lib.math/acos)
      (def asin motto.lib.math/asin)
      (def atan motto.lib.math/atan)
      (def atan2 motto.lib.math/atan2)
      (def cbrt motto.lib.math/cbrt)
      (def cos motto.lib.math/cos)
      (def cosh motto.lib.math/cosh)
      (def exp motto.lib.math/exp)
      (def expm1 motto.lib.math/expm1)
      (def log motto.lib.math/log)
      (def log10 motto.lib.math/log10)
      (def log1p motto.lib.math/log1p)
      (def sin motto.lib.math/sin)
      (def sinh motto.lib.math/sinh)
      (def tan motto.lib.math/tan)
      (def tanh motto.lib.math/tanh)
      (def degrees motto.lib.math/degrees)
      (def radians motto.lib.math/radians)
      (def nan Double/NaN)
      (def is_nan motto.lib.math/nan?)

      ;; string
      (def sjoin motto.lib.str/join)
      (def ssplit motto.lib.str/split)

      ;; bytes
      (def byte_array byte-array)

      ;; regex
      (def rx re-pattern)
      (def rx_seq re-seq)
      (def rx_find re-find)
      (def rx_matches re-matches)
      (def rx_matcher re-matcher)

      ;; bit-vector
      (def bits motto.bitvec/from-seq)
      (def bools motto.bitvec/to-seq)
      (def count_bits motto.bitvec/length)
      (def bvon motto.bitvec/on?)
      (def bvoff motto.bitvec/off?)
      (def bvand motto.bitvec/_band)
      (def bvor motto.bitvec/_bor)
      (def bvand_not motto.bitvec/_band-not)
      (def bvxor motto.bitvec/_bxor)
      (def bvcross motto.bitvec/intersects?)
      (def bvflip motto.bitvec/flip)
      (def bviter motto.bitvec/for-each)
      (def bvnot motto.bitvec/flip-all)

      ;; set
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
      (def -%- motto.lib.burrow/residue)
      (def -=- motto.lib.burrow/eq)
      (def ->- motto.lib.burrow/gt)
      (def -<- motto.lib.burrow/lt)
      (def ->=- motto.lib.burrow/gteq)
      (def -<=- motto.lib.burrow/lteq)
      (def <> motto.lib.burrow/neq)
      (def big motto.lib.burrow/big)
      (def sml motto.lib.burrow/small)
      (def pow motto.lib.burrow/pow)
      (def band motto.lib.burrow/band)
      (def bor motto.lib.burrow/bor)
      (def bandNot motto.lib.burrow/band-not)
      (def bxor motto.lib.burrow/bxor)
      (def burrow motto.lib.burrow/burrow)

      ;; date-time
      (def dt motto.lib.dt/dt)
      (def sdt motto.lib.dt/sdt)
      (def now motto.lib.dt/now)
      (def dtadd motto.lib.dt/add)
      (def dtget motto.lib.dt/getf)

      (def cf motto.compile/compile-file)

      (def -concat- motto.lib.list/-concat-)
      (def -conj- motto.lib.list/-conj-)
      (def -fold- motto.lib.list/-fold-)
      (def -map- motto.lib.list/-map-)
      (def liftr motto.lib.list/-take-repeat-)
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
      (def collect1 motto.lib.list/collect-once)
      (def countf motto.lib.list/count-for)
      (def counteq motto.lib.list/count-eq)
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
      (def without motto.lib.list/without)
      (def eachprev motto.lib.list/each-previous)
      (def factor motto.lib.list/factor)
      (def replc motto.lib.list/replc)
      (def replcf motto.lib.list/replcf)

      ;; table
      (def -tab- motto.tab/-t-)
      (def tab motto.tab/mkt)
      (def rtab motto.tab/mkrt)
      (def fields motto.tab/cols)
      (def data motto.tab/rows)
      (def top motto.tab/top)
      (def group motto.tab/group)
      (def group_count motto.tab/group-count)
      (def club motto.tab/tmerge)
      (def where motto.tab/-where-)
      (def -filter- motto.tab/-filter-)
      (def tmap motto.tab/tmap)
      (def flip motto.tab/flip)

      ;; jdbc
      (def datasource motto.dbconn/data-source)
      (def open motto.dbconn/open)
      (def close motto.dbconn/close)
      (def stmt motto.dbconn/stmt)
      (def qry motto.dbconn/qry)
      (def cmd motto.dbconn/cmd)

      ;; csv
      (def csv motto.lib.csv/csv)
      (def csv_fmt motto.lib.csv/fmt)
      (def csv_ahdr motto.lib.csv/with-auto-header)
      (def csv_hdr motto.lib.csv/with-header)
      (def csv_delim motto.lib.csv/with-delim)
      (def csv_rd motto.lib.csv/rd)

      ;; http
      (def http_get motto.lib.http/http-get)
      (def http_res motto.lib.http/http-res)

      ;; json
      (def jsons motto.lib.json/json-str)
      (def json motto.lib.json/parse)

      ;; combinatorics
      (def permutations clojure.math.combinatorics/permutations)
      (def count_permutations clojure.math.combinatorics/count-permutations)
      (def combinations clojure.math.combinatorics/combinations)
      (def count_combinations clojure.math.combinatorics/count-combinations)
      (def subsets clojure.math.combinatorics/subsets)
      (def count_subsets clojure.math.combinatorics/count-subsets)
      (def cartesian_product clojure.math.combinatorics/cartesian-product)
      (def selections clojure.math.combinatorics/selections)
      (def partitions clojure.math.combinatorics/partitions)

      ;; excel
      (def xls motto.lib.xls/read-xls)

      ;; charts
      (def plot motto.lib.charts/plot)
      (def chart motto.lib.charts/chart)
      (def chartadd motto.lib.charts/chart-add)
      (def view motto.lib.charts/view)
      (def chartset motto.lib.charts/chart-set)
      (def logaxis motto.lib.charts/log-axis)

      ;; stats
      (def median incanter.stats/median)
      (def mean incanter.stats/mean)
      (def sd incanter.stats/sd)
      (def var incanter.stats/variance)
      (def cdf motto.lib.stats/cdf)
      (def pdf motto.lib.stats/pdf)
      (def chisqtest motto.lib.stats/chisq-test)
      (def skewness incanter.stats/skewness)
      (def quantile incanter.stats/quantile)
      (def sample_normal incanter.stats/sample-normal)
      (def cor incanter.stats/correlation)
      (def covar incanter.stats/covariance)
      (def cmean incanter.stats/cumulative-mean)
      (def distance motto.lib.stats/distance)
      (def jaccard_index incanter.stats/jaccard-index)
      (def odds_ratio incanter.stats/odds-ratio)
      (def permute incanter.stats/permute)
      (def predict incanter.stats/predict)
      (def summary motto.lib.stats/summary)
      (def sweep motto.lib.stats/sweep)
      (def gauss incanter.distributions/normal-distribution)
      (def draw incanter.distributions/draw)

      (def dct motto.lib.dct/dct)

      ;; io
      (def file_copy motto.lib.io/copy)
      (def file_delete motto.lib.io/delete-file)
      (def bytes_in motto.lib.io/input-stream)
      (def bytes_out motto.lib.io/output-stream)
      (def bytes_flush motto.lib.io/flush-out)
      (def byte_write motto.lib.io/write-byte)
      (def bytes_write motto.lib.io/write-bytes)
      (def close motto.lib.io/close)
      (def ireset motto.lib.io/reset)
      (def iskip motto.lib.io/skip)
      (def str_in motto.lib.io/reader)
      (def str_out motto.lib.io/writer)
      (def str_write motto.lib.io/write-str)
      (def str_flush motto.lib.io/flush-writer)
      (def str_in_seq clojure.core/line-seq)

      (fn [expr] (eval expr)))))
