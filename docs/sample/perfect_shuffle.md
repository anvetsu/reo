# Perfect Shuffle

A program that can perform a [perfect shuffle](http://rosettacode.org/wiki/Perfect_shuffle) on an even-sized list of values.

```lisp
perfect_shuffle:fn(xs) flatten(apply(zip split(xs)))
solve_iter:fn(xs ys n) if (eql(ys xs) & is_pos(n) n
                           rec(xs perfect_shuffle(ys) inc(n)))
solve:fn(xs) if (is_even(count(xs)) solve_iter(xs xs 0) 0)
```

A utility for generating a deck with `n` cards:

```lisp
deck_of:fn(n) til(1 n+1)
```

Solve the test cases:

```lisp
results:(solve ~ [deck_of(8) deck_of(24) deck_of(52) deck_of(100)
                  deck_of(1020) deck_of(1024) deck_of(1024) deck_of(10000)])
expected:[3 11 8 30 1018 10 10 300]

results = expected
; [1b 1b 1b 1b 1b 1b 1b 1b]
```