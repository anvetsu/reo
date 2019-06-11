# Sets

#### is_set(x)

Return `true` if `x` is a set, otherwise return `false`.

#### is_subset(x y)

Return `true` is `x` is a subset of `y`.

```rust
? is_subset(set([3 2]) set([1 2 3 4 5]))
; 1b
```

#### is_superset(x y)

Return `true` is `x` is a super-set of `y`.

```rust
? is_superset(set([1 2 3 4 5]) set([3 2]))
; 1b
```

#### set(xs)

Return the sequence `xs` as a set.

```rust
? set([1 2 1 3 2 4])
; [1 4 3 2]
```

#### setd(x y & xs)

Return the difference of the sets.

```rust
? x:set([1 2 3 4 5])
? y:set([3 2])

? setd(x y)
; [1 4 5]
```

#### seti(x y & xs)

Return the intersection of the sets.

```rust
? seti(x y)
; [3 2]
```

#### setj(x y & on)

Return the natural join of two relational sets.

```rust
? emp:set([['name:'sam 'emp_dept:1 'salary:1500]
           ['name:'mat 'emp_dept:2 'salary:2000]
	   ['name:'san 'emp_dept:1 'salary:1300]])

? dept:set([['dept:1 'title:'accounts]
            ['dept:2 'title:'sales]])

? setj(emp dept)
; [[dept:1 title:accounts name:sam emp_dept:1 salary:1500]
;  [dept:2 title:sales name:sam emp_dept:1 salary:1500]
;  [dept:1 title:accounts name:mat emp_dept:2 salary:2000]
;  [dept:1 title:accounts name:san emp_dept:1 salary:1300]
;  [dept:2 title:sales name:mat emp_dept:2 salary:2000]
;  [dept:2 title:sales name:san emp_dept:1 salary:1300]]
```

If `on` is specified, it must be a mapping of keys on which to perform the joins.

```rust
? setj(emp dept ['emp_dept:'dept])
; [[dept:1 title:accounts name:sam emp_dept:1 salary:1500]
;  [dept:1 title:accounts name:san emp_dept:1 salary:1300]
;  [dept:2 title:sales name:mat emp_dept:2 salary:2000]]
```

#### setp(x ks)

Remove unwanted keys from a relational set.

```rust
? setp(emp ['name 'salary])
; [[name:sam salary:1500]
;  [name:san salary:1300]
;  [name:mat salary:2000]]
``` 

#### setsel(predic x)

Filter the set based on the predicate.


```rust
? x
; [1 4 3 2 5]

? setsel(is_odd x)
; [1 3 5]
```

#### setu(x y & xs)

Return the union of the sets.

```rust
? setu(set([1 2 3]) set([3 4 5]) set([5 5 5]))
; [1 4 3 2 5]
```