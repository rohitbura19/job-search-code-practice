# Reference solutions (open only after trying)

Only files you must write are here (given types stay in `src/main/java`). The Java files mirror `src/main/java`; to check them, copy `answers/java/*` over `src/main/java/` in a scratch copy and run `mvn test`.
The Spring solution is under `e-spring-reading-list/src/` here (copy over the project's `src/`). Each file's header comment has the idea and the complexity.

| Item | File | Time | Space |
|---|---|---|---|
| A1 | java/.../a1/RangesAndHappy.java | O(log n * steps) / O(n) / O(n) | O(1) / O(1) / O(alphabet) |
| A2 | java/.../a2/GridAndDigits.java | O(81) / O(n) / O(total chars) | O(1) |
| B1 | java/.../b1/LogStats.java | O(n); sorted outputs O(m log m) | O(m) |
| C1 | java/.../c1/Connectivity.java | O(n^2 alpha) / O(n alpha) / O(n) | O(n) |
| C2 | java/.../c2/Backtracking.java | O(n 2^n) / O(n Catalan(n)) / ~O(n!) pruned | O(n) recursion + output |
| D1 | java/.../d1/ShortestPalindrome.java | O(n) | O(n) |
| E | e-spring-reading-list/src/... | - | - |
| G | java/.../g/InMemoryChatService.java | send O(1); fetch O(limit); markRead O(1); unread O(unread) | O(messages) |
| H1 | java/.../h1/H1Predictions.java | - | the reasoning is in the header |
| H2 | java/.../h2/Semver.java | parse O(len); compare O(identifiers) | O(identifiers) |

B1 paper answers: (a) `dropWhile` discards the prefix and `takeWhile` stops at the first failure, so you get ONE run; `filter` would collect every run. Both are short-circuiting on ordered streams; on unordered streams the result is any valid subset-prefix, i.e. non-deterministic. (b) `mapMulti` avoids allocating a stream per element: cheaper when most inputs yield 0-1 outputs or the output is small. (c) The 3-arg `groupingBy` takes the map factory, so the result is a real `TreeMap`; insert is O(log m) against O(1) for a `HashMap`.
H2 paper answers: with `BigDecimal`, `equals` says 1.0 != 1.00 but `compareTo` says 0, so a `HashSet` holds both and a `TreeSet` holds one. `final` + private constructor prevents subclasses from breaking the invariants; `List.copyOf` stops callers mutating the identifiers. Length-then-text avoids overflow and parsing for arbitrarily long digit strings.
E paper answers: the cache stores references, so a projection proxy (bound to the persistence context) or an entity would leak state and may fail after the session closes; records are immutable and detached. With `open-in-view=false` any lazy `tags` access after the service returns throws `LazyInitializationException`, which is why the service builds the view inside its transaction. A single tag value per query matches each article once; `distinct` is needed when you join on several tags (`t in :tags`).
