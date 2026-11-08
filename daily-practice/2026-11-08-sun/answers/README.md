# Reference answers

Java files mirror `src/main/java` (copy over the stubs to run the tests). Given-complete classes (`Txn`, `StoredResponse`, `Outcome`, `Command`, `H1Snippets`) are not repeated, except `Command` which is copied. Explanations are in each file's header. Spring: `answers/e-spring-courses/` holds the completed gap files (copy over `e-spring-courses/src/main/java/practice/e`).

| Item | Time | Space |
|---|---|---|
| A1 sortColors, moveZeroes, removeDuplicates | O(n) | O(1) |
| A2 RPN, MinStack, QueueViaStacks | O(n) / O(1) per op (amortised for the queue) | O(n) |
| B1 streams | O(n) / O(n log n) for sorted outputs | O(n) |
| C1 Dijkstra (delay, effort) | O((V+E) log V) | O(V+E) |
| C1 cheapest flights (Bellman-Ford, k+1 rounds) | O(k*E) | O(V) |
| C2 Trie ops | O(len) | O(total chars * 26) |
| C2 replaceWords / longestBuildableWord | O(total chars) / + sort | O(total chars) |
| D1 Hierholzer | O(E log E) | O(E) |
| G IdempotencyService | O(1) per op, purge O(n) | O(keys) |
| H2 Editor | O(1) + command cost | O(history) |
