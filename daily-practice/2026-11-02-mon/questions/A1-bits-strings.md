# A1. Excel columns, binary addition, counting bits (10 min)

> Code: `src/main/java/practice/d20261102/a1/BitsAndStrings.java`. Test: `BitsAndStringsTest`. **10 min.**

1. `excelTitle(n)` / `excelNumber(title)`: 1 -> A, 26 -> Z, 27 -> AA, 701 -> ZY, 2147483647 -> FXSHRXW. The numbering is bijective base 26 (there is no zero digit).
2. `addBinary(a, b)`: add two binary strings of length up to 10^5, inputs may have leading zeros, output must not (except "0").
3. `countBits(n)`: `out[i]` = number of 1 bits of i for i in 0..n, O(n) total, without `Integer.bitCount`.

Constraints: `1 <= n <= 2^31 - 1` for titles; `0 <= n <= 5*10^6` for countBits. Say out loud why `n % 26` alone is wrong for titles.
