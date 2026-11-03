# Reference answers

Java files mirror `src/main/java` (copy them over the stubs to run the tests). The explanation is in each file's header. The Spring gap files are in `answers/e-spring-bookings/src/main/java/practice/e/` (copy over the project's stubs).

| Item | Time | Space |
|---|---|---|
| A1 majorityElement | O(n) | O(1) |
| A1 pascalRow | O(r^2) | O(r) |
| A1 isValid | O(n) | O(n) |
| A2 diameter, isBalanced, hasPathSum | O(n) | O(h) |
| B1 MapApi | O(n) (invert/topK O(n log n)) | O(n) |
| C1 order | O((n + e) log n) | O(n + e) |
| C1 canFinish, minSemesters | O(n + e) (canFinish via order: with log) | O(n + e) |
| C2 lis | O(n log n) | O(n) |
| C2 lcs | O(nm) | O(min(n, m)) |
| C2 robCircular | O(n) | O(1) |
| D1 trapRainWater | O(mn log(mn)) | O(mn) |
| E create booking | 1 overlap query + 1 insert | - |
| G JobScheduler | O(log n) schedule / per run, O(1) cancel | O(n) |
| H2 Composite + Visitor | size and visitors O(subtree) | O(depth) for the call stack |
