> Code: `c1/KthLargest.java`. Test: `KthLargestTest` (includes timeouts that catch a quadratic partition). **15 min timer, plain editor.**

### C1. Kth Largest Element in an Array (Quickselect)
Return the `k`-th largest element of `nums` (the k-th in sorted order from the top, duplicates counted). `[3,2,1,5,6,4], k = 2 -> 5`; `[3,2,3,1,2,4,5,5,6], k = 4 -> 4`.

Interviewer follow-up: state three solutions with their costs (sort, a min-heap of size `k`, Quickselect) and when each one wins. Why is Quickselect expected O(n) when it only recurses into ONE side (the sum n + n/2 + n/4 + ...)? What is the worst case, and how does a random pivot make it unlikely? What goes wrong with the textbook Lomuto partition when MANY elements are equal (all values equal: how many comparisons?) and how does a 3-way partition fix it? Why do you modify the input, and how do you avoid that? The data is 10^9 numbers on disk or spread over machines: what do you do? The numbers arrive as a stream and `k` is small? What is the median-of-medians guarantee and why is it rarely used?
