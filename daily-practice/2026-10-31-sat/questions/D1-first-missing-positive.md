# D1. First Missing Positive (hard, short, 20 min) - `d1/MissingPositive.java`

`firstMissingPositive(int[] nums)` returns the smallest positive integer not in `nums`. **O(n) time and O(1) extra space**; you may rearrange the array.
`[1,2,0] -> 3`; `[3,4,-1,1] -> 2`; `[7,8,9,11,12] -> 1`; `[] -> 1`; `[1,1] -> 2`. Values may be negative, zero, duplicated, `Integer.MAX_VALUE`; n up to 2*10^6.
Hint: the answer is in `[1, n+1]`. Is the nested `while` really O(n)? Say why (each swap places one value for good).
