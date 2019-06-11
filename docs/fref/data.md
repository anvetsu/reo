# Data Export/Import

## JDBC

#### db(& config)

Return a new JDBC connection. If `config` is not specified, return
a connection to the built-in data store.

`Config` must be a map with the following keys:

 - 'jdbc_url - connection URL for the JDBC driver
 - 'driver_class - JDBC driver class name
 - 'user - database username
 - 'password - database password
 - 'min_pool_size - minimum number of connections a pool will maintain at any given time
 - 'max_pool_size - maximum number of connections in a pool
 - 'acquire_increment - number of connections to acquire when a pool is exhausted

Note that to use the built-in data store, there is no need to explicitly create or open a new connection.
The specified `driver_class` must be in the classpath of the JVM process.

#### db_open(db & user password)

Open a database connection and return it

#### db_close(conn)

Close a database connection.

#### db_stmt(conn query_str)

Return a JDBC prepared statement.

#### db_qry(stmt & params)

Execute a query and return result as a columnar table.

#### db_cmd(stmt & params)

Execute a DML command and return number of rows affected.

## CSV

#### csv(file & config)

Load data from a CSV file.

`Config` should contain the following keys:

 - 'auto_header  - detect header row (default - 1b)
 - 'headers      - custom headers (default - auto-detected)
 - 'charset      - character encoding (default - "UTF-8")
 - 'delim        - the delimiter (default - ,)
 - 'numcols      - the number of columns (default - auto-detected from header row)

#### csv_fmt()

Return the default CSV format object (RFC4180)


#### csv_ahdr(fmt)

Set the CSV format object to treat the first row as headers.

#### csv_hdr(fmt headers)

Set the list of headers for the formatter.

#### csv_delim(fmt c)

Set the delimiter character for the formatter.

#### csv_rd(file & fmt config)

Read the CSV file with the given formatter and config.

## HTTP

#### http_get(url & options handler)

Fetch a URL via HTTP GET. Return a future that can be [dereferenced](https://clojuredocs.org/clojure.core/deref).
Providing a `handler` will make a blocking call and pass the result to the handler as a map with keys `[status headers body error]`.

`Options` can be `nul` or a dictionary with the following keys:

 - 'url
 - 'method - one of ['get 'post 'put 'head 'other]
 - 'user_agent
 - 'oauth_token
 - 'headers - a dictionary of string key-values
 - 'query_params - a dictionary of string key-values, "nested" query parameters are also supported
 - 'form_params - just like query-params, except sent in the body
 - 'body
 - 'basic_auth - ["user" "pass"]
 - 'keepalive - keep the TCP connection for `n` milliseconds
 - 'timeout - connection timeout and reading timeout in milliseconds
 - 'filter - reject if body is more than `n` k
 - '`insecure?` - boolean, use with untrusted SSL cert
 - `max_redirects` - max redirects to follow
 - 'follow_redirects - boolean, default false (`0b`)
	       
#### http_res(f & timeout_ms timeout_val)

Dereference the return value of `http_get` or `http_req`.
Will return `timeout_val` on timeout.

#### http_req(options)

Make a generic HTTP request.

## JSON

#### json_enc(x)

Encode `x` as a JSON string.

#### json_dec(s)

Decode a JSON encoded string to a sequence or dictionary.

#### json(filename)

Load data from a JSON encoded file. Return a columnar table.

## Excel

#### xls(filename & options)

Load data from an Excel file. Return a columnar table.

`Options` can be `nul` or a dictionary with the following keys:

 - 'sheet  - the index of the sheet to read (default - 0)
 - 'all_sheets - read all sheets? (default - 0b)

## Stata

#### dct(dict_path data_path)

Load a Stata dataset file and return it as a columnar table.