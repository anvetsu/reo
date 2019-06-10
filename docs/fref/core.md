# Function Reference - Core

#### + - * /

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

#### = <> < <= > >=

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

dict

div

eq

ex

exit

fork

gt

gteq

is_dict

is_double

is_empty is_even is_false is_float is_int is_list is_neg is_nul is_number? is_odd is_pos is_seq is_str is_string is_true is_vec is_zero lt lteq mul nul num_eq num_gt num_gteq num_lt num_lteq parse roll size sub times to_double to_float to_int with_ex]