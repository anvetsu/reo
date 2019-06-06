# Greedy Packing

This program shows a greedy solution to the [0/1 Knapsack problem](https://en.wikipedia.org/wiki/Knapsack_problem).

Imagine that we have to pack the following items into a bag that carry a maximum weight of 20u (units - kg or pound).
Each item is encoded as `[item_name value weight]`.

```rust
items:[['clock 175 10]
       ['painting 90 9]
       ['radio 20 4]
       ['vase 50 2]
       ['book 10 1]
       ['computer 200 20]]
```

It will be useful to define accessor functions for an item's fields:

```rust
iname:first
ivalue:second
iweight:fn(item) nth(item 2)
```

We can sort the items by either weight, value or the ratio of value to weight.
Then we have to pick the items from the sorted list until the bag is full.
Finally we will choose the best packing by comparing the total value of items packed by all three methods.

Here we define the three predicates required for sorting:

```rust
by_wt:fn([_ _ w1] [_ _ w2]) w1 < w2
by_val:fn([_ v1 _] [_ v2 _]) v1 > v2
by_rat:fn([_ v1 w1] [_ v2 w2]) v1/w1 > v2/w2
```

Next we define the `pack` function. This will pick each item from a sorted list of items
and put that into a bag. The bag is returned when it is packed to full capacity or when the
items run-out.

```rust
pack:fn(bag maxwt items) \
       if (items {
            i:first(items)
            w:maxwt-iweight(i)
            if (is_pos(w) rec(bag;i w next(items))
                is_zero(w) bag;i
                rec(bag maxwt next(items)))
	   }
           bag)
```

The `pack_bag` function, that we define next, will take a predicate, sort the items by that predicate
and call `pack` with an empty bag, maximum allowed weight and the sorted list of items:

```rust
pack_bag:fn(cmpr maxwt) pack([] maxwt sort(cmpr, items))
```

Now we have everything required to start packing!

```rust
? bag_by_wt:pack_bag(by_wt 20)
? bag_by_val:pack_bag(by_val 20)
? bag_by_rat:pack_bag(by_rat 20)

? bag_by_wt
; [[book 10 1]
; [vase 50 2]
; [radio 20 4]
; [painting 90 9]]

? bag_by_val
; [[computer 200 20]]

? bag_by_rat
; [[vase 50 2]
;  [clock 175 10]
;  [book 10 1]
;  [radio 20 4]]
```

It's just a matter of folding these bags by value to figure out the best method:

```rust
? totval:fn(bag) sum(ivalue ~ bag)

? w:totval(bag_by_wt)
? v:totval(bag_by_val)
? r:totval(bag_by_rat)

? [w v r]
; [170 200 255]
```

We may use fold again to automate the task of finding the best method:


```rust
? best_of_2:fn([n1 b1] [n2 b2]) if (b1 > b2 [n1 b1] [n2 b2])
? best_of_2 @ [['by_weight w] ['by_val v] ['by_ratio r]]
; [by_ratio 255]
```

### An optimal solution

The result obtained from `best_of_2` is approximate and not the best.

The best solution can be found by the following algorithm:

1. Compute all subsets of items.
2. Filter out all combinations whose weight exceeds the maximum weight allowed.
3. From the remaining subsets, find the one with the maximum value.

All possible subsets of a set can be obtained by calling the built-in `subsets` function.

We also need a function to find the total weight of a combination:

```rust
towt:fn(bag) sum(iweight ~ bag)
```

The `best_fits` function defined below will filter out all overweight combinations
from all possible subsets (i.e the [power set](https://en.wikipedia.org/wiki/Power_set) of `items`).
Note that the first subset, which is empty, is ignored by the `filter` operation.

```rust
best_fits:fn(maxwt) (fn(bag) num_lteq(towt(bag), maxwt)) ! rest(subsets(items))
```

The next function picks the best from the bags returned by `best_fits`.
This is calculated by folding the results by a `maximum-by-value` function.

```rust
best_fit:fn(maxwt) (fn(a b) if (totval(a) > totval(b) a b)) @ best_fits(maxwt)
```

So what's the optimal combination that gives the maximum value for a bag that can carry 20u?

```rest
? bag:best_fit(20)
? bag
; [[clock 175 10]
;  [painting 90 9]
;  [book 10 1]]

? totval(bag)
; 275
```

Keep in mind that the optimal algorithm has a time complexity of `O(n*2<sup>n</sup>)`, where `n` is the number of items.
This makes it practical only for very small data-sets.

**Reference** - <a href="https://mitpress.mit.edu/books/introduction-computation-and-programming-using-python-second-edition">Introduction to Computation and Programming Using Python</a>.

[Back](../sample.md)