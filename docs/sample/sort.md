# Sorting

An implementation of quick sort:

```rust
? qsort:fn(xs) if (seq(xs) {[b first(xs) xs rest(xs)
                             a qsort(sel(xs < b, xs))
                             c qsort(sel(xs > b, xs))]
                           a#b#c}
	           [])

? qsort([10 20 5 78 2 4 1 9])
; [1 2 4 5 9 10 20 78]
```

How can we check is sequence is already sorted or not?

We can do that by checking if each subsequent pair in the sequence is in sorted order.
Here is a function that transforms a sequence into a sequence of "couples":

```rust
? twos:fn(xs) zip(xs rest(xs))

? twos([1 2 3 4 5])
; [[1 2]
;  [2 3]
;  [3 4]
;  [4 5]]
```

We can define the `is_sorted` predicate with the help pf `twos`:

```rust
? is_sorted:fn(xs) all(fn([x y]) x < y, twos(xs))

? is_sorted([1 2 3 4 5])
; 1b
? is_sorted([1 2 3 0 4 5])
; 0b
? is_sorted(qsort([10 20 5 78 2 4 1 9]))
; 1b
```

A parting note: for serious programs, remember to use only the built-in `sort` function :-)

```rust
? sort([10 20 5 78 2 4 1 9])
; [1 2 4 5 9 10 20 78]

? sort(num_gt [10 20 5 78 2 4 1 9])
; [78 20 10 9 5 4 2 1]
```
