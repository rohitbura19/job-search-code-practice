# Reference answers

Java files mirror `src/main/java` (copy them over the stubs to run the tests). The explanation is in each file's header. The Spring gap files are in `answers/e-spring-shipments/src/main/java/practice/e/` (copy over the project's stubs).

| Item | Time | Space |
|---|---|---|
| A1 productExceptSelf, rotate, pivotIndex | O(n) | O(1) besides the result |
| A2 strStr | O(n*m) worst | O(1) |
| A2 reverseWords, zigzag | O(n) | O(n) |
| B1 streams | O(n) / O(n log n) with sorting | O(n) |
| C1 prefix sums + HashMap | O(n) | O(n); divisible-by-k O(k) |
| C2 multi-source BFS (oranges, nearestZero) | O(mn) | O(mn) |
| C2 openLock | O(10^4 * 8) | O(10^4) |
| D1 median of two sorted arrays | O(log min(m, n)) | O(1) |
| G CouponService | O(1) per call | O(coupons + redemptions) |
| H2 Exprs | O(tree size) | O(height) |
