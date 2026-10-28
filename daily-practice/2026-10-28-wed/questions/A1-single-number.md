> Code: `src/main/java/practice/d20261028/a1/SingleNumber.java`. Test: `SingleNumberTest`. **15 min.**

# A1. Single Number I, II, III (bit manipulation)

1. `once`: every value appears twice except one. `[2,2,1,1,4]` -> `4`.
2. `onceAmongThrees`: every value appears three times except one. `[2,2,3,2]` -> `3`; `[0,1,0,1,0,1,99]` -> `99`. No hash map.
3. `twoSingles`: two values appear once, all others twice; return both, smaller first. `[1,2,1,3,2,5]` -> `[3,5]`.

Constraints: `1 <= n <= 3*10^6`, values are any `int` (negatives, `MIN_VALUE`). O(n) time, O(1) extra space.
Say out loud: why `x & -x` isolates the lowest set bit and still works for `Integer.MIN_VALUE`; what the `ones`/`twos` pair counts per bit.
