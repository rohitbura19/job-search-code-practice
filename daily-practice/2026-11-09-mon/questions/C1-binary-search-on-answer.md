# C1 - Binary search on the answer (25 min) - `c1/BinarySearchOnAnswer.java`

1. `minEatingSpeed(piles, h)`: smallest bananas-per-hour speed so that all piles are finished within h hours (a pile takes `ceil(p / k)` hours; one pile per hour at most). `[3,6,7,11], h=8 -> 4`. Piles up to `Integer.MAX_VALUE`: watch overflow when summing hours.
2. `shipWithinDays(weights, days)`: packages in order, smallest ship capacity to finish within `days`. `[1..10], 5 -> 15`.
3. `splitArray(nums, k)`: split into at most k contiguous parts minimising the largest part sum (long). `[7,2,5,10,8], 2 -> 18`.

State the search interval and the monotonic predicate for each before coding. n up to 200,000: O(n log range).
