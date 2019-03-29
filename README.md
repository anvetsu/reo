# Motto

A programming environment for creating and analyzing columnar databases.

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
> t
t
> f
f
```

The arithmetic operators obey their generally accepted precedence rules. This can be
overridden by enclosing expressions in parenthesis:

```
> 10 + 4 / 2
12
> (10 + 4) / 2
7
```

If you want to split an expression into multiple lines, terminate each line with a space:

```
> 10 + 4 <space>
- / 2
12
```

The `-` prompt indicates that `motto` is waiting for more input.

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

The arithmetic operators are overloaded to work with lists and numbers:

```
> 2 * til(5)
[0 2 4 6 8]
> til(5) + [10 20 30 40 50]
[10 21 32 43 54]
```

Adding single values to the front and rear of a list:

```
> 1;[2 3 4]
[1 2 3 4]
> [1 2 3];4
[1 2 3 4]
```

Folding and mapping over lists:

```
> (+)@ [1 2 3 4 5]
15
> (fn(x) x*x)~ [1 2 3 4 5]
[1 4 9 16 25]
> (+)@~ [1 2 3 4 5]
[1 3 6 10 15]
```

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
t
> (1 = 1) = t
t
```

Other comparison operators are `<`, `>`, `<=`, `>=` and `<>`.
The comparison operators can also be applied to lists and atomic values, mixed together:

```
> [1 2 3] < [3 4 5]
[t t t]
> 100=[99 100 101]
[f t f]
```

The logical operators are `&` and `|` (or).

#### Functions

The `fn` keyword is used to create function objects.

```
> sq:fn(x) x*x
> sq(10)
100
```

Functions are first-class objects. Here is a function returning a function:

```
> a:fn(x) fn(y) x + y
> b:a(1)
> b(10)
11
```

#### Code blocks

Code blocks are delimited by opening and closing curly braces:

```
> pyth:fn(x y) { a:x*x b:y*y a+b }
> pyth(3 4)
25
```

#### Conditional expressions

TODO

#### Comments

The character `!` starts a comment that will extend to the end of the line.
A comment can be embedded between expressions by enclosing it in two `!`s.

```
> 1+2 !this is a comment! 3+4
7
```

An exclamation mark can be included in a comment by typing it twice:

```
!hi!!
```

### Columnar data tables

TODO

#### The query sub-language

TODO

#### Higher-order operations on columnar data

TODO

### Scripts & Compilation

Motto source files usually have the extension `.m`.
Source files can be loaded into the interpreter by invoking the `ld` command.
It is customary to call `ld` without the `.m` extension.

For instance, if you have a script called "abc.m", it can be loaded as:

```
ld "abc"
```

`ld` will take care of compiling the script into object code, if the object code
file (with extension `.mo`) is not found.

A script can be explicitly compiled into a `.mo` file by calling the `c` (compile) function:

```
c("abc")
```

The object code is compiled on-the-fly to JVM byte code.