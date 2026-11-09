# Reference answers

Java files mirror `src/main/java` (copy over the stubs to run the tests). Given-complete classes (`Trip`, `H1Snippets`, `IdGenerator`, `ShortLink`, `AliasTakenException`) are not repeated. Explanations are in each file's header. Spring: `answers/e-spring-invoices/` holds the completed gap files (copy over `e-spring-invoices/src/main/java/practice/e`).

| Item | Time | Space |
|---|---|---|
| A1 twoSum, groupAnagrams, majority | O(n), O(n k log k), O(n) | O(n), O(n k), O(1) |
| A2 gcd, lcm, happy, Excel, sieve | O(log n), O(1), O(log n) per step, O(log_26 n), O(n log log n) | O(1) (sieve O(n)) |
| B1 streams | O(n) / O(n log n) for sorted outputs | O(n) |
| C1 bananas, ship, split | O(n log range) | O(1) |
| C2 Kadane family | O(n) | O(1) |
| D1 merge-sort counting | O(n log n) | O(n) |
| G Base62 / UrlShortenerService | O(log_62 id) / O(1) per op, purge O(n) | O(links) |
| H2 RingBuffer | O(1) add/remove/get | O(capacity) |

Key points: twoSum needs a long complement (int overflow matches a wrong element). Majority vote cancels unlike pairs. Search on the answer needs a monotonic feasibility predicate; the loop is `while (lo < hi)` with `hi = mid` or `lo = mid + 1`. Circular Kadane is `max(best, total - worst)` except when all elements are negative. Count-smaller takes left on ties so equal values are not counted. H1 values were produced by running the snippets; the rules are listed in the README review.
