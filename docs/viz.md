# Visualizing Data

Visual aids are an excellent means for communicating information.

Motto has built-in visualization facilities that enable you to present
results of data analysis in an appealing manner.

The following example shows how to graphically view the projects returns from an investment
over the next 20 years:

```lisp
principal: 10000
interest_rate: 0.05
years: 20
returns: (^X1 + X1 * interest_rate) @~ repeat(years principal)

plot('xy til(years) returns
     ['xlabel: "Years of compounding" 'ylabel: "Value of principal"])
```

The `plot` function will produce the following chart on screen:

![projected returns](docs/images/rets.png)

;; TODO