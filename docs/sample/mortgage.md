# Calculating Mortgages

The following function computes the fixed monthly payment for a loan for the given
monthly rate and number of months.

```sml
monthly_payment: fn (loan rate months) {
                   r: rate
                   x: pow(r+1.0 months)
		   loan * ((r * x) / (x-1.0))
		 }
```

We need a function to return the payments and the outstanding for each month.
It takes the current outstanding loan amount, rate of interest and the current payment
as arguments. The return value is an [infinite sequence](../tut.md#lazy) of `payments` and `outstandings`.

```sml
payseq: fn (loan rate payment) {
          reduction: payment - loan*rate
          outstanding: loan - reduction
          lazy([payment outstanding], fn () payseq(outstanding rate payment))
        }
```

The following function computes the fixed monthly pay and calls `payseq` to return the sequence of
payments and balances, starting from the principal loan amount:

```sml
make_payment: fn (loan rate months) {
                rate: rate/12.0
                payment: monthly_payment(loan rate months)
                [[0.0 loan]]#payseq(loan rate payment)
              }
```

Next we need a function to report the total amount paid for a given period:

```sml
total_paid: fn (payments months) sum(first ~ lift(inc(months) payments))
```

How much money is to be paid for 12 months for a loan of 10000 taken for 24 months for a fixed monthly rate of 0.5? Here is the answer:

```lisp
floor(total_paid(make_payment(10000 0.5 24) 12))
; 8005.0
```

### Mortgage with Points

Some mortgages allow lower interest rates if "points" are paid to the lender at the time of taking the mortgage.
In this example, we define a point as a `1%` cash payment of the total value of the loan.

The following extended version of the `make_payment` function accommodates the `points` purchased by the client.
This is used for computing the initial payment made:

```sml
make_pts_payment: fn (loan rate months points) {
                   rate: rate/12.0
                   payment: monthly_payment(loan rate months)
                   [[loan*(points/100.0), loan]]#payseq(loan rate payment)
              }
```

The total payment over 5 months is recomputed as follows, if the client is willing to buy 7 points. He is given an reduced rate of
0.2:

```lisp
floor(total_paid(make_pts_payment(10000 0.2 24 7) 12))
; 6807.0
```

### Mortgage with Variable Rate

```sml
make_vr_payment: fn (loan rate months vrate vmonths) {
                   rate: rate/12.0
		   vrate: vrate/12.0
                   vr_pay: lift(inc(vmonths) make_payment(loan vrate vmonths))
		   mons: months - vmonths
		   norm_pay: lift(mons make_payment(second(last(vr_pay)) rate mons))
		   vr_pay#norm_pay
                 }
```

**Credits** - This example is based on Chapter 8 of <a href="https://mitpress.mit.edu/books/introduction-computation-and-programming-using-python-second-edition">Introduction to Computation and Programming Using Python</a>.

[Back](../sample.md)