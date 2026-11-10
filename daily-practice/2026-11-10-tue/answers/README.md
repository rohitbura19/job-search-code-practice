# Reference answers

Java files mirror `src/main/java` (copy over the stubs to run the tests). Given-complete classes (`Reading`, `TimeRange`, `Event`, `ConflictException`, `H1Snippets`) are not repeated. Explanations are in each file's header. Spring: `answers/e-spring-subscriptions/` holds the completed gap files (copy over `e-spring-subscriptions/src/main/java/practice/e`).

| Item | Time | Space |
|---|---|---|
| A1 sortedSquares, intersect, rotateRight | O(n), O(n + m), O(n) | O(n), O(min), O(1) |
| A2 compress, IPv4, addStrings | O(n) each | O(1), O(1), O(n) |
| B1 streams | O(n) / O(n log n) for sorted outputs | O(n) |
| C1 bipartite, partition, safe nodes | O(V + E) | O(V + E) |
| C2 maximal square, cooldown, perfect squares | O(rc), O(n), O(n sqrt n) | O(c), O(1), O(n) |
| D1 histogram | O(n) | O(n) |
| G CalendarService | schedule/cancel O(p log n) per p participants; agenda O(log n + k); freeSlots O(B log B) | O(events x participants) |
| H2 Money | O(1) per op; allocate O(parts) | O(parts) |

Key points: squares need `long` before squaring at the int boundary. 2-colouring fails on an equal-colour edge (odd cycle); safe nodes = Kahn on the reversed graph from the terminals. Cooldown DP has three states (hold, sold, rest). Histogram pops finalise a bar: right edge = current index, left edge = new stack top + 1. Calendar overlap needs only `floorEntry(start)` and the first event starting inside the range; validate everything before mutating. Money allocation truncates then distributes the leftover from the first part. H1 values were produced by running the snippets (JDK 21); the rules are in the README review.
