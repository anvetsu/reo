# Date & Time

#### dt(s & fmt)

Parse a string to a date-time object.
The encoding is specified by `fmt` which defaults to `yyyy-MM-dd'T'HH:mm:ss`.

#### sdt(dt & fmt)

Converts a date-time to a string in the specified format.
The format `fmt` defaults to `yyyy-MM-dd'T'HH:mm:ss`.

#### now()

Return the current date-time.

#### dtadd(dt field amount)

Add `amount` to the date-time field. Return the new date-time.

`Field` should be one of:

- 'y - YEAR
- 'M - MONTH
- 'd - DAY_OF_MONTH
- 'D - DAY_OF_YEAR
- 'dW - DAY_OF_WEEK
- 'dM - DAY_OF_WEEK_IN_MONTH
- 'H - HOUR_OF_DAY
- 'm - MINUTE
- 's - SECOND

#### dtget(dt field)

Return the value of the specified date-time field.