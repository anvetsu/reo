# Counting Numbers

Count from `start` till `end` (`start` is optional and defaults to `0`):

```lisp
xs:til(1 7)
xs
; [1 2 3 4 5 6]
```

First `n` evens and odds:

```lisp
a:til(10)

a + a
; [0 2 4 6 8 10 12 14 16 18]

a + til(1 11)
; [1 3 5 7 9 11 13 15 17 19]
```

Successors and predecessors:

```lisp
inc(1)
; 2

inc ~ xs
; [2 3 4 5 6 7]

dec ~ xs
; [0 1 2 3 4 5]
```

Predecessors of predecessors will take us to the land of *integers*:

```lisp
dec ~ dec ~ xs
; [-1 0 1 2 3 4]
```

Let's reverse the operation:

```lisp
inc ~ inc ~ dec ~ (dec ~ xs)
; [1 2 3 4 5 6]
```

Being able to apply the same function `n` times is useful:

```lisp
times(3 dec) ~ xs
; [-2 -1 0 1 2 3]

times(3 inc)~times(3 dec) ~ xs
; [1 2 3 4 5 6]
```

Isn't that the same as adding or subtracting by a constant?

```lisp
xs - 3
; [-2 -1 0 1 2 3]

3 + xs - 3
; [1 2 3 4 5 6]
```

Two sequences can be added or subtracted to produce a sequences or tables:

```lisp
a: [0 1 2 3 4 5]
b: [2 3 5 7 11 13]

a+b
; [2 4 7 10 15 18]

a+a
; [0 2 4 6 8 10]

a-a
; [0 0 0 0 0 0]

t:partial(enum `+`)

t(a b)
; [[2 3 5 7 11 13]
;  [3 4 6 8 12 14]
;  [4 5 7 9 13 15]
;  [5 6 8 10 14 16]
;  [6 7 9 11 15 17]
; [7 8 10 12 16 18]]

t(a)
; [[0 1 2 3 4 5]
;  [1 2 3 4 5 6]
;  [2 3 4 5 6 7]
;  [3 4 5 6 7 8]
;  [4 5 6 7 8 9]
;  [5 6 7 8 9 10]]

enum(`-` b [1 2])
; [[1 0]
;  [2 1]
;  [4 3]
;  [6 5]
;  [10 9]
;  [12 11]]
```

## Relations

Any two integers `a` and `b` are related in the following ways

  1. `a` *precedes* `b`
  2. `a` *equals* `b`
  3. `a` *succeeds* `b`

These relations are captured by the `<`, `=` and `>` functions.

```lisp
1 < 2
; 1b

2 = 3
; 0b
```

`1b` stands for `true` (the relation holds) and `0b` stands for `false`.

```lisp
a:til(1 6)
a
; [1 2 3 4 5]

b:6-a

b
; [5 4 3 2 1]

a < b
; [1b 1b 0b 0b 0b]

a > b
; [0b 0b 0b 1b 1b]

a = b
; [0b 0b 1b 0b 0b]

enum(`<` a b)
; [[1b 1b 1b 1b 0b]
;  [1b 1b 1b 0b 0b]
;  [1b 1b 0b 0b 0b]
;  [1b 0b 0b 0b 0b]
;  [0b 0b 0b 0b 0b]]

enum(`=` a b)
; [[0b 0b 0b 0b 1b]
;  [0b 0b 0b 1b 0b]
;  [0b 0b 1b 0b 0b]
;  [0b 1b 0b 0b 0b]
;  [1b 0b 0b 0b 0b]]

enum(`>` a b)
; [[0b 0b 0b 0b 0b]
;  [0b 0b 0b 0b 1b]
;  [0b 0b 0b 1b 1b]
;  [0b 0b 1b 1b 1b]
;  [0b 1b 1b 1b 1b]]
```

Lesser and greater in two lists:

```lisp
sml(a b)
; [1 2 3 2 1]

big(a b)
; [5 4 3 4 5]

enum(sml a)
; [[1 1 1 1 1]
;  [1 2 2 2 2]
;  [1 2 3 3 3]
;  [1 2 3 4 4]
;  [1 2 3 4 5]]
```

**Note** If the arguments are simple integers, you can call `max` and `min` instead of `big` and `sml`.

## Insertion

The fold operator `@` inserts an operator between the items of a sequence:

```
a:[2 7 1 8 2]

`+` @ a
; 20

`*` @ a
; 224

max @ a
; 8
```

You may also use the pre-defined convenience functions:

```lisp
sum(a)
; 20

prd(a)
; 224

mx(a)
; 8
```

## Discovering Multiplication & Power

```lisp
m:3
n:5

n$m
; [3 3 3 3 3]
sum(n$m)
; 15
```

The result shows that multiplication is just repeated addition.

```lisp
n*m
n*m
15
```

In the same line, finding the `n`<sup>th</sup> power of `m` can be achieved by repeated multiplication.

```lisp
prd(n $ m)
; 243

pow(m n)
; 243.0
```

Consider the following relation:

```lisp
pow(3 5) * pow(3 2)
; 2187.0

(pow(3 5) * pow(3 2)) = pow(3 5+2)
; 1b
```

This equivalence can be explained in terms of the earlier definition of `power`:

```lisp
prd(5$3) * prd(2$3)
; 2187

prd((5+2)$3)
; 2187
```

**Reference** [Arithmetic](https://www.jsoftware.com/books/pdf/arithmetic.pdf) by Kenneth E. Iverson