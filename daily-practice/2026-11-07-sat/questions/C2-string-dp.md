# C2 - Palindrome + target sum (20 min) - `c2/StringDp.java`

1. `longestPalindrome(String s)` - longest palindromic substring, `|s| <= 5000`; among several of the maximum length return the one starting first. `"babad"` -> `"bab"`, `"cbbd"` -> `"bb"`, `""` -> `""`.
2. `long targetSum(int[] nums, int target)` - count the ways to put `+` or `-` before every number so the expression equals `target`. `0 <= nums[i] <= 1000`, sum <= 5000, n <= 60, result fits in long. `[1,1,1,1,1], 3` -> 5; zeros double the count (`[0,0,1], 1` -> 4). Derive: `P - N = target`, `P + N = sum`, so count subsets with sum `(sum+target)/2`.
