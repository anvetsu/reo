(ns reo.global-env
  (:require [clojure.math.combinatorics]
            [incanter.distributions]
            [incanter.stats]
            [incanter.charts]
            [reo.config]
            [reo.compile]
            [reo.dbconn]
            [reo.type]
            [reo.bitvec]
            [reo.tab]
            [reo.burrow]
            [reo.math]
            [reo.list]
            [reo.str-util]
            [reo.expr-io]
            [reo.lib.sys]
            [reo.lib.func]
            [reo.lib.clj-integ]
            [reo.lib.io]
            [reo.lib.obj]
            [reo.lib.csv]
            [reo.lib.dt]
            [reo.lib.xls]
            [reo.lib.dct]
            [reo.lib.charts]
            [reo.lib.stats]
            [reo.lib.json]
            [reo.lib.http]))

(defn make-eval []
  (eval
   '(do
      (def set_prompt reo.config/prompt!)
      (def set_prompt2 reo.config/prompt2!)

      (def _ clojure.core/partial)
      (def o clojure.core/comp)
      (def -neg- clojure.core/-)
      (def dict reo.list/dict)
      (def roll clojure.core/rand-int)
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
      (def is_list clojure.core/list?)
      (def is_dict clojure.core/map?)
      (def is_number clojure.core/number?)
      (def is_empty clojure.core/empty?)
      (def is_true clojure.core/true?)
      (def is_false clojure.core/false?)

      (def to_int reo.str-util/s->int)
      (def to_float reo.str-util/s->float)
      (def to_double reo.str-util/s->double)

      (def exit reo.lib.sys/exit)

      (def fork reo.lib.func/fork)
      (def times reo.lib.func/times)

      (def num_eq clojure.core/=)
      (def num_lt clojure.core/<)
      (def num_gt clojure.core/>)
      (def num_lteq clojure.core/<=)
      (def num_gteq clojure.core/>=)
      (def add clojure.core/+)
      (def sub clojure.core/-)
      (def div clojure.core//)
      (def mul clojure.core/*)

      (def eql clojure.core/=)
      (def eq reo.lib.obj/eq)
      (def lt reo.lib.obj/lt)
      (def lteq reo.lib.obj/lteq)
      (def gt reo.lib.obj/gt)
      (def gteq reo.lib.obj/gteq)
      (def size reo.lib.obj/size)
      (def nul nil)
      (def is_nul clojure.core/nil?)
      (def ex reo.lib.obj/ex)
      (def with_ex reo.lib.obj/with-ex)

      ;; Clojure integration
      (def clj_refer reo.lib.clj-integ/clj-refer)
      (def clj_use reo.lib.clj-integ/clj-use)
      (def clj_require reo.lib.clj-integ/clj-require)

      ;; math
      (def ceil reo.math/ceil)
      (def floor reo.math/floor)
      (def sqrt reo.math/sqrt)
      (def nthrt reo.math/nthrt)
      (def E java.lang.Math/E)
      (def PI java.lang.Math/PI)
      (def abs reo.math/abs)
      (def acos reo.math/acos)
      (def asin reo.math/asin)
      (def atan reo.math/atan)
      (def atan2 reo.math/atan2)
      (def cbrt reo.math/cbrt)
      (def cos reo.math/cos)
      (def cosh reo.math/cosh)
      (def exp reo.math/exp)
      (def expm1 reo.math/expm1)
      (def log reo.math/log)
      (def log10 reo.math/log10)
      (def log1p reo.math/log1p)
      (def sin reo.math/sin)
      (def sinh reo.math/sinh)
      (def tan reo.math/tan)
      (def tanh reo.math/tanh)
      (def degrees reo.math/degrees)
      (def radians reo.math/radians)
      (def nan Double/NaN)
      (def is_nan reo.math/nan?)
      (def inf Double/POSITIVE_INFINITY)
      (def _inf Double/NEGATIVE_INFINITY)
      (def is_inf reo.math/inf?)

      ;; bytes
      (def byte_array clojure.core/byte-array)

      ;; regex
      (def rx clojure.core/re-pattern)
      (def rx_seq clojure.core/re-seq)
      (def rx_find clojure.core/re-find)
      (def rx_matches clojure.core/re-matches)
      (def rx_matcher clojure.core/re-matcher)

      ;; bit-vector
      (def bits reo.bitvec/from-seq)
      (def bools reo.bitvec/to-seq)
      (def count_bits reo.bitvec/length)
      (def bits_size reo.bitvec/size)
      (def bvget reo.bitvec/at)
      (def bvand reo.bitvec/_band)
      (def bvor reo.bitvec/_bor)
      (def bvand_not reo.bitvec/_band-not)
      (def bvxor reo.bitvec/_bxor)
      (def bvcross reo.bitvec/intersects?)
      (def bvflip reo.bitvec/flip)
      (def bviter reo.bitvec/for-each)
      (def bvnot reo.bitvec/flip-all)

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

      (def -+- reo.burrow/add)
      (def --- reo.burrow/sub)
      (def -*- reo.burrow/mul)
      (def -div- reo.burrow/div)
      (def -%- reo.burrow/residue)
      (def -=- reo.burrow/eq)
      (def ->- reo.burrow/gt)
      (def -<- reo.burrow/lt)
      (def ->=- reo.burrow/gteq)
      (def -<=- reo.burrow/lteq)
      (def <> reo.burrow/neq)
      (def big reo.burrow/big)
      (def sml reo.burrow/small)
      (def pow reo.burrow/pow)
      (def band reo.burrow/band)
      (def bor reo.burrow/bor)
      (def band_not reo.burrow/band-not)
      (def bxor reo.burrow/bxor)
      (def burrow reo.burrow/burrow)

      ;; date-time
      (def dt reo.lib.dt/dt)
      (def sdt reo.lib.dt/sdt)
      (def now reo.lib.dt/now)
      (def dtadd reo.lib.dt/add)
      (def dtget reo.lib.dt/getf)

      (def cf reo.compile/compile-file)

      (def any clojure.core/rand-nth)
      (def push clojure.core/conj)
      (def all clojure.core/every?)
      (def not_all clojure.core/not-every?)
      (def one (complement clojure.core/not-any?))
      (def none clojure.core/not-any?)
      (intern *ns* (symbol "#") reo.list/-concat-)
      (intern *ns* (symbol ";") reo.list/-conj-)
      (intern *ns* (symbol "@") reo.list/-fold-)
      (intern *ns* (symbol "~") clojure.core/map)
      (def liftr reo.list/-take-repeat-)
      (def lift reo.list/lift)
      (def dip reo.list/dip)
      (def take_while clojure.core/take-while)
      (def drop_while clojure.core/drop-while)
      (def pack reo.list/pack)
      (intern *ns* (symbol "@~") reo.list/fold-incr)
      (intern *ns* (symbol "@>") reo.list/fold-times)
      (def sum reo.list/sum)
      (def dif reo.list/diff)
      (def prd reo.list/prd)
      (def qt reo.list/-quot-)
      (def mx reo.list/-max-)
      (def mn reo.list/-min-)
      (def sums reo.list/sums)
      (def difs reo.list/diffs)
      (def prds reo.list/prds)
      (def qts reo.list/quots)
      (def mxs reo.list/maxs)
      (def mns reo.list/mins)
      (def til reo.list/til)
      (def twins reo.list/twins)
      (def collect reo.list/collect)
      (def collect1 reo.list/collect-once)
      (def countf reo.list/count-for)
      (def counteq reo.list/count-eq)
      (def counts reo.list/counts)
      (def zip reo.list/zip)
      (def pairs reo.list/pairs)
      (def listf reo.list/listf)
      (def nof reo.list/nof)
      (def pos reo.list/index-of)
      (def truths reo.list/truths)
      (def dim reo.list/dim)
      (def sel reo.list/sel)
      (def in reo.list/in?)
      (def infs reo.list/infs)
      (def dig reo.list/dig)
      (def at reo.list/at)
      (def without reo.list/without)
      (def eachprev reo.list/each-previous)
      (def factor reo.list/factor)
      (def replc reo.list/replc)
      (def replcf reo.list/replcf)
      (def filter_by reo.list/filter-by)
      (def lazy reo.list/lazy)
      (def enum reo.list/enum)
      (def split reo.list/split)
      (def splitf clojure.core/split-with)

      ;; table
      (def is_tab reo.tab/t?)
      (def is_rtab reo.tab/rt?)
      (def $ reo.tab/-t-)
      (def tab reo.tab/mkt)
      (def rtab reo.tab/mkrt)
      (def fields reo.tab/cols)
      (def data reo.tab/rows)
      (def top reo.tab/top)
      (def group reo.tab/group)
      (def group_count reo.tab/group-count)
      (def club reo.tab/tmerge)
      (def where reo.tab/-where-)
      (def ! reo.tab/-filter-)
      (def tmap reo.tab/tmap)
      (def flip reo.tab/flip)
      (def save reo.tab/-save-)

      ;; jdbc
      (def db reo.dbconn/data-source)
      (def db_open reo.dbconn/open)
      (def db_close reo.dbconn/close)
      (def db_stmt reo.dbconn/stmt)
      (def db_qry reo.dbconn/qry)
      (def db_cmd reo.dbconn/cmd)

      ;; csv
      (def csv reo.lib.csv/csv)
      (def csv_fmt reo.lib.csv/fmt)
      (def csv_ahdr reo.lib.csv/with-auto-header)
      (def csv_hdr reo.lib.csv/with-header)
      (def csv_delim reo.lib.csv/with-delim)
      (def csv_rd reo.lib.csv/rd)

      ;; http
      (def http_get reo.lib.http/http-get)
      (def http_res reo.lib.http/http-res)
      (def http_req reo.lib.http/http-req)

      ;; json
      (def json_enc reo.lib.json/json-str)
      (def json_dec reo.lib.json/str->json)
      (def json reo.lib.json/parse)

      ;; excel
      (def xls reo.lib.xls/read-xls)

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

      ;; charts
      (def plot reo.lib.charts/plot)
      (def chart reo.lib.charts/chart)
      (def plot_add reo.lib.charts/plot-add)
      (def view reo.lib.charts/view)
      (def chart_set reo.lib.charts/chart-set)
      (def logaxis reo.lib.charts/log-axis)
      (def save_view reo.lib.charts/save)

      ;; stats
      (def dataset incanter.core/dataset)
      (def median incanter.stats/median)
      (def mean incanter.stats/mean)
      (def sd incanter.stats/sd)
      (def v incanter.stats/variance)
      (def cdf reo.lib.stats/cdf)
      (def pdf reo.lib.stats/pdf)
      (def chisqtest reo.lib.stats/chisq-test)
      (def chisq reo.lib.stats/chisq)
      (def skewness incanter.stats/skewness)
      (def quantile reo.lib.stats/quantile)
      (def sample_normal reo.lib.stats/sample-normal)
      (def cor incanter.stats/correlation)
      (def cov incanter.stats/covariance)
      (def cmean incanter.stats/cumulative-mean)
      (def distance reo.lib.stats/distance)
      (def jaccard_index incanter.stats/jaccard-index)
      (def odds_ratio incanter.stats/odds-ratio)
      (def permute incanter.stats/permute)
      (def predict incanter.stats/predict)
      (def summary reo.lib.stats/summary)
      (def sweep reo.lib.stats/sweep)
      (def gauss incanter.distributions/normal-distribution)
      (def draw incanter.distributions/draw)
      (def zscore reo.lib.stats/zscore)
      (def dct reo.lib.dct/dct)
      (def tabds reo.tab/maybe-dset)

      ;; io
      (def setprec reo.expr-io/dbl-prec!)
      (def wr reo.expr-io/write)
      (def wrln reo.expr-io/writeln)
      (def newln clojure.core/println)
      (def rdln reo.expr-io/readln)
      (def rdmln reo.expr-io/read-multiln)
      (def file_copy reo.lib.io/copy)
      (def file_delete reo.lib.io/delete-file)
      (def bytes_in reo.lib.io/input-stream)
      (def bytes_out reo.lib.io/output-stream)
      (def bytes_flush reo.lib.io/flush-out)
      (def byte_write reo.lib.io/write-byte)
      (def bytes_write reo.lib.io/write-bytes)
      (def close reo.lib.io/close)
      (def ireset reo.lib.io/reset)
      (def iskip reo.lib.io/skip)
      (def str_in reo.lib.io/reader)
      (def str_out reo.lib.io/writer)
      (def str_write reo.lib.io/write-str)
      (def str_flush reo.lib.io/flush-writer)
      (def str_in_seq clojure.core/line-seq)
      (def with_open reo.lib.io/-with-open-)

      (fn [expr] (eval expr)))))
