# Reference solutions (open only after trying)

Only files you must write are here (given types stay in `src/main/java`). The Java files mirror `src/main/java`; to check them, copy `answers/java/*` over `src/main/java/` in a scratch copy and run `mvn test`.
The Spring solution is under `e-spring-reviews/src/` (copy over the project's `src/`). Each file's header comment has the idea and the complexity.

| Item | File | Time | Space |
|---|---|---|---|
| A1 | java/.../a1/Roman.java | O(n) / O(1) (n = length) | O(1) |
| A2 | java/.../a2/Anagrams.java | isAnagram O(n); group O(n L log L); find O(\|s\| + \|p\|) | O(n); O(1) |
| B1 | java/.../b1/TicketStats.java | O(n) per method; sorted outputs O(n log m) | O(m) |
| C1 | java/.../c1/Matrices.java | spiral O(mn); rotate O(n^2); setZeroes O(mn) | O(mn) output; O(1); O(1) |
| C2 | java/.../c2/SearchOnAnswer.java | O(n log range) | O(1) |
| D1 | java/.../d1/ValidParens.java | O(n) | O(1) |
| E | e-spring-reviews/src/... | - | - |
| G | java/.../g/InMemorySeatHoldService.java | hold O(k log h); confirm O(k); state/availableCount O(1) amortised | O(seats + holds) |
| H1 | java/.../h1/H1Predictions.java | - | the reasoning is in the header |
| H2 | java/.../h2/AbstractApprover.java, LimitApprover.java, CategoryBlocker.java, LoggingApprover.java | O(links) per expense | O(1) |

B1 paper answers: (a) `partitioningBy` always has both keys (an empty list on the empty side); `groupingBy` omits the key, so `map.get(false)` is `null` and a caller NPEs. (b) `6 / 3` -> `2.0`; `teeing` feeds both collectors in one pass, so it works on a one-shot stream where two `collect` calls would throw `IllegalStateException`. (c) a group is never empty (groups exist because of an element); `summingInt` on an empty stream returns `0` and `averagingInt` returns `0.0` (not null, not NaN). (d) `toMap` uses `HashMap.merge`, which throws `NullPointerException` for a null VALUE; a null key is accepted by `HashMap` but two nulls collide (`IllegalStateException: Duplicate key null`), and `Collectors.groupingBy` throws NPE for a null classifier result ("element cannot be mapped to a null key").

C2 notes: bounds: Koko `lo = 1`, `hi = max(piles)`; split `lo = max(nums)`, `hi = sum(nums)`. Predicate: hours(speed) <= h (monotone: faster is never slower); parts(cap) <= k (monotone: a bigger cap needs fewer parts).

E notes: the unique-constraint test relies on the NAMED constraint (`uq_review_product_author`); `ddl-auto: validate` in production means Flyway/Liquibase owns the schema.
