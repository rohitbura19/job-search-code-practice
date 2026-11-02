# Reference answers

Java files mirror `src/main/java` (copy them over the stubs to run the tests). The explanation is in each file's header. The Spring gap files are in `answers/e-spring-wallet/src/main/java/practice/e/` (copy over the project's stubs).

| Item | Time | Space |
|---|---|---|
| A1 excelTitle / excelNumber | O(log n) / O(len) | O(1) |
| A1 addBinary | O(max len) | O(max len) |
| A1 countBits | O(n) | O(n) output |
| A2 dailyTemperatures, nextGreaterCircular | O(n) | O(n) |
| A2 removeDuplicates(k) | O(n) | O(n) |
| B1 StaffStats | O(n log n) worst (sorts, TreeMap) | O(n) |
| B1 lruCache | O(1) per get/put | O(capacity) |
| C1 KthLargest.add | O(log k) | O(k) |
| C1 kClosest | O(n log k) | O(k) |
| C1 mergeSorted | O(N log k) | O(k) + output |
| C2 coinChangeMin / coinWays | O(amount * coins) | O(amount) |
| C2 canPartition | O(n * sum/2) | O(sum/2) |
| D1 maxSlidingWindow | O(n) | O(n) as written (O(k) with a ring buffer) |
| E transfer | 2 row-locked reads + 1 insert per request | - |
| G token bucket check | O(1) | O(1) per key |
| G sliding window check | O(1) amortised | O(limit) per key |
| H2 VendingMachine | O(1) per operation | O(slots) |
