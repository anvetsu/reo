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
            [motto.burrow]
            [motto.math]
            [motto.list]
            [motto.str-util]
            [motto.expr-io]
            [motto.lib.sys]
            [motto.lib.func]
            [motto.lib.clj-integ]
            [motto.lib.io]
            [motto.lib.obj]
            [motto.lib.csv]
            [motto.lib.dt]
            [motto.lib.xls]
            [motto.lib.dct]
            [motto.lib.charts]
            [motto.lib.stats]
            [motto.lib.json]
            [motto.lib.http]))

(defn make-eval []
  (eval
   '(do
      (def -neg- clojure.core/-)
      (def dict motto.list/dict)
      (def roll rand-int)
      (def parse clojure.core/read-string)
      (def fst clojure.core/first)
      (def snd clojure.core/second)
      (def is_zero clojure.core/zero?)
      (def is_pos clojure.core/pos?)
      (def is_neg clojure.core/neg?)
      (def is_odd clojure.core/odd?)
      (def is_even clojure.core/even?)
      (def is_int clojure.core/int?)
      (def is_float clojure.core/float?)
      (def is_double clojure.core/double?)
      (def is_seq clojure.core/seqable?)
      (def is_str clojure.core/string?)
      (def is_vec clojure.core/vector?)
      (def is_dict clojure.core/map?)
      (def is_string clojure.core/string?)
      (def is_number? clojure.core/number?)
      (def is_empty clojure.core/empty?)
      (def is_true clojure.core/true?)
      (def is_false clojure.core/false?)

      (def to_int motto.str-util/s->int)
      (def to_float motto.str-util/s->float)
      (def to_double motto.str-util/s->double)

      (def exit motto.lib.sys/exit)

      (def fork motto.lib.func/fork)

      (def num_eq clojure.core/=)
      (def num_lt clojure.core/<)
      (def num_gt clojure.core/>)
      (def num_lteq clojure.core/<=)
      (def num_gteq clojure.core/>=)

      (def eq motto.lib.obj/eq)
      (def lt motto.lib.obj/lt)
      (def lteq motto.lib.obj/lteq)
      (def gt motto.lib.obj/gt)
      (def gteq motto.lib.obj/gteq)
      (def size motto.lib.obj/size)
      (def nul nil)
      (def void :void)
      (def is_nul clojure.core/nil?)
      (def is_void motto.lib.obj/void?)
      (def ex motto.lib.obj/ex)
      (def with_ex motto.lib.obj/with-ex)

      ;; Clojure integration
      (def clj_refer motto.lib.clj-integ/clj-refer)
      (def clj_use motto.lib.clj-integ/clj-use)
      (def clj_require motto.lib.clj-integ/clj-require)

      ;; math
      (def ceil motto.math/ceil)
      (def floor motto.math/floor)
      (def sqrt motto.math/sqrt)
      (def E java.lang.Math/E)
      (def PI java.lang.Math/PI)
      (def abs motto.math/abs)
      (def acos motto.math/acos)
      (def asin motto.math/asin)
      (def atan motto.math/atan)
      (def atan2 motto.math/atan2)
      (def cbrt motto.math/cbrt)
      (def cos motto.math/cos)
      (def cosh motto.math/cosh)
      (def exp motto.math/exp)
      (def expm1 motto.math/expm1)
      (def log motto.math/log)
      (def log10 motto.math/log10)
      (def log1p motto.math/log1p)
      (def sin motto.math/sin)
      (def sinh motto.math/sinh)
      (def tan motto.math/tan)
      (def tanh motto.math/tanh)
      (def degrees motto.math/degrees)
      (def radians motto.math/radians)
      (def nan Double/NaN)
      (def is_nan motto.math/nan?)
      (def inf Double/POSITIVE_INFINITY)
      (def _inf Double/NEGATIVE_INFINITY)
      (def is_inf motto.math/inf?)

      ;; bytes
      (def byte_array clojure.core/byte-array)

      ;; regex
      (def rx clojure.core/re-pattern)
      (def rx_seq clojure.core/re-seq)
      (def rx_find clojure.core/re-find)
      (def rx_matches clojure.core/re-matches)
      (def rx_matcher clojure.core/re-matcher)

      ;; bit-vector
      (def bits motto.bitvec/from-seq)
      (def bools motto.bitvec/to-seq)
      (def count_bits motto.bitvec/length)
      (def bvget motto.bitvec/at)
      (def bvand motto.bitvec/_band)
      (def bvor motto.bitvec/_bor)
      (def bvand_not motto.bitvec/_band-not)
      (def bvxor motto.bitvec/_bxor)
      (def bvcross motto.bitvec/intersects?)
      (def bvflip motto.bitvec/flip)
      (def bviter motto.bitvec/for-each)
      (def bvnot motto.bitvec/flip-all)

      ;; set
      (def is_set clojure.core/set?)
      (def is_subset clojure.set/subset?)
      (def is_superset clojure.set/superset?)
      (def setu clojure.set/union)
      (def seti clojure.set/intersection)
      (def setd clojure.set/difference)
      (def setp clojure.set/project)
      (def setj clojure.set/join)
      (def setsel clojure.set/select)

      (def -+- motto.burrow/add)
      (def --- motto.burrow/sub)
      (def -*- motto.burrow/mul)
      (def -div- motto.burrow/div)
      (def -%- motto.burrow/residue)
      (def -=- motto.burrow/eq)
      (def ->- motto.burrow/gt)
      (def -<- motto.burrow/lt)
      (def ->=- motto.burrow/gteq)
      (def -<=- motto.burrow/lteq)
      (def <> motto.burrow/neq)
      (def big motto.burrow/big)
      (def sml motto.burrow/small)
      (def pow motto.burrow/pow)
      (def band motto.burrow/band)
      (def bor motto.burrow/bor)
      (def bandNot motto.burrow/band-not)
      (def bxor motto.burrow/bxor)
      (def burrow motto.burrow/burrow)

      ;; date-time
      (def dt motto.lib.dt/dt)
      (def sdt motto.lib.dt/sdt)
      (def now motto.lib.dt/now)
      (def dtadd motto.lib.dt/add)
      (def dtget motto.lib.dt/getf)

      (def cf motto.compile/compile-file)

      (def -concat- motto.list/-concat-)
      (intern *ns* (symbol "#") motto.list/-concat-)
      (def -conj- motto.list/-conj-)
      (def -fold- motto.list/-fold-)
      (intern *ns* (symbol "@") motto.list/-fold-)
      (def -map- clojure.core/map)
      (intern *ns* (symbol "~") clojure.core/map)
      (def liftr motto.list/-take-repeat-)
      (def lift motto.list/lift)
      (def dip motto.list/dip)
      (def take_while clojure.core/take-while)
      (def drop_while clojure.core/drop-while)
      (def pack motto.list/pack)
      (def fold-incr motto.list/fold-incr)
      (intern *ns* (symbol "@~") motto.list/fold-incr)
      (def fold-times motto.list/fold-times)
      (intern *ns* (symbol "@>") motto.list/fold-times)
      (def sum motto.list/sum)
      (def dif motto.list/diff)
      (def prd motto.list/prd)
      (def qt motto.list/-quot-)
      (def mx motto.list/-max-)
      (def mn motto.list/-min-)
      (def sums motto.list/sums)
      (def difs motto.list/diffs)
      (def prds motto.list/prds)
      (def qts motto.list/quots)
      (def mxs motto.list/maxs)
      (def mns motto.list/mins)
      (def til motto.list/til)
      (def twins motto.list/twins)
      (def collect motto.list/collect)
      (def collect1 motto.list/collect-once)
      (def countf motto.list/count-for)
      (def counteq motto.list/count-eq)
      (def counts motto.list/counts)
      (def zip motto.list/zip)
      (def pairs motto.list/pairs)
      (def listf motto.list/listf)
      (def pos motto.list/index-of)
      (def truths motto.list/truths)
      (def dim motto.list/dim)
      (def sel motto.list/sel)
      (def in motto.list/in?)
      (def infs motto.list/infs)
      (def dig motto.list/dig)
      (def without motto.list/without)
      (def eachprev motto.list/each-previous)
      (def factor motto.list/factor)
      (def replc motto.list/replc)
      (def replcf motto.list/replcf)
      (def filter_by motto.list/filter-by)
      (def lazy motto.list/lazy)

      ;; table
      (def is_tab motto.tab/t?)
      (def is_rtab motto.tab/rt?)
      (def -tab- motto.tab/-t-)
      (def $ motto.tab/-t-)
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
      (def ! motto.tab/-filter-)
      (def tmap motto.tab/tmap)
      (def flip motto.tab/flip)
      (def save motto.tab/save)
      (def dataset incanter.core/dataset)

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
      (def http_req motto.lib.http/http-req)

      ;; json
      (def json_enc motto.lib.json/json-str)
      (def json_dec motto.lib.json/str->json)
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
      (def save_view motto.lib.charts/save)

      ;; stats
      (def median incanter.stats/median)
      (def mean incanter.stats/mean)
      (def sd incanter.stats/sd)
      (def v incanter.stats/variance)
      (def cdf motto.lib.stats/cdf)
      (def pdf motto.lib.stats/pdf)
      (def chisqtest motto.lib.stats/chisq-test)
      (def skewness incanter.stats/skewness)
      (def quantile incanter.stats/quantile)
      (def sample_normal incanter.stats/sample-normal)
      (def cor incanter.stats/correlation)
      (def cov incanter.stats/covariance)
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
      (def zscore motto.lib.stats/zscore)
      (def dct motto.lib.dct/dct)

      ;; io
      (def wr motto.expr-io/write)
      (def wrln motto.expr-io/writeln)
      (def newln clojure.core/println)
      (def rdln motto.expr-io/readln)
      (def rdmln motto.expr-io/read-multiln)
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
      (def with_in motto.lib.io/with-is)

      (fn [expr] (eval expr)))))
