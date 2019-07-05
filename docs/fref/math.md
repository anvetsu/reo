# Math

#### E

The double value that is closer than any other to `e`, the base of the natural logarithms.

#### PI

The double value that is closer than any other to `pi`, the ratio of the circumference of a circle to its diameter.

#### _inf

A constant holding the negative infinity of type double.

#### abs(x)

Return the absolute value of the `double` value `x`.

#### acos(x)

Return the arc cosine of a value; the returned angle is in the range 0.0 through pi.

#### asin(x)

Return the arc sine of a value; the returned angle is in the range -pi/2 through pi/2.

#### atan(x)

Return the arc tangent of a value; the returned angle is in the range -pi/2 through pi/2.

#### atan2(x)

Return the angle theta from the conversion of rectangular coordinates (x, y) to polar coordinates (r, theta).

#### big(x y)

Return the biggest value of `x` and `y`. A burrowing operation.

```lisp
big(1 2)
; 2

big([[1 2 3]] [[0 5 1]])
; [[1 5 3]]
```

#### cbrt(x)

Return the cube root of a double value.

#### ceil(x)

Return the smallest (closest to negative infinity) double value that is greater than
or equal to the argument and is equal to a mathematical integer.

#### cos(x)

Return the trigonometric cosine of an angle.

#### cosh(x)

Return the hyperbolic cosine of a double value.

#### degrees(x)

Convert an angle measured in radians to an approximately equivalent angle measured in degrees.

#### exp(x)

Return Euler's number `e` raised to the power of a double value.

#### expm1(x)

Return e<sup>x</sup> -1.

#### floor(x)

Return the largest (closest to positive infinity) double value that is
less than or equal to the argument and is equal to a mathematical integer.

#### inf

A constant holding the positive infinity of type double.

#### is_inf(x)

Return `true` if `x` is infinity, `false` otherwise.

#### is_nan(x)

Return `true` if `x` is NaN

#### log(x)

Return the natural logarithm (base e) of a double value.

#### log10(x)

Return the base 10 logarithm of a double value.

#### log1p(x)

Return the natural logarithm of the sum of the argument and 1.

#### nan

A constant holding a Not-a-Number (NaN) value of type `double`.

#### nthrt(x n)

Return the `n`<sup>th</sup> root of `x`.

#### pow(x y)

Return the value of the first argument raised to the power of the second argument.
A burrowing operator.

```lisp
pow(2 3)
; 8.000

pow([1 2 3 4] 3)
; [1.000 8.000 27.000 64.000]
```

#### radians(x)

Convert an angle measured in degrees to an approximately equivalent angle measured in radians.

#### sin(x)

Return the trigonometric sine of an angle.

#### sinh(x)

Return the hyperbolic sine of a double value.

#### sml(x y)

Return the smallest value of `x` and `y`. A burrowing operator.

#### sqrt(x)

Return the correctly rounded positive square root of a double value.

#### tan(x)

Return the trigonometric tangent of an angle.

#### tanh(x)

Return the hyperbolic tangent of a double value.