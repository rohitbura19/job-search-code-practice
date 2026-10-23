> Code: `src/main/java/practice/d20261023/a2/HouseRobber.java`. Test: `HouseRobberTest`.

# A2. House Robber I and II - 1-D DP with two variables (12 min)

(a) `rob(int[] nums)`: maximum sum of elements with no two adjacent. `[1,2,3,1] -> 4`, `[2,7,9,3,1] -> 12`.

(b) `robCircular(int[] nums)`: the houses form a circle (first and last are adjacent). `[2,3,2] -> 3`, `[1,2,3,1] -> 4`.

Constraints: `0 <= n <= 10^6`, `0 <= nums[i] <= 10^9` (the answer does not fit in `int`: return `long`), O(1) extra space.

Say out loud: the recurrence and why only two values are kept; why (b) is two runs of (a); what changes if negative values are allowed (empty choice) and how you would return the chosen indices (needs the full table or a parent bit).
