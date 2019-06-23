# Raw Records

Consider the following table that records the employee name, pay per hour and total hours worked:

```lisp
emp:['name 'pay_per_hr 'hrs] $ [['Kate 'Sam 'Mary 'Sue 'Dan 'Beth]
                                [40 40.5 35 30 42 40]
				[12 10 15 0 0 10]]

emp
; name: [Kate Sam Mary Sue Dan Beth]
; pay_per_hr: [40 40.5 35 30 42 40]
; hrs: [12 10 15 0 0 10]
```

Let's first consider some computations that are straightforward on this columnar table.

1. Find all employees with hourly pay greater than 40:

```lisp
where(^X1('pay_per_hr) > 40, emp)
; name: [Sam Dan]
; pay_per_hr: [40.500 42]
; hrs: [10 0]
```

2. Find all employees whose total pay exceeds 450:

```lisp
where(^X1('pay_per_hr) * X1('hrs) > 450, emp)
; name: [Kate Mary]
; pay_per_hr: [40 35]
; hrs: [12 15]
```

3. The number of employees who worked more that 2 hours:

```lisp
count((^X1 > 10) ! emp('hrs))
; 2
```

4. Total pay and average pay:

```lisp
sum(map(^X1 * X2, emp('pay_per_hr) emp('hrs)))
; 1810.000

mean(map(^X1 * X2, emp('pay_per_hr) emp('hrs)))
; 301.667
```

For more descriptive reporting, a record based table might be more ideal.
So we first `flip` the table and then get a handle to its data:

```lisp
emp_data:data(flip(emp))

emp_data
; [[Kate 40 12]
;  [Sam 40.5 10]
;  [Mary 35 15]
;  [Sue 30 0]
;  [Dan 42 0]
;  [Beth 40 10]]
```

The following program shows the total pay for those employees who worked for more than zero hours:

```lisp
xs:(^X1(2) > 0) ! emp_data
xs
; [[Kate 40 12]
;  [Sam 40.500 10]
;  [Mary 35 15]
;  [Beth 40 10]]

ys:(^[X1(0) X1(1) * X1(2)]) ~ xs
ys
; [[Kate 480]
;  [Sam 405.000]
;  [Mary 525]
;  [Beth 400]]
```

The payments made to the employees can be printed using the [`doseq`](https://clojuredocs.org/clojure.core/doseq) special form:

```lisp
doseq([y ys] wrln(str("total pay for " y(0) " is " y(1))))
; total pay for Kate is 480
; total pay for Sam is 405.0
; total pay for Mary is 525
; total pay for Beth is 400
```

Fancier output can be produced with the help of the [`format`](https://clojuredocs.org/clojure.core/format) function:

```lisp
doseq([y ys] wrln(format("%-8s %6.2f" y(0) float(y(1)))))
; Kate     480.00
; Sam      405.00
; Mary     525.00
; Beth     400.00
```

You can create a new table by merging in the total pay:

```lisp
zs:vec(vec ~ map(`#` xs second ~ ys))
zs
; [[Kate 40 12 480]
;  [Sam 40.500 10 405.000]
;  [Mary 35 15 525]
;  [Beth 40 10 400]]
```

Note that we forced the entire result into a vector so that elements can be accessed directly by index.

Now let's print a new report in increasing order of payment:

```lisp
p:fn(z) wrln(format("%-6s %3.2f %3d %6.2f" z(0) float(z(1)) z(2) float(z(3))))
ss:sort(^X1(3) < X2(3), zs)

doseq([s ss] p(s))
; Beth   40.00  10 400.00
; Sam    40.50  10 405.00
; Kate   40.00  12 480.00
; Mary   35.00  15 525.00
```