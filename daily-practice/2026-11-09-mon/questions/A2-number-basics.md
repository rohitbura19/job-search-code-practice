# A2 - Number basics (10 min) - `a2/NumberBasics.java`

1. `gcd(a, b)` (Euclid, non-negative; `gcd(0,0) = 0`) and `lcm(a, b)` as `long` (`lcm(Integer.MAX_VALUE, Integer.MAX_VALUE - 1)` overflows int; `lcm` with 0 is 0).
2. `isHappy(n)`: replace n by the sum of squared digits until it reaches 1 (happy) or loops forever. `19 -> 82 -> 68 -> 100 -> 1` is happy, `2` is not. O(1) space (cycle detection, no set).
3. `excelTitle(int)` / `excelNumber(String)`: `1 -> "A"`, `26 -> "Z"`, `27 -> "AA"`, `702 -> "ZZ"`, `703 -> "AAA"`. It is base 26 without a zero digit.
4. `countPrimes(n)`: primes strictly below n, n up to 5,000,000, within 2 s.
