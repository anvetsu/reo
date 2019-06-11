# File IO

#### byte_write(os b)

Write a byte to the output-stream.
Return `true` on success, `false` on failure.

#### bytes_flush(os)

Flush the output-stream.
Return `true` on success, `false` on failure.

#### bytes_in(x)

Open and return an input-stream.

If the argument is a string, it tries to resolve it first as a URI, then
as a local file name.  URIs with a 'file' protocol are converted to
local file names.

#### bytes_out(x)

Open and return an output-stream.

If the argument is a string, it tries to resolve it first as a URI, then
as a local file name.  URIs with a 'file' protocol are converted to
local file names.

#### bytes_write(os bytes)

Write the byte array to the output-stream.
On success, return the number of bytes return, otherwise return `false`.

#### close(s)

Close the input/output stream. Return `true` on success.

#### file_copy(src dest & options)

Copy the source file to destination.

Option keys: `['bufsize 'encoding]`.

Return `true` on success, `false` on failure.

#### file_delete(f)

Delete the file.

#### ireset(is)

Reset the input stream to position `0`.
Return `true` on success.

#### iskip(is n)

Skip `n` bytes in the input-stream.
Return the number of bytes skipped, on error return `false`.

#### newln()

Print a newline to the standard-output.

#### rdln()

Read an expression from the standard-input.

#### rdmln()

Read a multi-line expression from the standard-input.

#### setprec(n)

Sets the precision of double output.

#### str_flush(sw)

Flush the string writer.

#### str_in(x)

Open and return a string reader.

If the argument is a string, it tries to resolve it first as a URI, then
as a local file name.  URIs with a 'file' protocol are converted to
local file names.

#### str_in_seq(sr)

Return the lines of text from string reader as a lazy sequence of strings.

#### str_out(x)

Open and return a string writer.

If the argument is a string, it tries to resolve it first as a URI, then
as a local file name.  URIs with a 'file' protocol are converted to
local file names.

#### str_write(sw s)

Write the string to the string-writer.
On success return the number of characters writer, on error return `false`.

#### with_open(f stream)

Call the function `f` with the open stream.
Close the stream before returning.

#### wr(x)

Write the expression to the standard-output.

#### wrln(x)

Write the expression followed by a newline to the standard-output.