# Warm-up (10 min, blank page, no IDE)

1. Floyd's cycle detection on the "happy number" sequence: why does the sequence always either hit 1 or cycle (bound the next value for a 10-digit input)? Why is O(1) space possible?
2. Union-Find: write `find` with path halving and `union` by size. What is the amortised cost, and why does recursion in `find` break on a 200 000-node path?
3. In `subsetsWithDup` the guard is `i > start && a[i] == a[i-1]`, not `i > 0`. What breaks with `i > 0`? Give a 3-element input.
4. KMP prefix function: what does `pi[i]` mean, and why does the separator between `s` and `reverse(s)` matter?
5. Why does `Stream.iterate(...).takeWhile(v -> v < Long.MAX_VALUE)` need a saturating add for Fibonacci?
Mark "needed hints" in the score card.
