# Reference answers

Java files mirror `src/main/java` (copy over the stubs to run the tests); classes that are given complete in `src/main` (`ListNode`, `Product`, `TreeNode`, `CartLine`, `CartView`, `PriceCatalog`, `DiscountRule`, `H1Snippets`) are not repeated. Explanations are in each file's header. Spring: `answers/e-spring-tasks/` holds the completed gap files (copy over `e-spring-tasks/src/main/java/practice/e`).

| Item | Time | Space |
|---|---|---|
| A1 stairs, min-cost stairs, tribonacci | O(n) | O(1) |
| A2 merge, remove n-th, middle | O(n) | O(1) |
| B1 streams | O(n) / O(n log n) for sorted outputs | O(n) |
| C1 buildTree (iterative) | O(n) | O(depth) |
| C1 serialize / deserialize | O(n) | O(width) |
| C2 longest palindrome (expand) | O(n^2) | O(1) |
| C2 target sum | O(n * (sum+target)/2) | O(sum) |
| D1 sliding window median | O(n log k) | O(k) |
| G CartService | O(lines log lines) per op | O(total lines) |
| H2 Spec | O(leaves) per evaluation | O(leaves) |
