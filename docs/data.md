# Data Import and Export

Motto can read and write data encoded in various formats.
It can also fetch data from external sources like databases and HTTP servers.

## Character Delimited Files

A common way to encode textual data is to use comma or some other character to delimit values.

For example, here is a product database using comma as the separator. The first row is the "header",
identifying the names of the columns:

```
ProductId,ProductName,UnitPrice,DateReleased
1,PC-178,34000,2019-02-25
2,XTR-130,23000,2019-03-01
3,TTY-234,45600,2018-12-02
```

Data formatted in this way is known as CSV (comma-separated-values) encoded.

Motto can import CSV encoded files and convert them to tables. For example, if the above data is contained in a file
named "products.csv", we can import it as:

```lisp
? products:csv("products.csv")

? products
; ProductId: [1 2 3]
; ProductName: [PC-178 XTR-130 TTY-234]
; UnitPrice: [34000 23000 45600]
; DateReleased: [2019-02-25 2019-03-01 2018-12-02]
```

One problem is that all values, even those that are supposed to be numbers, is imported as strings.

```lisp
? is_string(products('UnitPrice)(0))
; 1b
```

So we need to do some explicit conversions before we can apply data processing operations on them:

```lisp
? sum(to_int ~ products('UnitPrice))
; 102600
```

This may not be convenient always. So the `csv` function allows you to specify the type for each column and it will
take care of converting values at the time of load itself. The following call to `csv` specifies the types of
the products columns as `string (ProductId)`, `string (ProductName)`, `int (UnitPrice)` and `date of format yyyy-MM-dd (DateReleased)`.

```lisp
? products:csv("products.csv" ['types:['s 's 'i "yyyy-MM-dd"]])

? products
; ProductId: [1 2 3]
; ProductName: [PC-178 XTR-130 TTY-234]
; UnitPrice: [34000 23000 45600]
; DateReleased: [dt("2019-02-25T00:00:00") dt("2019-03-01T00:00:00") dt("2018-12-02T00:00:00")]

? sum(products('UnitPrice))
; 102600

? dtadd(products('DateReleased)(1) 'M 1)
; dt("2019-04-01T00:00:00")
```

Other options that can be passed to `csv` are:

```
'auto_header  - detect header row (default - 1b)
'headers      - custom headers (default - auto-detected)
'charset      - character encoding (default - "UTF-8")
'delim        - the delimiter (default - ,)
'numcols      - the number of columns (default - auto-detected from header row)
```

## JSON

Imagine that the products data is encoded as [JSON](https://json.org/) in the file "products.json":

```js
{"ProductId": "1",
 "ProductName": "PS-178",
 "UnitPrice": 34000,
 "DateReleased": "2019-02-25"}
{"ProductId": "2",
 "ProductName": "XTR-130",
 "UnitPrice": 23000,
 "DateReleased": "2019-03-01"}
{"ProductId": "3",
 "ProductName": "TTY-234",
 "UnitPrice": 45600,
 "DateReleased": "2018-12-02"}
```

This data can be decoded and imported as a columnar table by calling the `json` function:

```lisp
? products:json("products.json")

? products
; ProductId: [1 2 3]
; ProductName: [PS-178 XTR-130 TTY-234]
; UnitPrice: [34000 23000 45600]
; DateReleased: [2019-02-25 2019-03-01 2018-12-02]

? sum(products('UnitPrice))
; 102600

? mx(products('UnitPrice))
; 45600
```

Sequences and dictionaries can be encoded as JSON strings using the `json_enc` function.
JSON encoded string can be decoded back to Motto structures using the `json_dec` function.

```lisp
? json_enc([['a:1 'b:2] ['a:3 'b:4]])
; [{"a":1,"b":2},{"a":3,"b":4}]

? json_dec(json_enc([['a:1 'b:2] ['a:3 'b:4]]))
;[[a:1 b:2] [a:3 b:4]]
```

## Excel Spreadsheets

;; TODO

## Relational Databases

;; TODO

## HTTP Services

;; TODO
