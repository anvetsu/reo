# Motto

A language and environment for analyzing columnar databases.

Data from RDBMSs, CSVs, REST APIs and other sources can be imported and analyzed using a simple functional language.

The environment exposes a command-line REPL as well as an HTTP interface.
This makes it scale from simple desktop data analytics to serving as the compute infrastructure for
distributed analytics dashboards and tools.

`Motto` is in the early stages of development. Make sure you have JDK 10+ and Leiningen (https://leiningen.org/) installed.
Execute `lein run` from this folder to start the REPL.

### The Language

The `Motto` data-analysis language has a simple and compact syntax, making it ideal for using from dashboards
designed for both web and mobile platforms.

#### Datatypes

Numbers, strings, booleans:

```
motto v0.0.1
> 12.7 - 5 * 10
-37.3
> "hello world"
hello world
> true
true
> false
false
```

The arithmetic operators obey their generally accepted precedence rules. This can be
overridden by enclosing expressions in parenthesis:

```
> 10 + 4 / 2
12
> (10 + 4) / 2
7
```
List is the fundamental compound data structure.
They are the building blocks for the more complex columnar tables.

Unlike other dynamic languages, `Motto` optimizes for the storage of homogeneous lists.

List literals are enclosed in `[]`. List elements are separated by spaces.

```
> [1 2 3]
[1 2 3]
> ["$" 10+20]
[$ 30]
```

The most fundamental list making function is `til`, which takes a non-negative integer `n` and returns
the first `n` integers starting at `0` (`n` itself is not included in the result).

```
> til(5)
[0 1 2 3 4]
```

The arithmetic operators are overloaded to work with lists:

```
> 2 * til(5)
[0 2 4 6 8]
> til(5) + [10 20 30 40 50]
[10 21 32 43 54]
```

TODO: other list operations - fold, filter etc

#### Variables

Variables are declared with the `:` operator.

```
> a:10
10
> a + 2
12
```

Variable declaration is an expression:

```
> b:4+a:100
104
> a
100
> b
104
```

Note that variable binding has lower precedence than arithmetic operators.
To bind `b` to `4`, you will have to do:

```
(b:4)+a:100
```

#### Comparison

The `=` operator compares two values for equality:

```
> a = 100
true
> (1 = 1) = true
true
```

TODO: other comparison and logical operators

#### Functions

TODO

#### Conditional expressions

TODO

#### Comments

TODO

### Columnar data tables

TODO

#### The query sub-language

TODO

#### Higher-order operations on columnar data

TODO