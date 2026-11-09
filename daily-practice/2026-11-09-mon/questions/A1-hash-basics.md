# A1 - Hash basics (10 min) - `a1/HashBasics.java`

1. `twoSum(int[] nums, int target)`: exactly one pair `i < j` with `nums[i] + nums[j] == target`; return `{i, j}`. O(n). Careful: `target - nums[j]` overflows in int (`target = 10`, `nums[j] = Integer.MIN_VALUE`).
2. `groupAnagrams(String[] words)`: group anagrams, any order. `["eat","tea","tan","ate","nat","bat"]` -> `[["eat","tea","ate"],["tan","nat"],["bat"]]`. The empty string is a word; duplicates stay.
3. `majorityElement(int[] a)`: a value occurs more than n/2 times (guaranteed). O(n) time, O(1) space. `[2,2,1,1,1,2,2]` -> 2.

Constraints: n up to 2,000,000; values any int.
