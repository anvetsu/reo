# Useful Recipes

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
; hi
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

## Accesing and Manipulating Sequences

Accessing the first, all-but-first and last elements in a sequence:

```lisp
? xs:[1 2 3 4 5]

? first(xs)
; 1

? rest(xs)
; [2 3 4 5]

? last(xs)
; 5
```

Accessing an element by index in a normal list:

```lisp
? xs(3)
; 4

? get(xs 4)
; 5

? get(xs 5 100)
; 100
```

Accessing an element by index in a lazy-sequence:

```lisp
? xs:range(10 20)

? xs
; [10 11 12 13 14 15 16 17 18 19]

? nth(xs 5)
; 15

? vec(xs)(5)
; 15
```

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

The `append` operator concatenates two lists into one:

```lisp
? [1 2 3]#xs
; [1 2 3 10 34 5]

? xs#[1 2 3]
; [10 34 5 1 2 3]
```

Atomic values are automatically converted to a list:

```lisp
? 100#xs
; [100 10 34 5]
```

## Lift and Drop

Take the first `n` or last `n` elements from a sequence:

```lisp
? xs:til(50)

? lift(5 xs)
; [0 1 2 3 4]

? lift(-5 xs)
; [45 46 47 48 49]
```

Drop the first `n` or last `n` elements from a sequence:

```lisp
? dip(45 xs)
; [45 46 47 48 49]

? dip(-45 xs)
; [0 1 2 3 4]
```

## Compare, Select, Filter

Comparison operators burrow into sequences and returns a list of
boolean values:

```lisp
? xs:[10 89 3 41 52 77]

? xs > 40
; [0b 1b 0b 1b 1b 1b]
```

The select function (`sel`) filters a list by masking it using a sequence of boolean values:

```lisp
? sel(xs > 40, xs)
; [89 41 52 77]
```

The function `filter` is similar to `sel`, but selects values that satisfy a predicate:

```lisp
? filter(^X1 > 40, xs)
; [89 41 52 77]
```

`Filter` returns a lazy-sequence, so it might be ideal for filtering really large lists.

The `mx` (max) function selects the largest value from a sequence, the function `mn` (min) selects the smallest value:

```lisp
? mx(xs)
; 89

? mn(xs)
; 3
```

The functions `big` and `sml` (small) are burrowing versions of `mx` and `mn`:

```lisp
? big(40 xs)
; [40 89 40 41 52 77]

? sml(40 xs)
; [10 40 3 40 40 40]

? big([10 20 30 40 50 60] xs)
; [10 89 30 41 52 77]
```

## Accessing and Manipulating Dictionaries

Adding and removing keys:

```lisp
? data: ['name: "Rachel" 'age: 4]

? assoc(data 'class 1 'div 'A)
; [name:Rachel age:4 class:1 div:A]

? dissoc(data 'age)
; [name:Rachel]
```

Dictionaries, like all sequences, are immutable. The operations `assoc` and `dissoc`
return new dictionaries. The original in left intact:

```lisp
? data
; [name:Rachel age:4]
```

`Map` and `filter` can be applied to dictionaries:

```lisp
? r:map(^{k:first(X1) v:second(X1) [k v*10]}, ['a:1 'b:20 'c:30])
? r
; [[a 10]
;  [b 200]
;  [c 300]]

? r:filter(^{k:first(X1) v:second(X1) is_odd(v)}, ['a:1 'b:2 'c:3])
? r
; [[a 1]
;  [c 3]]
```

These functions return generic sequences of key-value pair.
Such sequences can be coerced into dictionaries:

```lisp
? dict(r)
; [a:1 c:3]
```

## Sorting

```lisp
? sort([10 20 1 4 89])
; [1 4 10 20 89]

? sort(`>` [10 20 1 4 89])
; [89 20 10 4 1]

? sort(^X1('qty) < X2('qty) [['id:1 'qty:100] ['id:2 'qty:43] ['id:3 'qty:560]])
; [[id:2 qty:43]
;  [id:1 qty:100]
;  [id:3 qty:560]]
```

## Table Operations

### Grouping

Consider the following employee table:

```lisp
? emp:['name 'dept 'salary] $ [["Joe" "Sam" "Mat" "Ken" "San"]
                                 [1 2 1 3 2]
				 [1400 1450 1300 1120 1500]]
? emp
; name: [Joe Sam Mat Ken San]
; dept: [1 2 1 3 2]
; salary: [1400 1450 1300 1120 1500]
```

Find the total salary for each department:

```lisp
group(`+` 0 emp('salary) emp('dept))
; [1:2700 2:2950 3:1120]
```

A resuable version of this sum and group-by function could defined as:

```lisp
? sum_grp:partial(group `+` 0)

sum_grp(emp('salary) emp('dept))
; [1:2700 2:2950 3:1120]
```
Count the number of employees in each department:

```lisp
? group_count(emp('dept))
; [1:2 2:2 3:1]
```

### Filtering

Find all employees with salary greater-than `1400`:

```lisp
? where(^X1('salary) > 1400, emp)
; name: [Sam San]
; dept: [2 2]
; salary: [1450 1500]
```

### Merging

Merge a new table with the employee table:

```lisp
? club(emp, ['years_of_service 'age] $ [[12 10 8 15 4] [35 30 36 40 31]])
; name: [Joe Sam Mat Ken San]
; dept: [1 2 1 3 2]
; salary: [1400 1450 1300 1120 1500]
; years_of_service: [12 10 8 15 4]
; age: [35 30 36 40 31]
```

Note that tables are immutable and the `club` operation will return a new table instead of
updating the employee table in-place.

## Set Operations

## Doing Statistics

## Binary Data
