# Motto - An Extended Tutorial

Computers consume and create all sorts of data - numbers, texts, lists and tables.
As the encoding of data becomes more complex, people tend to use specialized tools.
As an example, spreadsheet programs are used for visualizing and manipulating tabular data.

In contrast, Motto strives to be a powerful tool that can deal with data in many forms
and shapes. The user interface to Motto is an elegant programming language that you can pick up
in a weekend. This makes Motto a more expressive and scalable tool than a point-and-click GUI program.

It is hoped that this tutorial will convince you of the veracity of that last sentence.

1. [Simple Calculations](#simp)
2. [Variables](#vars)
3. [Lists](#lists)
4. [Multiple Dimensions](#dims)
5. [Reductions & Deductions](#reds)
6. [Indexing & Mapping](#idx)
7. [Dictionaries & Tables](#dicts)
8. [Functions](#funs)
9. [Conditions & Control Flow](#conds)
10. [Infinit Data Streams](#inf)
11. [Dealing with Errors](#err)

<a name="simp"></a>
## Simple Calculations

For simple data objects, like numbers and booleans, Motto behaves like any traditional programming
language:

```scheme
? 157.89 + 34.19
; 192.07999999999998

? 10 - 2 * 5
; 0
```

Large numbers may be written with an underscore for ease of reading:

```scheme
? 1_50_000 * 45_000
; 6750000000
```

Division of integers will result in an exact real value.

```scheme
? 56 / 2
; 28

? 56 / 3
; 56/3
```

Divisions that involve an inexact floating-point number will produce an inexact result.

```scheme
? 56 / 3.0
;18.666666666666668
```

Division by zero will return a constant that represents infinity, which can be used in other
arithmetic operations. The only catch is that any operation that involve infinity will return infinity.

```scheme
? 56/0
; inf

? 10 + 56 / 0
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

```scheme
? 1 < 1+1
; 1b

? 1 < 1+1 & 3 >= 4
; 0b

? 1 < 2 | 3 >= 4
; 1b
```

Strings are enclosed in double-quotes. Character literals are prefixed by a `\`.

```scheme
? "hello world"
; hello world

? \a
; a
```

Non-visual characters may be written with more descriptively as `\space` and `\tab`.

String constants may also be represented as *symbols*. A symbol is an identifier prefixed by a single-quote (').
Symbols that contain white-spaces or other special characters must be enclosed in tick-quotes (`).

```scheme
? 'red
; red

? '`hello world`
; hello world

? '`+++---`
; +++---
```

Symbols are more efficient than strings because two symbols made of the same characters share the same space in memory.

<a name="vars"></a>
## Variables

Variables are created using the `:` (declare) operator. This operator will return the symbolic name of the
new variable. (In the sample code we have left out these return values).

```scheme
? price:59.75
? qty:3
? tot:price * qty
? tot
; 179.25
```

<a name="lists"></a>
## Lists

Motto can also operate on lists of values. Lists are delimited by opening and closing square brackets.

Assume that we have to find the total price of not one by five products. The prices and corresponding
quantities can be represented as two lists:


```scheme
? price:[59.75 34.0 55.25 60.5 23.5]
? qty:[3 5 3 2 4]
```

**Note** You may separate each element in a list by commas but this is optional most of the time.
The comma separator is required while mixing positive and negative numbers. E.g: `[2, -1 3]`. If the
comma separator is left out, the list returned will be `[1 3]` instead of the expected `[2 -1 3]`.

The total costs can be found by simply multiplying the two lists together:

```scheme
? cost: price * qty
? cost
; [179.25 170.0 165.75 121.0 94.0]
```

As we can see here, the arithmetic operators can operate on lists as well as they work with individual numbers.
This is true for comparison operators and many other built-in functions. This "burrowing" extends to lists
of more complex shapes and structures. This advanced list processing capability eliminates the need for imperative,
"loopy" code common in many languages.

You may freely mix numbers and lists in an arithmetic expression. For example, you can add a `5%` tax to the prices as,

```scheme
? tax:0.05
? price + tax * price
; [62.7375 35.7 58.0125 63.525 24.675]
```

<a name="dims"></a>
## Multiple Dimensions

Lists can be used to model data with multiple dimensions. An as example, consider the temperature forecasts (in Celsius) for two cities
for the next 7 days:

```scheme
? forecast:[[37 36 37.5 37 35.4 33 35]
            [38 38.3 37 35 37 36.5 37]]
```

The `dim` function can be used to find out the dimension of complex lists:

```scheme
? dim(forecast)
; [2 7]
```

The output means `forecast` is a list with `2` rows and `7` columns.

After 7 days we receive the actual temperature readings as a single list with 14 entries.
The first 7 entries pertain to the first city and the next 7 entries are for the second city.

```scheme
? sensor_data:[36 35 37 36 35 34 35 36 37 38 37 37 36 35]
? assert(count(sensor_data) = 14)
```

Before we can use this for comparison with the forecast, we need to mold this sequence into the
appropriate shape or dimension. We can use the `tab` (tabulate) function for this.
The `tab` function takes two arguments - the dimension and the sequence of data that needs to tabulated.

```scheme
? actual:tab([2 7] sensor_data)
? actual

; [[36 35 37 36 35 34 35]
;  [36 37 38 37 37 36 35]]
```

**Note** Just as for lists, the comma separator is optional for function arguments.

If you find yourself tabulating too much, you may save a few keystrokes by using the `tab` operator (`$`):

```scheme
? [2 7] $ sensor_data

; [[36 35 37 36 35 34 35]
;  [36 37 38 37 37 36 35]]
```

Now that we have the forecast data and actual data, one basic question we would like to answer is
how much the actual temperature readings varies from the forecast? As the arithmetic operators can burrow into
lists of any dimension, the solution is the following simple program:

```scheme
? variance:actual - forecast
? variance

; [[-1 -1 -0.5 -1 -0.3999999999999986 1 0]
;  [-2 -1.2999999999999972 1 2 0 -0.5 -2]]
```

<a name="reds"></a>
## Reductions & Deductions

Earlier in this tutorial, we calculated the cost of purchase of a list of products:

```scheme
? cost
; [179.25 170.0 165.75 121.0 94.0]
```

We find the total cost by adding together all elements in the list.
This can be achieved with the help of the `fold` operator denoted by the `@` symbol:

```scheme
? `+` @ cost
; 730.0
```

As `+` is an operator the function attached to it is extracted by enclosing it in ticks (`).
Now what does the fold (`@`) operator do? It basically inserts the `+` function between all the elements of
the `cost` list and then evaluate the resulting expression:

```scheme
`+` @ cost => 179.25 + 170.0 + 165.75 + 121.0 + 94.0 => 730.0
```

The fold operator is defined in terms of a lower level function called `reduce`. This function takes three
arguments: a function that is inserted between the elements, an initial value to start the folding and the list to be
folded.

```scheme
? reduce(`+` 0 cost)
; 730.0
```

Convenience functions for folding a list using the arithmetic operators are built-into Motto - `sum` for `+`, `dif` (difference) for `-`,
`prd` (product) for `*` and `qt` (quotient) for `/`:

```scheme
? sum(cost)
; 730.0

? dif([4 3 2])
; -1

? prd([5 5 10])
; 250

? qt([100 5 2])
; 10
```

Let's use fold for finding the average cost:

```scheme
? sum(cost) / count(cost)
; 146.0
```

That was just an exercise! Normally, you should use the built-in `mean` function for this:

```scheme
? mean(cost)
; 146.0
```

<a name="idx"></a>
## Indexing & Mapping

A list can be called like a function, with an index as argument. The value at that index will be returned.
List indices starts at `0`.

```scheme
? xs:[10 78 34 90]
? xs(0)
; 10

? sqrt(xs(2))
; 5.830951894845301
```

What if want to apply a function to each element in a list and construct a list of the results?
We can do that with the `map` function:

```scheme
? map(sqrt xs)
; [3.1622776601683795 8.831760866327848 5.830951894845301 9.486832980505138]
```

Mapping a function over a list a common operation, so we have an operator for that represented by the `~` (tilde) symbol.

```scheme
? sqrt ~ xs
; [3.1622776601683795 8.831760866327848 5.830951894845301 9.486832980505138]
```

As a list itself is treated as a function, we can map it over a list of indices to extract a subset:

```scheme
? xs ~ [0 2 3]
; [10 34 90]
```

We can combine the fold and map operators to return the accumulated results
at each stage of the fold:

```scheme
? `+` @~ [1 2 3 4 5]
; [1 3 6 10 15]
```

Here is a more practical example of `map`. We are given the following information about 5 employees in a company:

```scheme
? salary:[1500 2300 1200 3000 1250]
? category:[0 2 1 0 1]
```

The management has decided to give a salary increment to employees in each category based on these fixed rates:

 - Category 0: 500
 - Category 1: 200
 - Category 2: 350

These rates can be represented as the list:

```scheme
? rate:[500 200 350]
```

The increment applicable to each employee can be found out by mapping the `rate` list over the `category` list:

```scheme
? rate ~ category
; [500 350 200 500 200]
```

The actual increment can be computed by adding these rates to the `salary` list:

```scheme
? (rate ~ category) + salary
; [2000 2650 1400 3500 1450]
```

Those are the new salaries!

<a name="dicts"></a>
## Dictionaries & Tables

A dictionary is a data structure that associates names (keys) to values.
Here is an example of using a dictionary to represent an employee record:

```scheme
emp:['name: "J Kale" 'dept: 101 'salary: 1500]
```

A dictionary is indexed by keys:

```scheme
? emp('name)
; J Kale

? emp('salary)
; 1500
```

If a non-existing key is looked-up, a `nil` value is returned, which has no printable representation.
The function `is_nul` can be used to check if a value is `nil` or not.

```scheme
? is_nul(emp('age))
; 1b
```

The `get` function also may be used to lookup keys. This function can accept an optional argument
that will be returned instead of `nil` for missing keys.

```scheme
? is_nul(get(emp 'age))
; 1b

? get(emp 'age 45)
; 45
```

Lists of dictionaries form tables. Here is a database of employee records:

```scheme
? db:[['name:"J Kale"  'dept:101 'salary:1500]
      ['name:"M Sally" 'dept:100 'salary:2000]
      ['name:"K Joe"   'dept:101 'salary:1400]]
```

How will you find the total salary? With the help of `map` and `sum`! First let's extract the salaries:

```scheme
? sals:'salaries ~ db
? sals
; [1500 2000 1400]
```

A symbol can also behave like a function. When applied to a dictionary, the symbol will extract the associated value.

```scheme
? 'salary(emp)
; 1500
```

To extract all salaries in the `db` list, we just map the symbol `'salary` over that list. That's how we obtained the
value for the list `sals`.

Call `sum` on this list, and we have the total salary!

```scheme
? sum(sals)
; 4900
```

### Columnar tables

If we perform a lot of aggregations like this, it will be more efficient to store tables in a different format, as shown below:

```scheme
? db:['a: [1 2 3 4 5]
      'b: [10 20 30 40 50]]
```

Now aggregations can be computed without an extra call to `map`.

```scheme
? sum(db('a))
; 15
```

The `tab` function can be used to easily create such "columnar" tables. Let's re-define the employee table
in this format:

```scheme
? db:['name 'dept 'salary] $ [["J Kale" "M Sally" "K Joe"]
                              [101 100 101]
			      [1500 2000 1400]]

? db
; name: [J Kale M Sally K Joe]
; dept: [101 100 101]
; salary: [1500 2000 1400]
```

Computing the total salary is now more straightforward:

```scheme
? sum(db('salary))
; 4900
```

Column names and data may be queried separately from the table:

```scheme
? fields(db)
; [name dept salary]

? data(db)
; [[J Kale M Sally K Joe]
;  [101 100 101]
;  [1500 2000 1400]]
```

If required, a columnar table could be "flipped" into a record-based store:

```scheme
? rec_db:flip(db)
? rec_db
;    name    dept  salary
; ---------------------------------------
;   J Kale     101    1500
;  M Sally     100    2000
;    K Joe     101    1400

? fields(rec_db)
; [name dept salary]

? data(rec_db)(0)
;[J Kale 101 1500]
```

And yes, a record-based table can be flipped back into a columnar store!

```scheme
? flip(rec_db)
; name: [J Kale M Sally K Joe]
; dept: [101 100 101]
; salary: [1500 2000 1400]
```

We will learn more about tables in the tutorial on [data analysis](data.md).

<a name="funs"></a>
### Functions

So far we have used many functions that come built-in with Motto - `map`, `sum`, `reduce` and so on.
We have seen that the operators like `+`, `*` and `=` themselves are functions.

In this section we will see how we can define our own functions. A function is created by the `fn` keyword, followed
by the function parameterd (enclosed in brackets) and the function body.

Here is how we will define a function that doubles its argument:

```scheme
? dbl:fn(a) a + a
```

Functions are also data, so we can bind a function to a variable. In the above example, we have
bound the doubling function to the name `dbl`.

We can apply the `dbl` function to a single argument. It's double will be returned. As the function internally
uses the burrowing `+` operator, `dbl` can seemlessly work with lists as well as single numbers.

```scheme
? dbl(10)
; 20

? dbl([1 2 3])
; [2 4 6]

? dbl([[2 3] 4 5 [[[6 7]]]])
; [[4 6] 8 10 [[[12 14]]]]
```

As another example, let's define a function that increments an amount by a percentage rate:

```scheme
? incr:fn(amount percent) amount + amount * (percent/100.0)
```

This is how we would call this two-argument function:

```scheme
? incr(1000 100)
; 2000.0
? incr(1000 50)
; 1500.0
? incr([1000 2000 3000] [10 5 50])
; [1100.0 2100.0 4500.0]
```

### Optional & Keyword arguments

;; TODO

### Function literals

;; TODO

<a name="conds"></a>
### Conditions & Control flow

;; TODO

<a name="inf"></a>
### Infinite Data Streams

;; TODO

<a name="err"></a>
### Dealing with Errors

;; TODO
