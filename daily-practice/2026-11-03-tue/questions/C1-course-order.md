# C1. Topological sort: course schedule (20 min)

> Code: `src/main/java/practice/d20261103/c1/CourseOrder.java`. Test: `CourseOrderTest`.

`n` courses `0..n-1`, `prereq[i] = {course, prerequisite}`.

1. `canFinish(n, prereq)`.
2. `order(n, prereq)`: the lexicographically smallest valid order; empty array if there is a cycle. Example: `n=3, {{0,1},{0,2}}` -> `[1,2,0]`.
3. `minSemesters(n, prereq)`: any number of courses per semester, prerequisites must be from earlier semesters; -1 on a cycle. Example `n=4, {{1,0},{2,0},{3,1},{3,2}}` -> 3.

Constraints: n up to 100_000, edges up to 200_000, duplicate edges and self-loops possible. O((n + e) log n) for 2, O(n + e) for 3. A 100_000-long chain must not recurse.
