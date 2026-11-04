# Reference answers

Java files mirror `src/main/java` (copy them over the stubs to run the tests). The explanation is in each file's header. The Spring gap files are in `answers/e-spring-tickets/src/main/java/practice/e/` (copy over the project's stubs).

| Item | Time | Space |
|---|---|---|
| A1 missingNumber, firstUniqChar | O(n) | O(1) |
| A1 findDuplicates | O(n + d log d) (sorted result of d duplicates) | O(1) besides the result |
| A2 searchInsert, firstLast, findMin | O(log n) | O(1) |
| B1 OrderStreams | O(n) (topCustomers, repeatCustomers, streak, grouping with TreeMap: O(n log n)) | O(n) |
| C1 longestUnique | O(n) | O(alphabet) |
| C1 longestRepeatingReplacement, shortestSubarrayAtLeast | O(n) | O(1) |
| C2 wordBreak | O(n * L) lookups, each hashing up to L chars | O(n + dictionary) |
| C2 minPathSum | O(mn) | O(n) |
| C2 countPalindromicSubstrings | O(n^2) | O(1) |
| D1 longestIncreasingPath | O(mn) | O(mn) |
| E | one query per call; search is a Slice (limit + 1 rows), escalate is a single UPDATE | - |
| G FeatureFlags | evaluation O(rules); put/remove O(flags) (copy-on-write), replaceAll O(flags) | O(flags) |
| H2 Proxies | O(1) per call plus the target | O(distinct memoised calls) |
