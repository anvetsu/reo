base:int(\a)
islcase:fn(c) `Character/isLowerCase`(c)
sj:`clojure.string/join`

ctoi:fn(c) int(c)-base
itoc:fn(i) char(base+i)

shift:fn(n c) if(islcase(c) itoc(mod(ctoi(c) + n, 26)) c)

encode:fn(n s) sj(partial(shift, n) ~ seq(s))

table:[8.1, 1.5, 2.8, 4.2, 12.7, 2.2, 2.0, 6.1, 7.0,
0.2, 0.8, 4.0, 2.4, 6.7, 7.5, 1.9, 0.1, 6.0,
6.3, 9.0, 2.8, 1.0, 2.4, 0.2, 2.0, 0.1]

percent:fn(n m) (float(n) / float(m)) * 100

lowers:fn(s) count((fn(c) islcase(c)) ! seq(s))

alphas:char~til(int(\a) int(\z)+1)

freqs:fn(s) {[xs seq(s) n lowers(s)] (fn(x) percent(counteq(x xs) n))~alphas}

rotate:fork(drop `#` take)

chisqr:fn(os es) sum((fn([o e]) pow(o-e, 2)/e)~zip(os es))

table2:freqs("kdvnhoo lv ixq")

(fn(n) chisqr(rotate(n table2) table))~til(26)