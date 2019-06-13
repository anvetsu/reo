# Core

Most functions in [`clojure.core`](https://clojuredocs.org/clojure.core) are directly usable in Motto.

Motto has extended some core functions with additional features and also added some new functions.
These are documented here.

#### +, -, *, /

The burrowing- arithmetic functions: addition, subtraction, multiplication and division.

```rust
? 1 + 2 * 3 / 2
; 4

? (1 + 2) * 3 / 2
; 9/2

? 1 + [12 67 100]
; [13 68 101]

? [[1 2] [3 4] 5 6] * [[10 100] [20 200] 1000 10000]
; [[10 200]
;  [60 800] 5000 60000]
```

Note that division by zero will return infinity. Any arithmetic operation involving infinity will result in
infinity.

```rust
? 1/0
; inf

? (1/0)*100
; inf
```

#### =, <>, <, <=, >, >=

The burrowing-comparison operators: equals, not-equals, less-than, less-than-or-equals, greater-than, greater-than-or-equals.

```rust
? 1 = 2
; 0b

? 1 = [1 1 2 2 1]
; [1b 1b 0b 0b 1b]

? "hi" < "abc"
; [0b 0b]

? "hi" > "abc"
; [1b 1b]

? [[1 2] 3 4] <= [[0 3] 3 5]
; [[0b 1b] 1b 1b]
```

#### & |

The logical operators: and, or.

```rust
? 1 = 1 & 3 = 4
; 0b

? 1 = 1 | 3 = 4
; 1b
```

#### add(n & ns)

Numeric addition.

```rust
? add(1 2 3)
; 6
```

#### burrow(f x y)

Burrows into `x` and `y` with the function `f`.

```rust
? sumsqr:fn(x y) x*x + y*y

? sumsqr(1 4)
; 17
? sumsqr(2 5)
; 29

? sumsqrs:partial(burrow sumsqr)

? sumsqrs([1 2 3] [4 5 6])
; [17 29 45]

? sumsqrs([[1 2] 3] [[4 5] 6])
; [[17 29] 45]
```

#### clj_refer(ns_sym & filters)

A wrapper for Clojure [`refer`](https://clojuredocs.org/clojure.core/refer).

If provided, `filters` must be a dictionary.


```rust
? clj_refer('clojure.string ['only: ['capitalize 'trim]])
? capitalize(trim("  hello world  "))
; Hello world
```

#### clj_require(& args)

A wrapper for Clojure [`require`](https://clojuredocs.org/clojure.core/require).

```rust
? clj_require(['`clojure.java.io` 'as 'io])

? `io/file`("abc.txt")
; #object[java.io.File ...]
```

#### clj_use(& args)

A wrapper for Clojure [`use`](https://clojuredocs.org/clojure.core/use).

```rust
? clj_use(['`clojure.string` 'as 's 'only ['split]])

? `s/replace`("foobar" "foo" "squirrel")
; squirrelbar

? split("hello world" rx(" "))
; [hello world]
```

#### cf(file)

Compile the Motto script file to object code.

#### comp(f & fs), o(f & fs)

Return the composition of the functions.

#### dict(x & xs)

Create a new dictionary.

```rust
? dict('a 1 'b 2)
; [a:1 b:2]
```

#### div(n & ns)

Integer division. Division by zero will raise an exception.

#### eq(x & xs)

Object equality.

#### ex(obj)

Throw any object as an exception.

#### exit(status)

Exit the runtime with the given numeric exit status which defaults to `0`.

#### fork(f g h)

Return the function `fn(x) g(fx) h(x)`.

#### gt(x & xs)

Return `true` if the objects are in monotonically decreasing order, `false` otherwise.

```rust
? gt(3 2 1)
; 1b

? gt(\x \s \a)
; 1b

? gt([10 20] [1 2])
; 1b

? gt([0 1] [10 20] [1 2])
; 0b
```

#### gteq(x & xs)

Return `true` if the objects are in monotonically non-increasing order, `false` otherwise.

#### import(x & xs)

See [https://clojuredocs.org/clojure.core/import](https://clojuredocs.org/clojure.core/import)

```rust
? import('`java.io.File`)

? `File/separator`
; /
```

#### is_dict(x)

Check if the argument is a dictionary.

#### is_double(x)

Return `true` if `x` is a `double` value.

#### is_empty(x)

Return `true` if `x` is the empty sequence `[]`.

#### is_even(n)

Return `true` if `n` is an even number.

#### is_false(x)

Return `true` if `x` is the false value `0b`.

#### is_float(x)

Return `true` if `x` is a `float` value.

#### is_int(x)

Return `true` if `x` is an `int` value.

#### is_list(x)

Return `true` if `x` is a list.

#### is_neg(n)

Return `true` if `n` is a negative number.

#### is_nul(x)

Return `true` if `x` is the `nul` value.

#### is_number(x)

Return `true` if `x` is a number.

#### is_odd(n)

Return `true` if `n` is an odd number.

#### is_pos(n)

Return `true` if `n` is a positive number.

#### is_seq(x)

Return `true` if `x` is a sequence.

#### is_str(x)

Return `true` if `x` is a string.

#### is_true(x)

Return `true` if `x` is the truth value `1b`.

#### is_vec(x)

Return `true` if `x` is a vector.

#### is_zero(n)

Return `true` if `n` is `0`.

#### juxt(f & fs)

Return the juxtaposition of the function calls: `[f0(x) f1(x) ... fN(x)]`.

```rust
? f:fn(x) x + 2
? g:fn(x) x + 3

? juxt(f g)(10)
; [12 13]
```

#### lt(x & xs)

Return `true` if the objects are in monotonically increasing order, `false` otherwise.

#### lteq(x & xs)

Return `true` if the objects are in monotonically non-decreasing order, `false` otherwise.

#### mul(n & ns)

Numeric multiplication.

#### nul()

Return `nul`.

#### num_eq num_gt num_gteq num_lt num_lteq

Numeric comparison.

#### partial(f & args), _(f & args)

Return a `f` with parially applied to `args`.
Call the returned function to complete the computation of `f`.

#### roll(n)

Return a random integer between `0` (inclusive) and `n` (exclusive).

#### size(x)

Returns the number of components in `x`, which must be a sequence or a table.

```rust
? size([1 2 3 4 5])
; 5

? size([2 2]$[1 2 3 4 5])
; 2
```

#### sub(n & ns)

Numeric subtraction.

#### times(n f)

Return a function that applies `f` `n` times to an argument.

```rust
? i3:times(3 inc)

? i3(100)
; 103
```

#### to_double, to_float, to_int

Convert a string to a double, float or int.

```rust
? setprec(10)
; 10

? to_double("12.344443443")
; 12.3444434430

? to_float("12.344443443")
; 12.3444433212

? to_int("12")
; 12
```

#### with_ex(handler f)

Calls the no-argument function `f`. If `f` raises an exception, call `handler` with the raised exception object.

```rust
? with_ex(fn(e) { wr("ERROR: ") wrln(e) 'failed } fn () ex('an_error))
; ERROR: an_error
; failed
```