# Sorting

An implementation of quick sort:

```rust
qsort:fn(xs) if (seq(xs) {[b first(xs) xs rest(xs)
                           a qsort(sel(xs < b, xs))
		           c qsort(sel(xs > b, xs))]
                          a#b#c}
	         [])

? qsort([10 20 5 78 2 4 1 9])
; [1 2 4 5 9 10 20 78]
```

For serious programs, always use the built-in `sort` function :-)

```rust
? sort([10 20 5 78 2 4 1 9])
; [1 2 4 5 9 10 20 78]

? sort(num_gt [10 20 5 78 2 4 1 9])
; [78 20 10 9 5 4 2 1]
```
