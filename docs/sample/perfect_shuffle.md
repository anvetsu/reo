# Perfect Shuffle

A program that can perform a [perfect shuffle](http://rosettacode.org/wiki/Perfect_shuffle) on an even-sized list of values.

```sml
perfect_shuffle: fn (xs) flatten(apply(zip split(xs)))
solve_iter: fn (xs ys n) if (eql(ys xs) & is_pos(n) n
                             rec(xs perfect_shuffle(ys) inc(n)))
solve: fn (xs) if (is_even(count(xs)) solve_iter(xs xs 0) 0)
```

A utility for generating a deck with `n` cards:

```sml
deck_of:fn(n) til(1 n+1)
```

Solve the test cases:

```lisp
results:(solve ~ (deck_of ~ [8 24 52 100 1020 1024 10000]))
expected:[3 11 8 30 1018 10 300]

results = expected
; [1b 1b 1b 1b 1b 1b 1b 1b]
```