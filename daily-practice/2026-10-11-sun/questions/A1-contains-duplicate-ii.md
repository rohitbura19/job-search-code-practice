> Code: `a1/ContainsDuplicateII.java`. Test: `ContainsDuplicateIITest`.

### A1. Contains Duplicate II (Hashing / Sliding window)
Given `int[] nums` and `int k`, return true if there are two distinct indices `i != j` with `nums[i] == nums[j]` and `|i - j| <= k`.
Example: `[1,2,3,1], k=3 -> true`; `[1,2,3,1,2,3], k=2 -> false`.

Interviewer follow-up: how do you do it in O(k) memory? What if the stream is unbounded?
