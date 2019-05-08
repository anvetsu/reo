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
            [motto.lib.obj]
            [motto.lib.csv]
            [motto.lib.burrow]
            [motto.lib.str]
            [motto.lib.math]
            [motto.lib.dt]
            [motto.lib.list]
            [motto.lib.xls]
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

      (def ceil motto.lib.math/ceil)
      (def floor motto.lib.math/floor)
      (def sqrt motto.lib.math/sqrt)

      (def sjoin motto.lib.str/join)
      (def ssplit motto.lib.str/split)

      (def regex re-pattern)
      (def reseq re-seq)
      (def refind re-find)
      (def rematches re-matches)
      (def rematcher re-matcher)

      (def bits motto.bitvec/from-seq)
      (def bools motto.bitvec/to-seq)
      (def countBits motto.bitvec/length)
      (def bvon motto.bitvec/on?)
      (def bvoff motto.bitvec/off?)
      (def bvand motto.bitvec/_band)
      (def bvor motto.bitvec/_bor)
      (def bvandNot motto.bitvec/_band-not)
      (def bvxor motto.bitvec/_bxor)
      (def bvcross motto.bitvec/intersects?)
      (def bvflip motto.bitvec/flip)
      (def bviter motto.bitvec/for-each)
      (def bvnot motto.bitvec/flip-all)

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
      (def collectOnce motto.lib.list/collect-once)
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

      (def -tab- motto.tab/-tab-)
      (def tab motto.tab/mktab)
      (def cols motto.tab/tab-cols)
      (def top motto.tab/top)
      (def group motto.tab/group)
      (def count_group motto.tab/count-grp)
      (def club motto.tab/tab-merge)
      (def where motto.tab/-where-)
      (def -filter- motto.tab/-filter-)

      (def datasource motto.dbconn/data-source)
      (def open motto.dbconn/open)
      (def close motto.dbconn/close)
      (def stmt motto.dbconn/stmt)
      (def qry motto.dbconn/qry)
      (def cmd motto.dbconn/cmd)

      (def csv motto.lib.csv/csv)
      (def csvfmt motto.lib.csv/fmt)
      (def csvahdr motto.lib.csv/with-auto-header)
      (def csvhdr motto.lib.csv/with-header)
      (def csvdelim motto.lib.csv/with-delim)
      (def csvrd motto.lib.csv/rd)

      (def httpget motto.lib.http/http-get)
      (def httpres motto.lib.http/http-res)

      (def json motto.lib.json/json-str)
      (def json_parse motto.lib.json/parse)

      (def permutations clojure.math.combinatorics/permutations)
      (def countPermutations clojure.math.combinatorics/count-permutations)
      (def combinations clojure.math.combinatorics/combinations)
      (def countCombinations clojure.math.combinatorics/count-combinations)
      (def subsets clojure.math.combinatorics/subsets)
      (def countSubsets clojure.math.combinatorics/count-subsets)
      (def cartesianProduct clojure.math.combinatorics/cartesian-product)
      (def selections clojure.math.combinatorics/selections)
      (def partitions clojure.math.combinatorics/partitions)

      (def xls motto.lib.xls/load-data)

      (def histogram motto.lib.charts/histogram)
      (def qqplot motto.lib.charts/qq-plot)
      (def scatterplot motto.lib.charts/scatter-plot)
      (def boxplot motto.lib.charts/box-plot)
      (def addBoxplot motto.lib.charts/add-box-plot)
      (def xyplot motto.lib.charts/xy-plot)
      (def addPoints motto.lib.charts/add-points)
      (def addLines motto.lib.charts/add-lines)
      (def areahart motto.lib.charts/area-chart)
      (def barchart motto.lib.charts/bar-chart)
      (def linechart motto.lib.charts/line-chart)
      (def piechart motto.lib.charts/pie-chart)
      (def view motto.lib.charts/view)
      (def chartset motto.lib.charts/chart-set)
      (def logaxis motto.lib.charts/log-axis)

      (def median incanter.stats/median)
      (def mean incanter.stats/mean)
      (def sd incanter.stats/sd)
      (def variance incanter.stats/variance)
      (def cdf motto.lib.stats/cdf)
      (def pdf motto.lib.stats/pdf)
      (def chebyshev_distance incanter.stats/chebyshev-distance)
      (def chisqtest motto.lib.stats/chisq-test)
      (def skewness incanter.stats/skewness)
      (def quantile incanter.stats/quantile)
      (def sampleNormal incanter.stats/sample-normal)
      (def correlation incanter.stats/correlation)
      (def covariance incanter.stats/covariance)
      (def cumulativeMean incanter.stats/cumulative-mean)
      (def distance motto.lib.stats/distance)
      (def jaccardIndex incanter.stats/jaccard-index)
      (def oddsRatio incanter.stats/odds-ratio)
      (def permute incanter.stats/permute)
      (def predict incanter.stats/predict)
      (def summary incanter.stats/summary)
      (def sweep motto.lib.stats/sweep)

      (def gauss incanter.distributions/normal-distribution)
      (def draw incanter.distributions/draw)

      (fn [expr] (eval expr)))))
