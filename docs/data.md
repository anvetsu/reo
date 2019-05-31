# Munching Data

This document presents some functions and operators that you will find useful
while processing data in Motto. These will be presented in the form of short
code recipes, with short explanations, where that is absolutley required.

## Making Sequences

;; TODO - til, range, repeat etc

## Insert, Append

Insert a new element to the beginning or end of a list using the `concat` (`;`)
operator. (Do not confuse this operator with the `;` prefix of the output):

```lisp
? xs:[10 34 5]
? xs
; [10 34 5]

? 2;xs
; [2 10 34 5]

? xs;2
; [10 34 5 2]

? [1 2 3];xs
; [1 2 3 [10 34 5]]
? xs;[1 2 3]
; [10 34 5 [1 2 3]]
```

## Assoc, Dissoc, Merge

## Lifting and Dropping

## Filtering and Selecting

## Grouping

## Set Operations

## Doing Statistics

## Binary Data
