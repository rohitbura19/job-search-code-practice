# D1 - Maximum Gap (hard but short, 20 min)
File: `d1/MaximumGap.java`

Given an int array, return the maximum difference between successive elements in its sorted form (0 if fewer than 2 elements). **O(n) time and space, no comparison sort.** `[3,6,9,1]` -> 3; `[10]` -> 0; `[0, 2147483647]` -> 2147483647. n up to 2*10^6; elements can be any int, so differences must be computed in long.
Hint: how large must the maximum gap be at least, by pigeonhole, and what does that say about numbers in the same bucket?
