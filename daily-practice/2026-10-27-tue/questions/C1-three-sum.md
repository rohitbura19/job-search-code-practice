> Code: `src/main/java/practice/d20261027/c1/ThreeSum.java`. Test: `ThreeSumTest`. **25 min.**

# C1. 3Sum and 3Sum Closest

1. `threeSum(nums)`: all distinct triplets (by value) summing to 0, each ascending, the list in lexicographic order. `[-1,0,1,2,-1,-4]` -> `[[-1,-1,2],[-1,0,1]]`. `[0,0,0,0]` -> `[[0,0,0]]`. Do not mutate the input.
2. `threeSumClosest(nums, target)`: the sum of three elements (different indices) closest to `target`. `[-1,2,1,-4]`, 1 -> 2.

Constraints: `n <= 3000`, `|nums[i]| <= 10^5`. Target O(n^2). Say out loud: where exactly the duplicate skipping goes (at `i`, and after a hit on both pointers) and why sorting makes the output order free.
