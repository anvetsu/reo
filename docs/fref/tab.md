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

#### group(f init col by)

#### group_count

#### club

#### where

#### !

#### tmap

#### flip

#### save

#### dataset