# Calculating Mortgages

The following function computes the fixed monthly payment for a loan for the given
monthly rate and number of months.

```lisp
monthly_payment: fn(loan rate months) {
                   x: pow(rate+1.0 months)
 		   loan * ((rate * x) / (x-1.0))
		 }
```

We need a function to return the payments and the outstanding for each month.
It takes the current outstanding loan amount, rate of interest and the current payment to
make as arguments.

```lisp
payseq: fn(loan rate payment) {
          reduction: payment - loan*rate
          outstanding: loan - reduction
          lazy([payment outstanding], fn() payseq(outstanding rate payment))
        }
```

This function essentially returns an infinite sequence of `payments` and `outstandings`.

You make such infinite sequences with the help of the `lazy` function. It takes two arguments:

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

Let's get back to mortgage calculations!

The following function computes the fixed monthly pay and calls `payseq` to return the sequence of
payments and balances, starting from the principal loan amount:

```lisp
make_payment: fn(loan rate months) {
                payment: monthly_payment(loan rate/12 months)
                payseq(loan rate payment)
              }
```

Next we need a function to report the total amount paid for a given period:

```lisp
? total_paid: fn(payments months) sum(first ~ lift(months payments))
```

How much money is to be paid for 5 months for a loan of 10000 taken for 12 months for a fixed monthly rate of 0.5?
Here is the answer:

```lisp
? total_paid(make_payment(10000 0.5 12) 5)
; 5379.255926948909
```

## Reference

<a href="https://mitpress.mit.edu/books/introduction-computation-and-programming-using-python-second-edition">Introduction to Computation and Programming Using Python</a>.