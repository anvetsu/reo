# Reo - A Primer

Computing is mostly about consuming data and interpreting it to create useful information.
Data comes in all forms imaginable - numbers, texts, lists, tables, images and so on.
Fortunately, we have digital computers that can be programmed to deal with data at all
scale and complexity.

As the representation of data becomes more complex, computer users tend to depend on specialized tools.
For example, spreadsheet programs are used for visualizing and manipulating tabular data.

In contrast, Reo strives to be a powerful tool that can deal with data in many forms
and shapes. The user interface to Reo is an elegant programming language that you can pick up
in a weekend. This makes Reo a more expressive and scalable tool than a point-and-click GUI program.

It is hoped that this tutorial will convince you of the veracity of the preceding paragraph.

This document is divided into the following sections:

1. [Simple Calculations](#simp)
2. [Variables](#vars)
3. [Vectors and Lists](#lists)
4. [Multiple Dimensions](#dims)
5. [Reductions and Deductions](#reds)
6. [Indexing and Mapping](#idx)
7. [Dictionaries and Tables](#dicts)
8. [Functions](#funs)
9. [Control Flow](#conds)
10. [Dealing with Errors](#err)
11. [Scripts and Compilation](#scripts)
12. [Remote Evaluation](#remote-evaluation)
13. [Integration with the JVM](#jvm)
14. [Conclusion](#conclusion)

<a name="simp"></a>
## Simple Calculations

For simple data objects, like numbers and booleans, Reo behaves like any traditional programming
language:

```lisp
157.89 + 34.19
; 192.080

10 - 2 * 5
; 0
```

**Note** The precision of decimal numbers are limited to 3 digits after the decimal point only in printed output.
Internally they are stored with more precision. The output of decimal digits can be customized by the `setprec` function:

```lisp
setprec(14)
; 14

157.89 + 34.19
; 192.07999999999998
```

For convenience of display, this tutorial will continue to show decimal results with 3 (or sometimes even less) digits of precision.


Large numbers may be written with an underscore for ease of reading:

```lisp
1_50_000 * 45_000
; 6750000000
```

A number in a particular base can be entered with the `0base_` prefix:

```lisp
02_1110011
; 115

016_FF
; 255
```

Division of integers will result in an exact real value.

```lisp
56 / 2
; 28

56 / 3
; 56/3
```

Divisions that involve an inexact floating-point number will produce an inexact result.

```lisp
56 / 3.0
; 18.667
```

Division by zero will return a constant that represents infinity, which can be used in other
arithmetic operations. The only catch is that any operation that involve infinity will return infinity.

```lisp
56/0
; inf

10 + 56 / 0
; inf
```

The comparison and logical operators return boolean values. Boolean literals are represented compactly as `1b` (true)
and `0b` (false).

The comparison operators are:

 * `=`  - equal-to
 * `<>` - not equal-to
 * `<`  - less-than
 * `>`  - greater-than
 * `<=` - less-than or equal-to
 * `>=` - greater-than or equal-to

The logical operators are:

 * `&` - and
 * `|` - or

Some examples:

```lisp
1 < 1+1
; 1b

1 < 1+1 & 3 >= 4
; 0b

1 < 2 | 3 >= 4
; 1b
```

#### Multi-line expressions

Long expressions can be split across multiple lines by ending each line with a backward slash (`\`).
The REPL will mark each new line of the expression with the `.. ` prompt.

The following example shows how to split the expression `(2 + 2 * 3) / 2` across multiple lines.

```lisp
=> (2 + 2 \
.. * 3 \
.. ) / 2
; 4
```

Expressions enclosed in `( )`, `[ ]` and `{ }` can be split to multiple space without adding trailing spaces:

```lisp
[1 2 3
.. 4 5]
; [1 2 3 4 5]
```

### Textual Data

Strings are enclosed in double-quotes. Character literals are prefixed by a `\`.

```lisp
"hello world"
; hello world

\a
; a
```

Non-visual characters may be written more descriptively as `\space` and `\tab`.

String constants may also be represented as *symbols*. A symbol is an identifier prefixed by a single-quote (').
Symbols that contain white-spaces or other special characters must be enclosed in tick-quotes (`).

```lisp
'red
; red

'`hello world`
; hello world

'`+++---`
; +++---
```

Symbols are more efficient than strings because two symbols made of the same characters share the same space in memory.

### Date and Time

The `now` function will return the current date-time:

```lisp
now()
; dt("2019-06-02T10:18:20")
```

Date and time is encoded in the format `YYYY-MM-DDThh:mm:ss`. A string in this format can be converted to a date object by calling the
`dt` function. A date is converted back to a string by the `sdt` function.

```lisp
a:dt("2018-01-02T14:30:23")
a
; dt("2018-01-02T14:30:23")

sdt(a)
; 2018-01-02T14:30:23
```

The `dtget` function can access the various parts of a date. For example, to access the year field, we can call this function as:

```lisp
dtget(a 'y)
; 2018
```

The `dtadd` function is used for changing the value of any part of a date. Note that this function returns a new date object, and leaves
the original date unchanged:

```lisp
dtadd(a 'y 10)
; dt("2028-01-02T14:30:23")

dtadd(a 'y, -2)
; dt("2016-01-02T14:30:23")
```

The valid field flags that can be passed to `dtget` and `dtadd` are:

```lisp
'y  - year
'M  - month
'd  - day_of_month
'D  - day_of_year
'dW - day_of_week
'dM - day_of_week_in_month
'H  - hour_of_day
'm  - minute
's  - second
```

An invalid flag will cause the functions to fallback to `day_of_year`.

<a name="vars"></a>
## Variables

Variables are created using the `:` (define) operator. This operator will return the symbolic name of the
new variable. (In the sample code we have left out these return values).

```lisp
price:59.75
qty:3
tot:price * qty
tot
; 179.25
```

#### Multiple bindings

Multiple variables can be bound in a single expression by specifying the variables and corresponding values in lists:

```lisp
[a b c]:[100 200+10 45/5]

a
; 100

b
; 210

c
; 9
```

The values list must have as many expressions as there are variables, it may have more but not less.

### Controlling scope

Scope or visibility of variables can be controlled by declaring them within "code-blocks". A code-block is any sequence
of program expressions delimited by opening and closing curly-braces (`{` and `}`).

In the following program, the global definition of `x` is "shadowed" by the local definition of `x` in the code-block:

```lisp
x:10
y:20

x+y
; 30

{[x 100] x+y}
; 120

x+y
; 30
```

Note that variable declarations in a code-block are specified as a simple list of variable names and values.

```lisp
{[x 1 y 2] x+y}
; 3
```

Also keep in mind that the `define` operator (`:`), even when used in a code-block, will create a global variable.
The following program will change the global value of `y`, while preserving the global value of `x`:

```lisp
{[x 100] y:200 x+y}
; 300

x
; 10

y
; 200
```

Code-blocks can be defined just to group together program statements, without introducing new local bindings:

```lisp
{ wr('hello)
    wr(\space)
    wr('world)
    newln() }
; hello world
```

The statements may be optionally separated using comma.

<a name="lists"></a>
## Vectors and Lists

Reo can also operate on vectors of values, delimited by opening and closing square brackets.
Vectors support access to items by index in log32N hops.

Assume that we have to find the total price of not one by five products. The prices and corresponding
quantities can be represented as two vectors:


```lisp
price:[59.75 34.0 55.25 60.5 23.5]
qty:[3 5 3 2 4]
```

**Note** You may separate each element in a vector by commas but this is optional.
The comma separator is required while mixing positive and negative numbers. E.g: `[2, -1 3]`. If the
comma separator is left out, the vector returned will be `[1 3]` instead of the expected `[2 -1 3]`.

The total costs can be found by simply multiplying the two vectors together:

```lisp
cost: price * qty
cost
; [179.25 170.0 165.75 121.0 94.0]
```

As we can see here, the arithmetic operators can operate on vectors as well as they work with individual numbers.
This is true for comparison operators and many other built-in functions. This "burrowing" behavior extends to vectors
of more complex shapes and structures. This advanced vector processing capability eliminates the need for imperative,
"loopy" code common in many languages.

You may freely mix numbers and vectors in an arithmetic expression. For example, you can add a `5%` tax to the prices as,

```lisp
tax:0.05
price + tax * price
; [62.738 35.7 58.013 63.525 24.675]
```

### Lists

Lists are made of nodes linked by references. They are constructed by calling the `list` function.

```lisp
xs:list(1 2 3 4 5)
xs
; [1 2 3 4 5]
```

New elements can be added to a list's head in constant time. So they are suitable for being used as stacks:

```lisp
peek(xs)
; 1

pop(xs)
; [2 3 4 5]

push(pop(xs) 10)
; [10 2 3 4 5]
```

Both vectors and lists are instances of more generic types known as sequences. So they share some of the
basic functions, though the behavior of functions like `push`, `peek` and `get` are optimized for the actual underlying
structure.

```lisp
xs:[1 2 3 4 5]
ys:list(1 2 3 4 5)

is_vec(xs)
; 1b
is_list(ys)
; 1b

count(xs)
; 5
count(ys)
; 5

push(xs 10)
; [1 2 3 4 5 10]
push(ys 10)
; [10 1 2 3 4 5]
```

<a name="lazy"></a>
### Infinite Sequences

A sequence need not contain a finite number of elements. It could be infinite!
You make such infinite sequences with the help of the `lazy` function, which takes two arguments:

  - a value for the head of the sequence.
  - a zero-argument function that will be called to generate the rest of the sequence.

Here is how you can use `lazy` to generate an endless sequence of integers, given a starting value:

```lisp
nums:fn(n) lazy(n fn()nums(inc(n)))
xs:nums(10)

"take as many integers as you want: "
lift(20 xs)
; [10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29]
```

**Note** The `fn` keyword defines a new function. We will learn more about functions in a [later section](#funs).

<a name="dims"></a>
## Multiple Dimensions

Sequences can be used to model data with multiple dimensions. As an example, consider the temperature forecasts (in Celsius) for two cities
for the next 7 days:

```lisp
forecast:[[37 36 37.5 37 35.4 33 35]
            [38 38.3 37 35 37 36.5 37]]
```

The `dim` function can be used to find out the dimension of complex sequences:

```lisp
dim(forecast)
; [2 7]
```

This output means `forecast` is a sequence with `2` rows and `7` columns.

After 7 days we receive the actual temperature readings as a single sequence with 14 entries.
The first 7 entries pertain to the first city and the next 7 entries are for the second city.

```lisp
sensor_data:[36 35 37 36 35 34 35 36 37 38 37 37 36 35]
assert(count(sensor_data) = 14)
```

Before we can use this for comparison with the forecast, we need to mold this sequence into the
appropriate shape or dimension. We can use the `tab` (tabulate) function for this.
The `tab` function takes two arguments - the dimension and the sequence of data that needs to be tabulated.

```lisp
actual:tab([2 7] sensor_data)
actual

; [[36 35 37 36 35 34 35]
;  [36 37 38 37 37 36 35]]
```

**Note** Just as for sequences, the comma separator is optional for function arguments.

If you find yourself tabulating too much, you may save a few keystrokes by using the `tab` operator (`$`):

```lisp
[2 7] $ sensor_data

; [[36 35 37 36 35 34 35]
;  [36 37 38 37 37 36 35]]
```

Now that we have the forecast data and actual data, one basic question we would like to answer is
how much the actual temperature readings varies from the forecastAs the arithmetic operators can burrow into
sequences of any dimension, the solution is the following simple program:

```lisp
variance:actual - forecast
variance

; [[-1 -1 -0.5 -1 -0.399 1 0]
;  [-2 -1.299 1 2 0 -0.5 -2]]
```

<a name="reds"></a>
## Reductions and Deductions

Earlier in this tutorial, we calculated the cost of purchase of a sequence of products:

```lisp
cost
; [179.25 170.0 165.75 121.0 94.0]
```

We find the total cost by adding together all elements in the sequence.
This can be achieved with the help of the `fold` operator denoted by the `@` symbol:

```lisp
`+` @ cost
; 730.0
```

As `+` is an operator the function attached to it is extracted by enclosing it in tick-quotes (`).

Now what does the fold (`@`) operator doIt basically inserts the `+` function between all the elements of
the sequence and then evaluate the resulting expression:

```lisp
`+` @ cost => 179.25 + 170.0 + 165.75 + 121.0 + 94.0 => 730.0
```

The fold operator is defined in terms of a lower level function called `reduce`. This function takes three
arguments: a function that is inserted between the elements, an initial value to start the folding and the sequence to be
folded.

```lisp
reduce(`+` 0 cost)
; 730.0
```

Convenience functions for folding a sequence using the arithmetic operators are built-in - `sum` for `+`, `dif` (difference) for `-`,
`prd` (product) for `*` and `qt` (quotient) for `/`:

```lisp
sum(cost)
; 730.0

dif([4 3 2])
; -1

prd([5 5 10])
; 250

qt([100 5 2])
; 10
```

Let's use fold for finding the average cost:

```lisp
sum(cost) / count(cost)
; 146.0
```

That was just an exercise! Normally, you should use the built-in `mean` function for this:

```lisp
mean(cost)
; 146.0
```

<a name="idx"></a>
## Indexing and Mapping

A vector can be called like a function, with an index as argument. The value at that index will be returned.
vector indices starts at `0`.

```lisp
xs:[10 78 34 90]
xs(0)
; 10

sqrt(xs(2))
; 5.831
```

What if want to apply a function to each element in a sequence and construct a sequence of the results?
We can do that with the `map` function:

```lisp
map(sqrt xs)
; [3.162 8.832 5.831 9.487]
```

Mapping a function over a sequence is a common operation, so we have an operator for that represented by the `~` (tilde) symbol.

```lisp
sqrt ~ xs
; [3.162 8.832 5.831 9.487]
```

As a sequence itself is treated as a function, we can map it over a sequence of indices to extract a subset of the sequence:

```lisp
xs ~ [0 2 3]
; [10 34 90]
```

We can combine the fold and map operators to return the accumulated results
at each stage of the fold:

```lisp
`+` @~ [1 2 3 4 5]
; [1 3 6 10 15]
```

Here is a more practical example of `map`. We are given the following information about 5 employees in a company:

```lisp
salary:[1500 2300 1200 3000 1250]
category:[0 2 1 0 1]
```

The management has decided to give a salary increment to employees in each category based on these fixed rates:

 - Category 0: 500
 - Category 1: 200
 - Category 2: 350

These rates is represented by the vector:

```lisp
rate:[500 200 350]
```

The increment applicable to each employee can be found out by mapping the `rate` vector over the `category` vector:

```lisp
rate ~ category
; [500 350 200 500 200]
```

The actual increment can be computed by adding these rates to the `salary` vector:

```lisp
(rate ~ category) + salary
; [2000 2650 1400 3500 1450]
```

Those are the new salaries!

<a name="dicts"></a>
## Dictionaries and Tables

A dictionary is a data structure which associates names (keys) to values.

Here is an example of using a dictionary to represent an employee record:

```lisp
emp:['name: "J Kale" 'dept: 101 'salary: 1500]
```

A dictionary is indexed by keys:

```lisp
emp('name)
; J Kale

emp('salary)
; 1500
```

If a non-existing key is looked-up, a `nul` (null) value is returned.
The function `is_nul` can be used to check if a value is `nul` or not.

```lisp
is_nul(emp('age))
; 1b
```

The `get` function also may be used to lookup keys. This function can accept an optional argument
that will be returned instead of `nul` for missing keys.

```lisp
is_nul(get(emp 'age))
; 1b

get(emp 'age 45)
; 45
```

Vectors of dictionaries form tables. Here is a database of employee records:

```lisp
db:[['name:"J Kale"  'dept:101 'salary:1500]
      ['name:"M Sally" 'dept:100 'salary:2000]
      ['name:"K Joe"   'dept:101 'salary:1400]]
```

How will you find the total salaryWith the help of `map` and `sum`! First let's extract the salaries:

```lisp
sals:'salaries ~ db
sals
; [1500 2000 1400]
```

A symbol can also behave like a function. When applied to a dictionary, the symbol will extract the associated value.

```lisp
'salary(emp)
; 1500
```

To extract all salaries in the `db` vector, we just map the symbol `'salary` over it. That's how we obtained the
value for the sequence `sals`.

Call `sum` on this sequence, and we have the total salary!

```lisp
sum(sals)
; 4900
```

### Columnar tables

If we perform a lot of aggregations like this, it will be more efficient to store tables in a different format, as shown below:

```lisp
db:['a: [1 2 3 4 5]
      'b: [10 20 30 40 50]]
```

Now aggregations can be computed without the extra call to `map`.

```lisp
sum(db('a))
; 15
```

The `tab` function can be used to easily create such "columnar" tables. Let's re-define the employee table
in this format:

```lisp
db:['name 'dept 'salary] $ [["J Kale" "M Sally" "K Joe"]
                              [101 100 101]
			      [1500 2000 1400]]

db
; name: [J Kale M Sally K Joe]
; dept: [101 100 101]
; salary: [1500 2000 1400]
```

Computing the total salary is now more straightforward:

```lisp
sum(db('salary))
; 4900
```

Column names and data may be queried separately from the table:

```lisp
fields(db)
; [name dept salary]

data(db)
; [[J Kale M Sally K Joe]
;  [101 100 101]
;  [1500 2000 1400]]
```

If required, a columnar table could be "flipped" into a record-based store:

```lisp
rec_db:flip(db)
rec_db
;    name    dept  salary
; ---------------------------------------
;   J Kale     101    1500
;  M Sally     100    2000
;    K Joe     101    1400

fields(rec_db)
; [name dept salary]

data(rec_db)(0)
;[J Kale 101 1500]
```

And yes, a record-based table can be flipped back into a columnar store!

```lisp
flip(rec_db)
; name: [J Kale M Sally K Joe]
; dept: [101 100 101]
; salary: [1500 2000 1400]
```

We will learn more about tables in the [next chapter](data.md).

<a name="funs"></a>
## Functions

So far we have used many functions that come built-in with Reo - `map`, `sum`, `reduce` and so on.
We have seen that the operators like `+`, `*` and `=` themselves are functions.

In this section we will see how we can define our own functions. A function is created by the `fn` keyword, followed
by the function parameters (enclosed in brackets) and the function body.

Here is how we will define a function that doubles its argument:

```lisp
dbl:fn(a) a + a
```

There is no explicit `return` statement, the value of the last expression in the function will be automatically returned.

Functions are also data, so we can bind a function to a variable. In the above example, we have
bound the doubling function to the name `dbl`.

As the function internally uses the burrowing `+` operator, `dbl` can seamlessly work with sequences as well as single numbers.

```lisp
dbl(10)
; 20

dbl([1 2 3])
; [2 4 6]

dbl([[2 3] 4 5 [[[6 7]]]])
; [[4 6] 8 10 [[[12 14]]]]
```

As another example, let's define a function that increments an amount by a percentage rate:

```lisp
incr:fn(percent amount) amount + amount * (percent/100.0)
```

The following are all valid ways we can use this two-argument function:

```lisp
incr(100 1000)
; 2000.0

incr(50 1000)
; 1500.0

incr([10 5 50] [1000 2000 3000])
; [1100.0 2100.0 4500.0]

incr(5 [1000 2000 3000])
; [1050.0 2100.0 3150.0]
```

### Function literals

As functions are the primary means of abstraction in the language, there is a shorthand operator for defining them.
The `fn` keyword and the parameter list could be replaced by the `^` operator. A function defined this way can accept
an arbitrary number of arguments. Each argument can be referenced in the body with the variables `X1`, `X2`, `X3` and so on.

Usually the `^` operator is used to define functions used only in a limited local scope.

Here is `incr` again, defined as a use-once function and applied to the arguments `(50 1000)`:

```lisp
(^X2 + X2 * (X1/100.0))(50 1000)
; 1500.0
```

The following program shows how to compute the first `10` numbers of the Fibonacci sequence
by applying the `fold-times` (`@>`) operator to a function literal:

```lisp
(^X1;sum(lift(-2 X1))) @> 10 [1 1]
; [1 1 2 3 5 8 13 21 34 55 89 144]
```

The `fold-times` operator is an extension to the basic `fold` operator (`@`).
This operator starts with an initial sequence and extends it with the result of passing that to a function.
The function gets the extended sequence for each call.

### Partials

Functions may be *partially applied*, to only a subset of its arguments. For example,
the following program partially applies the `+` function to `5`. This operation will return a new function
that, when called with an argument will complete the addition.

```lisp
add5:_(`+` 5)

add5(10)
; 15

add5([1 2 3 4 5])
; [6 7 8 9 10]
```

The `_` symbol denotes the `partial` function. You may also explicity write `partial` instead of `_`.

Partial application is an important functional programming technique. We will call the `partial` function
often while writing significant programs.

### Compositions

Another technique for building new functions out of existing ones is through *function composition*.
For instance, imagine that you want to find the square-root of the sum of all numbers in a vector.
Here is how you would express it as a *composition* of the functions `sqrt` and `sum`:

```lisp
sqrt(sum([1 2 3 4 5]))
; 3.873
```

If this is a frequent computation, you can instantiate a new function defined as the composition of `sqrt` and `sum`
and apply that to any sequence.

```lisp
sqsum:^sqrt(sum(X1))

sqsum([1 2 3 4 5])
; 3.873
sqsum([10.5 90.2 5.6])
; 10.310
```

The `compose` built-in function, denoted by `o`, can ease the task of defining such compositions:

```lisp
sqsum:o(sqrt sum)

sqsum([1 2 3 4 5])
; 3.873
sqsum([10.5 90.2 5.6])
; 10.310

primes:[2 3 5 7 11]
b:inc~primes

b
; [3 4 6 8 12]

o(inc inc)~b
; [5 6 8 10 14]
```

**Note** You may write `comp` instead of `o`, both identifiers are bound to the compose function.

The composition for incrementing `n` number of times can be created by the `times` function:

```lisp
i3:times(3 inc)
i3 ~ primes
; [5 6 8 10 14]

d3:times(3 dec)
d3 ~ (i3 ~ primes)
; [2 3 5 7 11]
```

Well that was just for illustrating the various ways you can build composed functions.
With burrowing arithmetic, we could've just done:

```lisp
primes + 3
; [5 6 8 10 14]

primes + 3 - 3
; [2 3 5 7 11]
```

### Forking

A `forking` function call is of the form: `g(f(x) h(x))`. The `fork` function can create a function composition that satisfies this form.

```lisp
mnmx:fork(mn `#` mx)

mnmx([10 3 4 50 23])
; [3 50]
```

The `#` operator represents a function that can append two values into a single sequence.

Without `fork`, we would've had to write this as:

```lisp
mn([10 3 4 50 23]) # mx([10 3 4 50 23])
; [3 50]
```

### Optional and named arguments

In an earlier section we saw that functions defined with the `^` operator accepts an arbitrary number of arguments.
Each of these arguments has to be explicitly referenced with variables names `X1...XN`. What if we want to define a function
with `n` number of required parameters and `x` optional parametersThe following example will show you how to do this:

```lisp
prn_args:fn(x & xs) { wrln(x) wrln(xs) }

prn_args(10)
; 10

prn_args(10 20 30 40)
; 10
; [20 30 40]
```

The function `prn_args` is defined to take `x` as required argument and an arbitrary number of optional arguments.
The optional arguments are collected in a sequence named `xs`. If no optional arguments are passed, `xs` will be `nul`.
The function just writes its arguments to standard output and does nothing else.

**Note** The function `wrln` stands for "write-line". It writes the textual representation of an object followed by a newline
to the current output stream. The function `wr` (write) is similar, but does not emit the newline. You may explicitly
emit a newline by calling the `newln` function.

Functions with named arguments with default values can be emulated with the help of `^` and dictionaries.
The following program shows an example. A function is defined to take two arguments `x` and `y`. They default
to the values `10` and `20` respectively:

```lisp
f:^{[x get(X1 'x 10), y get(X1 'y 20)] x+y}

f()
; 30

f(['x: 100])
; 120

f(['x: 100 'y: 200])
; 300
```

### Applying Functions

What if the number of arguments to a function cannot be determined at compile time?
We can use the `apply` function to call that function with a list of arguments. This list could be
constructed while the program is running.

```lisp
xs:[1 2 3 4]
ys:push(xs 100)

apply(prn_args ys)
; 1
; [2 3 4 100]
```

In fact, `apply` itself can take an arbitrary number of arguments, only the last one needs to be a sequence:

```lisp
apply(prn_args 1 2 3 [10 20])
; 1
; [2 3 10 20]
```

### Argument Destructuring

Function parameters can be defined as data-patterns of vectors and dictionaries.

The following example shows how to define a function with a vector pattern:

```lisp
f:fn([x y z]) x+y+z

f([10 20 30])
; 60
```

When the function is called, the argument list is destructured according to the pattern and its elements are assigned to
the variables in the pattern. Note that a pattern variable will default to `nul`.

The next function destructures its dictionary argument:

```lisp
g:fn([a:'a b:'b]) a + b

g(['a:10 'b:20])
; 30
```

The `define` operator and variable bindings in code-blocks can also destructure sequences and dictionaries:

```lisp
[a b c]:[10 20 30]

a+b+c
; 60

[x:'x y:200]:['x:20 200:10]

x+y
; 30

{[[a b c] [1 2 3]] a+b+c}
; 6
[a b c]
; [10 20 30]

{[[x:'x y:200] ['x:2 200:1]] [x y]}
; [2 1]
[x y]
; [20 10]
```

<a name="conds"></a>
## Control Flow

The basic mechanism for conditional execution of code is the `if` construct.
This is a special construct built into the language, but its syntax is similar to a function call.

```lisp
if (cond1 conseq1 cond2 conseq2 ... alternative)
```

The arguments passed to `if` must be a sequence of conditions and consequences.
If a condition returns `true` (`1b`), its consequence is evaluated.
If none of the conditions evaluate to `true`, the last expression passed as argument is evaluated.
This last `alternative` expression is optional and defaults to `false` (`0b`).

```lisp
if (1<2 100)
; 100

if (1>2 100)
; 0b

if (1>2 100
      3=3 200)
; 200

if (1>2 100
      3<>3 200
      400)
; 400
```

### Repeating yourself

Reo does not have imperative looping constructs like the `for` loop or `while` loop. Instead, repetitive
code execution is achieved by recursive function calls.

A common example of recursion is the function to compute the `n`<sup>th</sup> Fibonacci number:

```lisp
fib:fn (n) if (n <= 1 n
                 fib(n - 1) + fib(n - 2))

fib(10)
; 55
```

Recursions that run deep can cause a stack-overflow error to happen.
This can be prevented by making the recursive call from a <a href="https://en.wikipedia.org/wiki/Tail_call" target="_blank">tail position</a>
using the special `rec` construct.

```lisp
fib2:fn (n a b) if (n = 0 a
                      n = 1 b
		      rec(n - 1, b, a + b))

fib:fn(n) fib2(n 0 1)

fib(25)
; 75025
```

<a name="err"></a>
## Dealing with Errors

Reo runs on top of the Java Virtual Machine and some low-level function calls will raise exceptions.
Sometimes, a function you write also may want to report a critical condition by raising an exception.
This can be achieved by calling the `ex` function, which can take any object as argument.

```lisp
ex("fatal!")
; ERROR: throw+: [:type :reo-ex, :obj "fatal!"]
```

As another example, consider the following function. It will divide `100` by a given number. If this number is
zero, it will raise an exception:

```lisp
f:^if (X1=0 ex("zero!")
         100/X1)

f(5)
; 20

?f(0)
; ERROR: throw+: [:type :reo-ex, :obj "zero!"]
```

The REPL will catch and print the exception. You may also write your own exception handlers using the
function `with_ex`.

Generally, this is how `with_ex` is used:

```lisp
with_ex(handler_fn, do_fn)
```

`Do_fn` is called with no arguments. Normally its value will become the return value of `with_ex`.
If `do_fn` raise an exception, `handler_fn` will be called with the exception object.
`handler_fn` can deal with this object in whatever way is appropriate. The value returned
by `handler_fn` will then become the return value of `with_ex`.

The next function offers a safer version of `f`, utilizing `with_ex`:

```lisp
safe_f:fn(n) with_ex(^{ wrln(str("ERROR: " X1)),
                          inf },
		       ^f(n))

safe_f(5)
; 20

safe_f(0)
; ERROR: zero!
; inf
```

<a name="scripts"></a>
## Scripts & Compilation

Reo source files usually have the extension `.m`.
Source files can be loaded into the interpreter by invoking the `ld` command.
It is customary to call `ld` without the `.m` extension.

For instance, if you have a script called "abc.m", it can be loaded as:

```lisp
ld "abc"
```

`ld` will take care of compiling the script into object code, if the object code
file (with extension `.mo`) is not found.

A script can be explicitly compiled into a `.mo` file by calling the `cf` (compile-file) function:

```lisp
cf("abc")
```

The object code is compiled on-the-fly to JVM byte code.

### Commenting your code

Reo does not have special syntax for commenting programs.
You may include explanatory statements in your code as string literals.
Some examples are given below:

```lisp
"rate of tax, slab 1" r1:0.2
"rate of tax, slab 2" r2:0.5

"calculate tax for a given sales price"
calc:fn(p) if (p > 10000 p * r2 p * r1)
```

## Remote Evaluation

You can start Reo in HTTP server mode and use it to evaluate expressions on a server.

Start the HTTP server, with no REPL:

```shell
$ java -jar target/reo-0.1.0-standalone.jar -s -r false
```

By default, the server listens on port `3030`.

POST an evaluation request:

```shell
$ curl --header "Content-Type: application/json" \\
  --request POST \
  --data '{"expr": "[1 2 3] * 100"}' \
  http://localhost:3030/eval

; {"value":[100,200,300]}
```

Remote evaluation might prove useful while dealing with very large data-sets, or for building data analysis apps.
You should sandbox the evaluator before exposing this feature via a public API.

<a name="jvm"></a>
## Integration with the JVM

Reo runs on top of the Java Virtual Machine and integrates with it seamlessly.
Reo code is compiled to Java bytecode on the fly and can take advantage of JIT compilation.

Functionality offered by the host platform is accessed in two ways,
 - through Clojure function calls
 - by directly importing Java classes and invoking their methods

Here is how you would use functions from a standard Clojure namespace:

```lisp
clj_require(['`clojure.data` 'as 'd])

`d/diff`([1 2 3] [5 9 3 2 3 7])
; [[1 2]
;  [5 9 nul 2 3 7]
;  [nul nul 3]]
```

Classes that are part of the Java `lang` package can be directly used.

A class constructor is invoked by the `ClassName.(args ...)` function call.
A method is accessed by the `.methodName(this ...)` syntax.
Static fields are accessed by the `ClassName/memberName` syntax.

```lisp
f:fn() {`Thread/sleep`(1000) wrln('hi) rec()}

t:`Thread.`(f)

`.start`(t)
; hi
; hi
...
```

Other packages that are part of the Java SDK has to be explicitly [imported](https://clojuredocs.org/clojure.core/import):

```lisp
import('`java.util.HashMap`)

m:`HashMap.`()
`.put`(m 1 "a")
`.put`(m 2 "b")
`.get`(m 1)
; a
```

## Conclusion

That concludes our first detailed peek at Reo. You may quit the REPL by calling the `exit` function:

```lisp
exit()
```

Now you know enough to use Reo to solve serious data processing problems. But there is always something new
to learn! For instance, how to import data from external sources like databases or spreadsheets?
How to use data to build statistical models and visualizations? These questions and more will be answered by the rest of
this [documentation](index.md).