# Plotting

#### plot(tag & args)

Create a plot. Return the new plot object.

`Tag` identifies the type of plot and must be one of,

 - 'scatter
 - 'qq
 - 'box
 - 'xy

For a scatter-plot, `args` should be `(x y & options).

`x` and `y` are sequences of data for plotting.
`options` must be a dictionary with keys:

 - 'title - (default '') main title
 - 'xlabel - (default `x` expression)
 - 'ylabel - (default 'Frequency')
 - 'legend - (default `0b`) prints legend
 - 'serieslabel - (default `x` expression)
 - 'groupby - (default `nul`) a vector of values used to group the x and y values into series.
 - 'density - (default `0b`) chart will represent density instead of frequency.
 - 'nbins - (default 10) number of bins (i.e. bars)
 - 'gradient - (default `0b`) use gradient on bars
 - 'visible - (default `1b`)


For qq-plot, `args` should be `(xs & options)`.

 - 'data - a dataset
 - 'visible - (default `1b`)

```lisp
ys:[[1.4 2.3 3.2 4.5] [10 20 30 40]]
plot('qq [1 2 3 4 5] ['data:dataset(ys)])
```

**Note** Dataset is a table format used by plotting and statistics functions.
Any sequence, columnar/relational table can be converted to a dataset.

For box-plot, `args` should be `(x & options)`.
`x` is the sequence of data to plot.

Options:

 - 'title - (default '') main title
 - 'xlabel - (default x expression)
 - 'ylabel - (default 'Frequency')
 - 'legend - (default `0b`) prints legend
 - 'serieslabel - (default `x` expression)
 - 'groupby - (default `nul`)
 - 'visible - (default `1b`)

For xy-plot, `args` should be `(x y & options)`.

Options:

 - 'data - (default nil) If the `'data` option is provided a dataset,
                      column names can be used instead of sequences
                      of data as arguments to xy-plot.
 - 'title - (default 'XY Plot') main title
 - 'xlabel - (default `x` expression)
 - 'ylabel - (default 'Frequency')
 - 'legend - (default `0b`) prints legend
 - 'serieslabel - (default `x` expression)
 - 'groupby - (default `nul`) a vector of values used to group the `x` and `y` values into series.
 - 'points - (default `0b`) includes point-markers
 - 'autosort - (default `1b`) sort data by `x`
 - 'visible - (default `1b`)

#### chart(tag & args)

Create a chart object and return it.

`tag` must be one of,

 - 'histogram
 - 'area
 - 'bar
 - 'line
 - 'pie

For histogram, `args` must be `(xs & options)`.

Options:

 - 'nbins - (default 10) number of bins
 - 'density - (default `0b`) if false, plots frequency, otherwise density
 - 'title - (default 'Histogram') main title
 - 'xlabel - (default `x` expression)
 - 'ylabel - (default 'Frequency')
 - 'legend - (default `0b`) prints legend
 - 'serieslabel (default `x` expression)
 - 'visible - (default `1b`)

For area-chart, bar-chart, line-chart and pie-chart - `args` must be `(categories values & options)`

`categories` - a sequence of categories
`values` - a sequence of numeric values

Options:

 - 'title - (default '') main title
 - 'xlabel - (default 'Categories')
 - 'ylabel - (default 'Value')
 - 'serieslabel
 - 'legend - (default `0b`) prints legend
 - 'vertical - (default `1b`) the orientation of the plot
 - 'groupby (default `nul`) a vector of values used to group the values into series within each category.
 - 'visible (default `1b`)

Line-chart will accept an extra option:

 - 'gradient - (default `0b`) use gradient on bars

For pie-chart, only `title` and `legend` options are considered.

#### plot_add(plot tag & args)

Update a plot or chart and return it.

`Tag` must be one of,

 - 'boxplot - add an additional box to an existing box-plot
 - 'points - plot points on the given scatter-plot or xy-plot of the (x,y) points
 - 'lines - plot lines on the given scatter or line plot (xy-plot) of the (x,y) points
 - 'categories - add additional categories to an existing bar-chart or line-chart

For `'boxplot`, `args` must be `(x options)`.
Options can take the `serieslabel` key.

For `'points`, `args` must be `(x y options)`.
Options can take the `serieslabel` key.

For `'lines`, `args` must be `(x y options)`.
Option keys: `[serieslabel points autosort]`.

For `'categories`, `args` must be `(categories values options)`.
Option keys: `[serieslabel groupby]`.

#### view(x & options)

Display the chart or plot `x`.
Option keys are `[title width height]`.

#### chart_set(chart tag & args)

Update and return a chart or a plot.

Possible values for `tag` and `args`:

 - 'alpha (alpha_level or transparency)
 - 'bgalpha (alpha_level or transparency) -- of the chart's background)
 - 'bgdefault ()
 - 'pointsize (point_size) -- of a scatter plot
 - 'title (title)
 - 'xlabel (label)
 - 'xrange (lower upper) -- range of the x-axis
 - 'ylabel (label)
 - 'yrange (lower upper) -- range of the y-axis

#### save_view(chart filename & options)

Save the chart to an image file.

Option keys: `[width height]`.