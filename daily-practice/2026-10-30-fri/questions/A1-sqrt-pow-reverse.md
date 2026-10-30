# A1. Integer square root, fast power, reverse digits (10 min)

Implement in `a1/Numbers.java`.

1. `sqrt(int x)`: floor of the square root, `0 <= x <= 2^31 - 1`, without `Math.sqrt`. `sqrt(8) = 2`, `sqrt(2147483647) = 46340`.
2. `pow(double x, int n)`: `x^n` in O(log |n|). `n` may be negative or `Integer.MIN_VALUE`.
3. `reverse(int x)`: reverse the decimal digits; return 0 on int overflow. `reverse(-120) = -21`, `reverse(1534236469) = 0`.

Constraints: no `String`, no `Math.pow`/`Math.sqrt`; overflow must be handled before it happens (for `reverse`) or by widening (for `sqrt`, `pow`).
Say out loud: which expression overflows in the naive version of each?
