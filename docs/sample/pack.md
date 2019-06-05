# Greedy Packing

This program shows a greedy solution to the [Knapsack problem](https://en.wikipedia.org/wiki/Knapsack_problem).

Imagine that we have to pack the following items into a bag that carry a maximum weight of 20 (units).
Each item is encoded as `[item_name value weight]`.

```lisp
items:[['clock 175 10]
       ['painting 90 9]
       ['radio 20 4]
       ['vase 50 2]
       ['book 10 1]
       ['computer 200 20]]
```

We can sort the items by either weight, value or the ration of value to weight.
Then we have to pick the items from the sorted list until the bag is full.
Finally we will choose the best packing by comparing the total value of items packed by all three methods.

Here we define the three predicates required for sorting:

```lisp
by_wt:fn([_ _ w1] [_ _ w2]) w1 < w2
by_val:fn([_ v1 _] [_ v2 _]) v1 > v2
by_rat:fn([_ v1 w1] [_ v2 w2]) v1/w1 > v2/w2
```

Next we define the `pack` function. This will pick each item from a sorted list of items
and put that into a bag. The bag is returned when it is packed to full capacity or when the
items run-out.

```lisp
pack:fn(bag maxwt items) \
       if (seq(items) { i:first(items)
                        w:maxwt-i(2)
			if (is_pos(w) rec(bag;i w rest(items))
			    is_zero(w) bag;i
			    rec(bag maxwt rest(items))) }
           bag)
```

The `pack_bag` function, that we define next, will take a predicate, sort the items by that predicate
and call `pack` with an empty bag, maximum allowed wait and the sorted list of items:

```lisp
pack_bag:fn(cmpr maxwt) pack([] maxwt sort(cmpr, items))
```

Now we have everything required to start packing!

```lisp
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

```lisp
? totval:fn(bag) sum(second ~ bag)

? totval(bag_by_wt)
; 170

? totval(bag_by_val)
; 200

? totval(bag_by_rat)
; 255
```

**Reference** - Chapter 12 of <a href="https://mitpress.mit.edu/books/introduction-computation-and-programming-using-python-second-edition">Introduction to Computation and Programming Using Python</a>.

[Back](../sample.md)