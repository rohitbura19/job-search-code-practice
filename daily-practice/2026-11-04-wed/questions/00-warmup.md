# Warm-up (10 min, blank page, no IDE)

1. XOR trick: why does `n ^ (0^a0) ^ (1^a1) ...` leave exactly the missing number, and why is the sum version risky? What changes if TWO numbers are missing?
2. Binary search: write the loop invariant for "first index with a[i] >= t" (what do `lo` and `hi` mean?). Why `hi = a.length` and `lo < hi` instead of `hi = a.length - 1` and `lo <= hi`?
3. Sliding window: why does the "at most k replacements" window never need to shrink `maxFreq` when letters leave it, and what do you pay for that shortcut?
4. A feature flag at 20 % must give the same answer for the same user on every request and every server. How do you do that without storing anything per user, and why must raising 20 % -> 40 % not flip anyone off?
