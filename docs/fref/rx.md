# Regular Expressions

#### rx(s)

Return a regular expression by compiling the string pattern `s`.

#### rx_seq(r s)

Return a lazy sequence of successive matches of pattern in string.

```rust
rx_seq(rx("[0-9]+") "abs123def345ghi567")
; [123 345 567]
```

#### rx_find(r s)

Return the next regex match, if any, of string to pattern.

```rust
rx_find (rx("\d+") "abc12345def")
; 12345
```

#### rx_matches(r s)

Return the match, if any, of string to pattern.

#### rx_matcher(r s)

Return an instance of `java.util.regex.Matcher`, for use, e.g. in
`rx_find`.

```rust
m:rx_matcher(rx("\d+") "abc12345def")

rx_find(m)
; 12345
```