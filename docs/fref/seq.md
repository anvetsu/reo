# Sequences & Sets

#### # (concat)

Concatenate two sequences. Put atomic arguments into sequences as required.

```lisp
[1 2 3]#[4 5]
; [1 2 3 4 5]

[1 2 3]#4
; [1 2 3 4]

1#[2 3]
; [1 2 3]

1#2
; [1 2]
```

#### ; (push)

Push an object to the front or back of a sequence.

```lisp
1;[2 3]
; [1 2 3]

[2 3];4
; [2 3 4]

[1 2 3];[4 5]
; [1 2 3 [4 5]]
```

#### @ (fold)

Insert an operator or function between each element of a sequence and reduce the sequence to a single value.

```lisp
(fn(x y) x*x + y*y) @ [1 2 3 4 5]
1373609
```

#### @> (fold-times)

Start with an initial sequence, pass it to a function. Extend the sequence with the result.
Repeat the process `n` times.

```lisp
(fn (x) x;sum(lift(-2 x))) @> 10 [1 1]
; [1 1 2 3 5 8 13 21 34 55 89 144]
```

#### @~ (fold-incr)

Perform a fold and return a sequence of incremental results.

```lisp
(fn(x y) x*x + y*y) @~ [1 2 3 4 5]
; [1 5 34 1172 1373609]
```

#### all(predic xs)

Return `true` if all elements in `xs` statisfies the predicate.

```lisp
all(is_odd [3 1 5 7])
; 1b

all(is_odd [3 1 5 7 8])
; 0b
```

#### any(xs)

Return a randomly selected value from `xs`.

#### collect(f init xs)

Call `f` with each element in `xs` and its index. Accumulates the result in a dictionary
keyed by the current value from `xs`. This key will have the initial value `init`.
Return this dictionary.

```lisp
collect(fn(x i) x*i 10 [1 2 3 4 5])
; [1:0 2:10 3:20 4:30 5:40]

collect(fn(x i) x*i 10 [1 3 3 4 4])
[1:0 3:20 4:120]
```

#### collect1(f xs)

Call `f` with each element in `xs` and collect the result in a dictionary.
An element is collected only once.

```lisp
collect1(fn(x) x*10 [1 3 3 4 4])
; [1:10 3:30 4:40]
```

#### counteq(x xs)

Count how many `x` are there in `xs`.

```lisp
counteq(1 [1 2 3 1 1])
; 3
```

#### countf(predic xs)

Count how many elements in `xs` return `true` for `predic`.

```lisp
countf(is_odd [1 2 3 4 5])
; 3
```

#### counts(xs)

Count the number of occurrences for each element in `xs`.

```lisp
counts([1 3 3 4 4 5 4])
; [1:1 3:2 4:3 5:1]
```

#### dif(xs)

Folds `xs` with the minus (`-`) operator.

```lisp
dif([10 34 56 77])
; -157
```

#### difs

Incremental version of `dif`.

```lisp
difs([10 34 56 77])
; [10 -24 -80 -157]
```

#### dig(xs co-ord)

Dig into `xs` by a co-ordinate and return the value there.

```lisp
dig([[1 2] [3 4]] [0 1])
; 2

dig([[1 2] [3 4]] [1 1])
; 4
```

#### dim(xs)

Return the dimension of `xs`.

```lisp
dim([10 20 45 34])
; [4]

dim([[10 20] [45 34]])
; [2 2]
```

#### dip(n xs)

Drop the first `n` elements of `xs`. If `n` is negative, drop from the end.

```lisp
dip(2 [10 20 30 40 50 60])
; [30 40 50 60]

dip(-2 [10 20 30 40 50 60])
; [10 20 30 40]
```

#### drop_while(predic xs)

Drop from `xs` until `predic` returns `true`.

```lisp
drop_while(is_odd [1 3 5 6 7 8])
; [6 7 8]
```

#### eachprev(f xs)

Call `f` with the current and the previous element in `xs`. Return the resulting sequence.

```lisp
eachprev(`+` [1 2 3 4 5])
; [3 5 7 9]
```

#### enum(f xs & ys)

Call `f` with each element of `xs` with all elements of `ys` to make a tabulated result.

```lisp
a: [0 1 2 3 4 5]
b: [2 3 5 7 11 13]

enum(`+` a b)
; [[2 3 5 7 11 13]
;  [3 4 6 8 12 14]
;  [4 5 7 9 13 15]
;  [5 6 8 10 14 16]
;  [6 7 9 11 15 17]
;  [7 8 10 12 16 18]]
```

If `ys` is omitted, `xs` will replace it.

```lisp
enum(`*` [1 2 3 4 5])
; [[1 2 3 4 5]
;  [2 4 6 8 10]
;  [3 6 9 12 15]
;  [4 8 12 16 20]
;  [5 10 15 20 25]]
```

#### factor(xs & options)

Assign "groups" to the values in `xs`. Return sequence of two elements - a dictionary of groupings and a
vector of corresponding group for each element in `xs`.

If specified, `options` must be a dictionary with the following entries:

 - levels - `xs` as a set of groups
 - sort - if `true`, use the sorted set of `xs` as groups.


```lisp
factor([10 20 30 40 30 50] ['levels:[50 40 30 20 10]])
; [[50:1 40:2 30:3 20:4 10:5]
;  [5 4 3 2 3 1]]

factor([10 20 30 40 30 50])
; [[20:1 50:2 40:3 30:4 10:5]
;  [5 1 4 3 4 2]]

factor([10 20 30 40 30 50] ['sort:1b])
; [[10:1 20:2 30:3 40:4 50:5]
;  [1 2 3 4 3 5]]
```

#### filter_by(predic xs yss)

For each element in `xs` that `predic` returns `true`, pick the corresponding element from the sequence-of-sequences `yss`.

```lisp
filter_by(is_odd [1 2 3] [[10 20 30] [100 200 300] [1000 2000 3000]])
; [[10 30]
;  [100 300]
;  [1000 3000]]
``` 

#### in(xs x)

Return `true` if `x` is in `xs`, return `false` otherwise.

#### infs(x)

Return an infinite sequence of `x`s.

```lisp
infs(1b)
; [1b 1b 1b 1b 1b 1b 1b 1b 1b 1b ...]
```

#### lazy(x f)

Return an infinite sequence starting with `x` and whose tail in filled in by calls to the no-argument function `f`.

```lisp
evens:fn(x) lazy(x fn() evens(x+2))
xs:evens(2)

xs
; [2 4 6 8 10 12 14 16 18 20 ...]
```

#### lift(n xs)

Take the first `n` elements from `xs`. If `n` is negative, take from the end.

```lisp
lift(2 [10 20 30 40 50])
; [10 20]

lift(-2 [10 20 30 40 50])
; [40 50]
```

#### liftr(n xs)

Take the first `n` elements from `xs`. If `xs` runs-out, take again from its head.

```lisp
liftr(10 [1 2 3 4 5])
; [1 2 3 4 5 1 2 3 4 5]
```

#### listf(f & r)

Return an infinite sequence of calling `r`, `r1:f(r)`, `r2:f(r1)` and so on.

```lisp
listf(sqrt 10)
; [10 3.162 1.778 1.334 1.155 1.075 1.037 1.018 1.009 ...]
```

If `r` is left out, return an infinite sequence of calling `f()`.

```lisp
r2:fn() roll(2)

take(5 listf(r2))
; [0 1 0 1 0]
```

#### mn(xs)

Return the smallest value from `xs`.

```lisp
mn([10 90 8 20 12])
' 8
```

#### mns(xs)

Incrementally return the smallest value from `xs`.

```lisp
mns([10 90 8 20 12])
; [10 10 8 8 8]
```

#### mx(xs)

Return the largest value from `xs`.

#### mxs(xs)

Incrementally return the largest value from `xs`.

#### none(predic xs)

Return `true` is none of the elements of `xs` statisfies the predicate.

```lisp
none(is_odd [3 1 5 7 8])
; 0b

none(is_odd [2 4 8])
; 1b
```

#### not_all(predic xs)

Return `true` is not all elements of `xs` statisfies the predicate.

```lisp
not_all(is_odd [3 1 5 7 8])
; 1b
```

#### one(predic xs)

Return `true` is at least one of the elements in `xs` statisfy the predicate.

```lisp
one(is_odd [3 1 5 7 8])
; 1b
```

#### pack(max_wt f xs)

Pack items from `xs` into a new sequence until an maximum weight is met.
Each element from `xs` is passed to the function `f` to compute the individual weight.

Return a sequence of elements moved to the new list and the remaining elements from `xs`.

```lisp
pack(10 identity [2 5 3 2 1])
; [[2 5 3]
;  [2 1]]

pack(10 identity [2 5 30 2 1])
; [[2 5 2 1]]
``` 

#### pairs(xs ys)

Return a dictionary created from `xs` (keys) and `ys` (values).

```lisp
pairs([1 2 3] [10 20 30])
; [1:10 3:30 2:20]
```

#### pos(xs x)

Return the index of `x` in `xs`. Return `-1` if `x` is not in `xs`.

```lisp
pos([\q \u \e \e \n] \u)
; 1

pos([10 56 34 90] 34)
; 2

pos([10 56 34 90] 30)
; -1
```

#### prd(xs)

Fold `xs` using multiplication.

```lisp
prd([1 2 3 4 5])
; 120
```

#### prds(xs)

Incrementally fold `xs` using multiplication.

```lisp
prds([1 2 3 4 5])
; [1 2 6 24 120]
```

#### push(xs x)

Add an element to a sequence. The place where the new element is added depends on the exact
type of the sequence.

```lisp
push([1 2 3 4 5] 6)
; [1 2 3 4 5 6]

push(list(1 2 3 4 5) 6)
; [6 1 2 3 4 5]

push(set([1 2 3 4 5]) 6)
; [1 4 6 3 2 5]
```

#### qt(xs)

Fold `xs` using division.

#### qts

Incrementally fold `xs` using division.

#### replc(xs x y)

Replace `x` is `xs` by `y`.

```lisp
replc([1 2 1 3 4] 1 \a)
; [a 2 a 3 4]
```

#### replcf(xs f y)

Replace all elements in `xs` that return `true` for `f` with `y`.

```lisp
replcf([1 2 1 3 4] is_odd 100)
; [100 2 100 100 4]
```

#### sel(flags xs)

Return all elements `xs` for which a flag is set.

```lisp
sel([1b 0b 0b 1b 1b] [1 2 3 4 5])
; [1 4 5]
```

#### sum(xs)

Fold `xs` using addition.

#### sums(xs)

Incrementally fold `xs` using addition.

#### take_while(predic xs)

Take elements from `xs` while `predic` returns `true`.

#### til(a & b)

Return a sequence with integers from `a` (inclusive) to `b` (exclusive).
`a` defaults to `0`.

```lisp
til(5)
; [0 1 2 3 4]

til(5 10)
[5 6 7 8 9]
```

#### truths(xs)

Return truths from `xs`.

```lisp
truths([1 0b "hello" 0b 23])
; [1 hello 23]
```

#### twins(f xs)

Call `f` with each pair from `xs`.

```lisp
twins(`+` [1 2 3 4 5 6])
; [1 3 5 7 9 11]
```

#### without(x xs)

Return `xs` without `x`.

```lisp
without(10 [1 2 10 3 4 10 5])
; [1 2 3 4 5]
```

#### zip(xs ys)

Pair `xs` with `ys.

```lisp
zip([1 2 3 4] [10 20 30 40])
; [[1 10]
;  [2 20]
;  [3 30]
;  [4 40]]
``` 

#### ~ (map)

The `map` operator.

```lisp
(fn(x) x*x) ~ [1 2 3 4 5]
; [1 4 9 16 25]
```

## Sets

#### is_set(x)

Return `true` if `x` is a set, otherwise return `false`.

#### is_subset(x y)

Return `true` is `x` is a subset of `y`.

```lisp
is_subset(set([3 2]) set([1 2 3 4 5]))
; 1b
```

#### is_superset(x y)

Return `true` is `x` is a super-set of `y`.

```lisp
is_superset(set([1 2 3 4 5]) set([3 2]))
; 1b
```

#### set(xs)

Return the sequence `xs` as a set.

```lisp
set([1 2 1 3 2 4])
; [1 4 3 2]
```

#### setd(x y & xs)

Return the difference of the sets.

```lisp
x:set([1 2 3 4 5])
y:set([3 2])

setd(x y)
; [1 4 5]
```

#### seti(x y & xs)

Return the intersection of the sets.

```lisp
seti(x y)
; [3 2]
```

#### setj(x y & on)

Return the natural join of two relational sets.

```lisp
emp:set([['name:'sam 'emp_dept:1 'salary:1500]
           ['name:'mat 'emp_dept:2 'salary:2000]
	   ['name:'san 'emp_dept:1 'salary:1300]])

dept:set([['dept:1 'title:'accounts]
            ['dept:2 'title:'sales]])

setj(emp dept)
; [[dept:1 title:accounts name:sam emp_dept:1 salary:1500]
;  [dept:2 title:sales name:sam emp_dept:1 salary:1500]
;  [dept:1 title:accounts name:mat emp_dept:2 salary:2000]
;  [dept:1 title:accounts name:san emp_dept:1 salary:1300]
;  [dept:2 title:sales name:mat emp_dept:2 salary:2000]
;  [dept:2 title:sales name:san emp_dept:1 salary:1300]]
```

If `on` is specified, it must be a mapping of keys on which to perform the joins.

```lisp
setj(emp dept ['emp_dept:'dept])
; [[dept:1 title:accounts name:sam emp_dept:1 salary:1500]
;  [dept:1 title:accounts name:san emp_dept:1 salary:1300]
;  [dept:2 title:sales name:mat emp_dept:2 salary:2000]]
```

#### setp(x ks)

Remove unwanted keys from a relational set.

```lisp
setp(emp ['name 'salary])
; [[name:sam salary:1500]
;  [name:san salary:1300]
;  [name:mat salary:2000]]
``` 

#### setsel(predic x)

Filter the set based on the predicate.


```lisp
x
; [1 4 3 2 5]

setsel(is_odd x)
; [1 3 5]
```

#### setu(x y & xs)

Return the union of the sets.

```lisp
setu(set([1 2 3]) set([3 4 5]) set([5 5 5]))
; [1 4 3 2 5]
```