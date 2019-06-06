# Some Numerical Algorithms

## Harmonic Numbers

The following function generates the first `n` [harmonic numbers](https://en.wikipedia.org/wiki/Harmonic_number):

```rust
? hn:fn(n) (fn(a b) a + 1.0/b) @~ til(1 n+1)

? hn(2)
; [1 1.5]

? hn(10)
; [1 1.5 1.8333333333333333 2.083333333333333 2.283333333333333
;  2.4499999999999997 2.5928571428571425 2.7178571428571425
;  2.8289682539682537 2.9289682539682538]
```

## Square Root

The built-in function `sqrt` can be used to find the square root of a positive number:

```rust
? sqrt(2544545)
; 1595.1630010754386

? sqrt(2)
; 1.4142135623730951
```

As an exercise, let's implement our own version of `sqrt`.
We will be following a technique widely knows as [Newton's method](https://en.wikipedia.org/wiki/Newton%27s_method).


```rust
? tolerance:1.0e-15
? is_good:fn(n x) (x - n/x) > tolerance * x
? guess:fn(n x) (n/x + x) / 2.0
? mysqrt:fn(n) last(take_while(partial(is_good n), listf(partial(guess n), n)))

? mysqrt(2.)
; 1.4142135623746899

? mysqrt(2544545)
; 1595.1630048993925
```

## Prime Factors

```rust
f1:fn(n fact rs) if (is_zero(rem(n fact)) rec(n/fact fact fact;rs) [n rs])
pf:fn(n fact ps) if (num_lteq(fact n/fact) {[nn rs]:f1(n fact []) rec(nn inc(fact) ps#rs)} ps)