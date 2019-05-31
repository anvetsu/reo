# Visualizing Data

```lisp
principal: 10000
interest_rate: 0.05
years: 20
returns: (^X1 + X1 * interest_rate)@~repeat(years principal)

plot('xy til(years) returns
     ['xlabel: "Years of compounding" 'ylabel: "Value of principal"])
```