# Statistics

#### dataset(column_names & data)

Return a dictionary of type `dataset` constructed from the given column-names and
data. The data is either a sequence of sequences or a sequence of dictionaries.

#### tabds(t)

Return a new `dataset` initialized from the given columnar/relational table.

#### median(xs)

Return the median of the data.

#### mean(xs)

Return the mean of the data.

#### sd(xs)

Return the sample standard deviation of the data.

#### v(xs)

Return the sample variance of the data.

#### cdf(tag x & options)

Computes a cumulative-distribution function.

Values for `tag` and default `options`:

 - 'beta ['alpha:1 'beta:1 'lowertail:0b]
 - 'binomial ['size:1 'prob:1/2 :lowertail:1b]
 - 'chisq ['df:1 'lowertail:1b]
 - 'empirical []
 - 'exp ['rate:1]
 - 'f ['df1:1 'df2:1 'lowertail:1b]
 - 'gamma ['shape:1 'scale:(1 | 1/rate) 'rate:1/scale 'lowertail:1b]
 - 'negbinomial ['size:10 'prob:1/2 'lowertail:1b]
 - 'normal ['mean:0 'sd:1]
 - 'poisson ['lambda:1 'lowertail:1b]
 - 't ['df:1 'lowertail:1b]
 - 'uniform ['min:0.0 'max:1.0]
 - 'weibull ['shape:1 'scale:1]
	    
#### pdf(tag x & options)

Computes a probability-density function.

Valid values for `tag` and default `options`:

 - 'beta ['alpha:1 'beta:1]
 - 'binomial ['size:1 'prob:1/2]
 - 'chisq ['df:1]
 - 'exp ['rate:1]
 - 'f ['df1:1 'df2:1]
 - 'gamma ['shape:1 'scale:(1 | 1/rate) 'rate:1/scale]
 - 'negbinomial ['size:10 'prob:1/2]
 - 'normal ['mean:0 'sd:1]
 - 'poisson ['lambda:1]
 - 't ['df:1]
 - 'uniform ['min:0.0 'max:1.0]
 - 'weibull ['shape:1 'scale:1]

#### chisqtest(xs & options)

Performs chi-squared contingency table tests and goodness-of-fit tests.

Options:

- 'x - a sequence of numbers.
- 'y - a sequence of numbers
- 'table - a contingency table. If one dimensional, the test is a goodness-of-fit
- 'probs - when(is_null(y)) - (repeat n-levels (/ n-levels)))
- 'freq (default `nul`) - if given, these are rescaled to probabilities
- 'correct (default `1b`) - use Yates' correction for continuity for 2x2 contingency tables
  
#### skewness(x)

Return the skewness of the data.

#### quantile(x & options)

Returns the quantiles of the data.

Option keys:

 - 'probs (default [0.0 0.25 0.5 0.75 1.0])

```rust
? quantile(sample_normal(100))

? quantile(sample_normal(100) ['probs:[0.025 0.975]])

```

#### sample_normal(size & options)

Return a sample of the given size from a Normal distribution.
Default `options`: `['mean:0 'sd:1]`

#### cor(x y), cor(mat)

Return the sample correlation of `x and `y, or the correlation
matrix of the given matrix.

#### cov(x y), cov(mat)

Return the sample covariance of `x` and `y` or the given matrix.

#### cmean(xs)

Return a sequence of cumulative means for the given collection.

#### distance(tag x y & xs)

`Tag` must be one of:

```
'euclidean, 'chebyshev, 'hamming, 'jaccard
'levenshtein 'lee 'manhattan 'minkowski 'normalized_kendall_tau
'mahalanobis
```
For `mahalanobis`, `y` must be a dictionary with keys `[y W centroid]`.

#### jaccard_index(a b)

Return the Jaccard index, also known as the Jaccard similarity coefficient.

#### odds_ratio(p1 p2)

See [http://en.wikipedia.org/wiki/Odds_ratio](http://en.wikipedia.org/wiki/Odds_ratio)

#### permute(xs & ys)

Return a permuted version of `xs` or a permutation across `xs` and `ys.

#### predict(model x)

Take a linear-model and an `x` value (either a scalar or vector)
and return the predicted value based on the linear-model.

#### summary(ds)

Return the summary of the dataset.

#### sweep(xs & options)

Return an array obtained from an input array by sweeping out a summary statistic.

Options:

'stat - (default mean) the statistic to sweep out
'fun - (default minus) the function used to sweep the stat out
      
#### gauss(& mean sd)

Return a Normal distribution.

#### draw(d)

Return a randomly drawn value from the support of distribution `d`.

#### zscore(sd mean x)

See [https://en.wikipedia.org/wiki/Standard_score](https://en.wikipedia.org/wiki/Standard_score)
