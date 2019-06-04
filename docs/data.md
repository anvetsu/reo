# Data Import and Export

Motto can read and write data encoded in various formats.
It can also fetch data from external sources like databases and HTTP servers.

## Delimited Files

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
; [[a:1 b:2] [a:3 b:4]]
```

The result of the above `json_dec` can be converted to columnar format by calling tab:

```lisp
? tab([['a:1 'b:2] ['a:3 'b:4]])
; a: [1 3]
; b: [2 4]
```

## Excel Spreadsheets

Excel files can be imported using the `xls` function.

```lisp
? xls("products.xls")
```

It can take an optional dictionary argument with the following keys:

```
sheet  - the index of the sheet to read (default - 0)
all_sheets - read all sheets? (default - 0b)
```

## Relational Databases

Motto can read and write relational databases. It comes with an embedded relational database
that can be used as local storage.

The main functions for interacting with the database is `cmd` and `qry`. `Cmd` is used for data-definition and
manipulation and `qry` is used for loading data into in-memory columnar tables.

```lisp
? cmd("create table products (id varchar(20) primary key, name varchar(50), unit_price int, date_released date)")
; 0
? cmd("insert into products values('1', 'PS-178', 34000, '2019-02-25')")
; 1
? cmd("insert into products values('2', 'XTR-130', 23000, '2019-03-01')")
; 1
? cmd("insert into products values('3', 'TTY-234', 45600, '2018-12-02')")
; 1
? products:qry("select * from products")
? products
; id: [1 2 3]
; name: [PS-178 XTR-130 TTY-234]
; unit_price: [34000 23000 45600]
; date_released: [#inst "2019-02-24T18:30:00.000-00:00" #inst "2019-02-28T18:30:00.000-00:00" #inst "2018-12-01T18:30:00.000-00:00"]
```

The values of `date_released` are returned as objects of the Java `Date` class. This needs to be converted to Motto's date-time type
before they can be easily processed.

```lisp
? products:assoc(products, 'date_released, dt ~ products('date_released))

? products
; id: [1 2 3]
; name: [PS-178 XTR-130 TTY-234]
; unit_price: [34000 23000 45600]
; date_released: [dt("2019-02-25T00:00:00") dt("2019-03-01T00:00:00") dt("2018-12-02T00:00:00")]
```

As Motto internally uses [JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity),
it can be easily extended to talk to any database with a JDBC driver.

## HTTP Services

The easiest way to fetch data over HTTP is via the `http_get` function. This function completes asynchronously and returns an
`future` object that can be queried with the `http_res` (http-result) function.

```lisp
? f:http_get("https://some-server/data.json")
? result:http_res(f)
```

`Http_res` will block until the result is filled with response from the HTTP call.
The returned value will be a dictionary with the following main keys - `headers`, `status` and `body`.

`Http_res` may also be called with an optional timeout value in milliseconds and a value to return on timeout.
The following program will return a special `timeout` status if the HTTP request does not complete within a second:

```lisp
? result:http_res(f 1000 ['status:'timeout])
```

There is a more generic function for making HTTP requests. This is called `http_req`. For example,
this function can be used as follows for POSTing JSON data to a HTTP server:

```lisp
? f:http_req(['url:"https://some-server/login" 'method:'post
              'headers:["Content-Type": "application/json"]
	      'body:json_enc(["user": "abc" "token": "xyz111"])])
? result:http_res(f)
```

[Back](index.md)