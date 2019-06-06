```rust
toss:fn() is_pos(roll(2))
flip:fn(n) float(count(is_true ! lift(n listf(toss))) / n)

flip_sim:fn(ntrials nflips) mean(lift(ntrials listf(^flip(nflips))))
```