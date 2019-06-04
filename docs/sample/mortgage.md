# Calculating Mortgages

The following function computes the monthly payment for a loan for the given
monthly rate and number of months.

```lisp
monthly_payment: fn(loan rate months) {
                   x: pow(inc(rate) months)
  		   loan * ((rate * x) / dec(x))
		 }
```

The `make_payment` functions makes single payment. It returns a sequence of
pairs of the current payment and the outstanding loan amount.

```lisp
make_payment: fn(loan rate months) {
                payment: monthly_payment(loan rate months)
	        reduction: payment - loan*rate
	        outstanding: loan - reduction
                println(loan rate months payment reduction outstanding)
	        lazy([payment outstanding]
		     fn() make_payment(outstanding, rate, months))
              }
```

This function essentially returns an infinite sequence of `payments` and `outstandings`.
You make such infinite sequences with the help of the `lazy` function. It takes two arguments

  - a value for the head of the sequence.
  - a zero-argument function that will be called to generate the rest of the sequence.

Here is how you can use `lazy` to generate an infinite number of integers, from a starting value:

```lisp
? nums:fn(n) lazy(n fn()nums(inc(n)))
? xs:nums(10)

"take as many integers as you want: "
? lift(20 xs)
; [10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29]
```

Let's get back to mortgage calculations! The following function will return the total amount paid over a given number of months:

```lisp
? total_paid: fn(payments months) sum(first ~ lift(months payments))
```

How much money is to be paid for 5 months for a loan of 10000 taken for 12 months for a fixed monthly rate of 0.5?
Here is the answer:

```lisp
? total_paid(make_payment(10000 0.5 12) 5)
; 24999.25026320071
```

## Reference

<a href="https://mitpress.mit.edu/books/introduction-computation-and-programming-using-python-second-edition">Introduction to Computation and Programming Using Python</a>.