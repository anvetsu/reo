# Munching Data

This document presents some functions and operators that you will find handy
for data processing and analysis. You will be presented with
code recipes. Short explanations will be provided, where that is absolutley required.

## Making Sequences

Create a numeric list:

```lisp
? til(5)
; [0 1 2 3 4]
? til(2 5)
; [2 3 4]
```

You may change the factor of increment:

```lisp
? til(2 10 3)
; [2 5 8]
```

A negative offset will return the list in reverse:

```lisp
? til(-5)
; [4 3 2 1 0]

? til(2, -5)
; [4 3 2]

? til(2, -10 3)
; [8 5 2]
```

The `range` function returns a lazy-numeric-sequence. A lazy sequence is realized only when
elements are actually accessed. This makes it ideal for creating very large sequences.

```lisp
? range(5)
? range(5)
; [0 1 2 3 4]

? range(2 5)
; [2 3 4]

? range(2 10 3)
; [2 5 8]
```

`Range` do not support negative offsets, you have to explicitly reverse the result:

```lisp
? reverse(range(2 10 3))
; [8 5 2]
```

Also note that a lazy sequence cannot be directly indexed, it must be converted to a
normal list (internally known as a `vector`) before it could be directly indexed.

```lisp
? til(1 10)(3)
; 4

? range(1 10)(3)
; ERROR

? vec(range(1 10))(3)
; 4
```

Create `n` instances of an object:

```lisp
? repeat(5 "hi")
; [hi hi hi hi hi]

? vec(repeat(5 "hi"))(0)
hi
```
Create a lazy-sequence by repeatedly applying a function. For example, the following program
returns an infinite sequence of random integers in the range `0-100`:

```lisp
? listf(^roll(100))
; [80 8 91 98 7 97 69 0 91 27 ...]
```

`Listf` can also accept a function that takes a single-argument. In that case, `listf` needs an extra argument
to seed the first call to the function. Each subsequent call will be seeded by the preceding result. Here is
function that returns a sequence of square-roots, starting at `10`:

```lisp
listf(sqrt 10)
; [10 3.1622776601683795 1.7782794100389228 1.333521432163324 ...]
```

## Insert, Append

Insert a new element to the beginning or end of a list using the `concat` (`;`)
operator. (Do not confuse this operator with the `;` prefix of the output):

```lisp
? xs:[10 34 5]
? xs
; [10 34 5]

? 2;xs
; [2 10 34 5]

? xs;2
; [10 34 5 2]

? [1 2 3];xs
; [1 2 3 [10 34 5]]
? xs;[1 2 3]
; [10 34 5 [1 2 3]]
```

## Assoc, Dissoc, Merge

## Lifting and Dropping

## Filtering and Selecting

## Grouping

## Set Operations

## Doing Statistics

## Binary Data
