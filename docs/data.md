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

Data encoded in this way is known to be CSV (comma-separated-values) encoded.