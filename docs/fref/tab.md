# Tables

#### is_tab(x)

Return `true` if `x` is a columnar table.

```rust
? t:['product_id 'price] $ [['a1 'a2 'a3] [1344 1788.56 1200]]
? t
; product_id: [a1 a2 a3]
; price: [1344 1788.560 1200]

? is_tab(t)
; 1b
```

#### is_rtab(x)

Return `true` if `x` is a relational table.

```rust
? r:flip(t)
? r
; product_id   price
; --------------------------------
;      a1    1344
;      a2 1788.56
;      a3    1200

? is_rtab(r)
; 1b
```

#### $

Create multi-dimensional sequences and columnar tables.

```rust
? [3 4] $ [1 2 3 4 5 6 7 8 9 10]
; [[1 2 3 4]
;  [5 6 7 8]
;  [9 10 1 2]]

? ['a 'b] $ [[1 2 3] [10 20 30]]
; a: [1 2 3]
; b: [10 20 30]
```

#### tab(xs & ys)

Create multi-dimensional sequences and columnar tables.
Convert relational data to columnar format.

```rust
? tab([3 4], [1 2 3 4 5 6 7 8 9 10])
; [[1 2 3 4]
;  [5 6 7 8]
;  [9 10 1 2]]

? tab(['a 'b], [[1 2 3] [10 20 30]])
; a: [1 2 3]
; b: [10 20 30]

? tab([['a:1 'b:10]
       ['a:2 'b:20]
       ['a:3 'b:30]])
; a: [1 2 3]
; b: [10 20 30]
```

#### rtab(cols data)

Create relational tables.

```rust
? rtab(['a 'b] [[1 2 3] [10 20 30]])
;   a  b
; ---------
;   1  2  3
; 10 20 30
```

#### fields(t)

Return the column names from a (columnar or relational) table.

```rust
? fields(t)
; [product_id price]

? fields(r)
; [product_id price]
```

#### data(t)

Return data from a (columnar or relational) table.

```rust
? data(t)
; [[a1 a2 a3]
;  [1344 1788.560 1200]]

? data(r)
; [[a1 1344]
;  [a2 1788.560]
;  [a3 1200]]
```

#### top(n t)

Return the first `n` values from a columnar table.

```rust
? top(2 t)
; product_id: [a1 a2]
; price: [1344 1788.560]
```

#### group(f init col & by)

Summarize a column by another. The function `f` acts as the "accumulator" of the values of `col`,
starting-off with `init`.

```rust
? emp:['name 'dept 'salary] $ [['mat 'sam 'sally 'zen] [2 1 1 3] [1200 1340 1000 1500]]
? emp
; name: [mat sam sally zen]
; dept: [2 1 1 3]
; salary: [1200 1340 1000 1500]

? "total salary in each dept" group(`+` 0 emp('salary) emp('dept))
; [2:1200 1:2340 3:1500]
```

#### group_count(col & by)

Group and count.

```rust
? group_count(emp('dept))
; [2:1 1:2 3:1]
```

#### club(t1 t2)

Merge two tables.

```rust
? dept:['id 'title] $ [[1 2 3] ['accounts 'sales 'hr]]
? dept
; id: [1 2 3]
; title: [accounts sales hr]

? club(emp dept)
; name: [mat sam sally zen]
; dept: [2 1 1 3]
; salary: [1200 1340 1000 1500]
; id: [1 2 3]
; title: [accounts sales hr]
```

#### where(predic t)

Filter rows by predicate.

```rust
? where(fn(row) row('salary) > 1300, emp)
; name: [sam zen]
; dept: [1 3]
; salary: [1340 1500]
```

#### ! (filter)

Filter a sequence or a table.

```rust
? is_odd ! [1 2 3 4 5]
; [1 3 5]

? (fn(row) row('salary) > 1300) ! emp
; name: [sam zen]
; dept: [1 3]
; salary: [1340 1500]
```

#### tmap(f t)

Maps the function `f` over the table `t`. Return a new table of results.

```rust
? tmap(fn(row) {s:row('salary) assoc(row 'salary s + s*0.05)}, emp)
; name: [mat sam sally zen]
; dept: [2 1 1 3]
; salary: [1260.000 1407.000 1050.000 1575.000]
```

#### flip(x)

Convert a columnar table to a relational table and vice versa.