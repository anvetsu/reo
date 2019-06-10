# Bits & Bytes

#### band(a b)

Perform a logical-and on two bit-vectors. A burrowing operator.

```rust
? band(001110b 010101b)
; 0001b

? band([001110b 111100b] 010101b)
; [0001b 0101b]
```

#### band_not(a b)

Clear all of the bits in `a` for the corresponding bit set in `b`. A burrowing operator.

```rust
? band_not(11111b 010101b)
; 10101b

? band_not(11111b [010101b 11111b])
; [10101b b]
```

#### bits(bs)

Compress the boolean sequence `bs` into a bit-vector.

```rust
? bits([1b 1b 0b 1b])
; 1101b
```

#### bits_size(a)

Return the number of bits of space actually in use by the bit-vector `a`.

```rust
? bits_size(010110b)
; 64
```

#### bools(bs)

Expand a bit-vector to a sequence of booleans.

```rust
? bools(1101b)
; [1b 1b 0b 1b]
```

#### bor(a b)

Return the logical-or of two bit-vectors. A burrowing operator.

? bor(11111b 010101b)
; 111111b

? bor(11111b [010101b 11111b])
; [111111b 11111b]
```

#### bvand(a b & clone)

Return logical-and of `a` and `b`. Update the bit-vector `a` if `clone` is `false`. (Default is `true`).


```rust
? x:11111b

? bvand(x 010101b)
; 0101b

? x
; 11111b

? bvand(x 010101b 0b)
; 0101b

? x
; 0101b
```

#### bvand_not(a b & clone)

Clear all of the bits in `a` for the corresponding bit set in `b`.
Update `a` if `clone` is `false`. (Default is `true`).

#### bvcross(a b)

Return `true` if `a` has any bits set that are also set in `b`.

```rust
? bvcross(010101b 110000b)
; 1b

? bvcross(010101b 100000b)
; 0b
```

#### bvflip(a i)

Set the bit in `a` at index `i` to the complement of its current value.
Updates `a` in-place.

```rust
? x
; 0101b

? bvflip(x 1)
; 0001b

? x
; 0001b
```

#### bvget(a i)

Return the bit of `a` at index `i`.

```rust
?x
; 0001b

? bvget(x 0)
; 0b

? bvget(x 3)
; 1b
```

#### bviter(f a)

Call `f` for each bit in `a`.

```rust
?x
; 0001b

? bviter(wrln x)
; 0b
; 0b
; 0b
; 1b
```

#### bvnot(a)

Flip all bits in `a`, update `a` in-place.

```rust
? x
; 0001b

? bvnot(x)
; 111b

? x
; 111b
```

#### bvor(a b & clone)

Return logical-or of `a` and `b`. Update the bit-vector `a` if `clone` is `false`. (Default is `true`).

#### bvxor(a b & clone)

Return logical-exclusive-or of `a` and `b`. Update the bit-vector `a` if `clone` is `false`. (Default is `true`).

#### bxor(a b)

Perform a logical-exclusive-or on two bit-vectors. A burrowing operator.

#### byte_array(size & init)

Create an array of bytes.

```rust
? byte_array(10)
; [0 0 0 0 0 0 0 0 0 0]

? byte_array(10, [1 2 3])
; [1 2 3 0 0 0 0 0 0 0]
```

#### count_bits(a)

Return the "logical size" of the bit-vector `a`: the index of the highest set bit in the `a` plus one.

```rust
? count_bits(010110b)
; 5
```
