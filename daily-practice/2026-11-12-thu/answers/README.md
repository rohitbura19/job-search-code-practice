# Reference answers

Java files mirror `src/main/java` (copy over the stubs to run the tests). Given-complete classes (`Movement`, `H1Snippets`, `Handler`) are not repeated differently. Explanations are in each file's header. Spring: `answers/e-spring-reviews/` holds the completed gap files (copy over `e-spring-reviews/src/main/java/practice/e`).

| Item | Time | Space |
|---|---|---|
| A1 roman both ways, common prefix | O(n), O(1), O(total chars) | O(1) |
| A2 spiral, rotate, Toeplitz | O(mn) each | O(1) beyond the output |
| B1 streams | O(n), sorts O(s log s) over distinct SKUs | O(n) |
| C1 provinces, redundant edge, accounts merge | O(n^2 alpha), O(E alpha), O(E log E) | O(n), O(n), O(E) |
| C2 merge, insert, rooms, erase | O(n log n), O(n), O(n log n), O(n log n) | O(n) |
| D1 burst balloons | O(n^3) | O(n^2) |
| G AutocompleteService | record O(L); suggest O(L + S log k), S = subtree size | O(total characters) |
| H2 Pipeline | O(handlers) per execute | O(handlers) stack |

Key points: DSU = path halving + union by size. Intervals: sort by start for merging, by end for the greedy. Burst balloons: choose the LAST balloon in (l, r). Reviews: the unique constraint is the real duplicate guard; the service check is a courtesy. H1 values come from running the snippets on JDK 21; the rules are in the README review.
