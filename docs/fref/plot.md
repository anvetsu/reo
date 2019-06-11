# Plotting

#### plot(tag & args)

Create a plot. `tag` identifies the type of plot and must be one of,

 - 'scatter
 - 'qq
 - 'box
 - 'xy

For a scatter-plot, `args` should be `(x y & options).

`x` and `y` are sequences of data for plotting.
`options` must be a dictionary with keys:

 - 'title - (default '') main title
 - 'xlabel - (default x expression)
 - 'ylabel - (default 'Frequency')
 - 'legend - (default false) prints legend
 - 'serieslabel - (default x expression)
 - 'groupby - (default nil) -- a vector of values used to group the x and y values into series.
 - 'density (default `0b`) -- chart will represent density instead of frequency.
 - 'nbins (default 10) -- number of bins (i.e. bars)
 - 'gradient (default `0b`) -- use gradient on bars
 - 'visible (default `1b`)


For qq-plot, `args` should be `(xs & options)`.

 - 'data - a dataset
 - 'visible (default `1b`)

```rust
? ys:[[1.4 2.3 3.2 4.5] [10 20 30 40]]
? plot('qq [1 2 3 4 5] ['data:dataset(ys)])
```



#### chart

#### chartadd

#### view

#### chartset

#### logaxis

#### save_view