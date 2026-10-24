# Sat 24 Oct 2026 - practice

Weekend session: one focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the recurrence or invariant in one sentence each; mark "needed hints" in the score card.

## Order and time boxes (about 3 h)

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 palindromes I/II (`questions/A1-palindromes.md`) | 10 | a1/Palindromes.java | PalindromesTest |
| 2 | A2 list cycle (Floyd) | 15 | a2/ListCycle.java | ListCycleTest |
| 3 | C1 rotated array: search + min with duplicates | 25 | c1/Rotated.java | RotatedTest |
| 4 | C2 rotting oranges + shortest path in a matrix | 25 | c2/Grids.java | GridsTest |
| 5 | D1 minimum window substring | 25 | d1/MinWindow.java | MinWindowTest |
| 6 | E Spring Boot: paging + projection + properties (`e-spring-catalog/`) | 35 | that project | ProductRepositoryTest, ProductControllerTest, ProductServiceTest, CatalogPropertiesTest |
| 7 | B1 collectors and sorted maps | 20 | b1/InventoryStats.java | InventoryStatsTest |
| 8 | H1 predict the output | 20 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 sealed `Expr` + pattern matching (OOP) | 30 | h2/Exprs.java | ExprsTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review) | 30 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1 (sketch 10, code 25) | 35 | my-designs/G-classes-sketch.md, then g/ | DefaultFlagEvaluatorTest |

Core set if short on time: 3, 5, 6, 8 and F1 out loud (about 1.5 h). Code paths are under `src/main/java/practice/d20261024/`. Run one test class: `mvn test -Dtest=MinWindowTest`. Reference solutions: `answers/` (the Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities).

## Score card

| Section | Done? | Tests green without hints? | Minutes | Weak point to revisit |
|---|---|---|---|---|
| Warm-up | | - | | |
| A1 / A2 | | | | |
| B1 | | | | |
| C1 / C2 | | | | |
| D1 | | | | |
| E | | | | |
| F1 / F2 | | - | | |
| G | | | | |
| H1 / H2 | | | | |

## Principal-engineer review: follow-ups and how to make each design more efficient

- **A1 / A2:** the follow-up for A1 is "why is one deletion enough": the first mismatch pair must lose one of its two characters, and the rest is a plain check. Unicode: iterate code points. For A2 prove the entry-point algebra (`a = k*c - b`) and get the cycle length by walking once around from the meeting point; Brent's algorithm does fewer pointer reads than Floyd; in production a visited set with identity semantics is fine when memory is not the constraint.
- **B1:** six passes over the list could be one pass with a loop updating several maps; keep streams for clarity and use loops when profiling says so. `qtyByExpiry` plus `subMap` answers many range queries in O(log n + k); a prefix-sum array over sorted dates makes each range O(log n) after one O(n) build. Quantities as `int` overflow in a big warehouse: use `long`.
- **C1:** both are O(log n) on distinct values; with duplicates the worst case is O(n), which is provable (an array of equal values with one different element hides it). Variant: find the pivot first, then do a plain binary search on the right half, which is easier to reason about but costs two passes. Say how you would test it: every rotation of every small sorted array, with every target.
- **C2:** O(cells) time and an `int[]` queue instead of `ArrayDeque<int[]>` (no boxing, no per-cell allocation). For (b), bidirectional BFS roughly square-roots the explored area on open grids; A* with the Chebyshev distance helps when the grid is mostly open; 0-1 BFS or Dijkstra when cells have costs.
- **D1:** one pass, O(1) space with the 128-entry table; for Unicode use a map keyed by code point and a deficit counter. For a huge text read as a stream keep only the window in a ring buffer. Related: all anagram positions (fixed window), smallest window containing all words (the same idea on word indices).
- **E:** `Page` costs an extra `count(*)`; use `Slice` when the client only needs "has next", and keyset paging (`where (name, id) > (:lastName, :lastId) order by name, id limit :n`) to avoid `OFFSET` scans and shifting pages, backed by an index on `(category, active, name, id)`. The `existsBySku` check is a courtesy; the unique constraint is the guard. Soft delete leaves a unique `sku` blocked: decide whether a deactivated sku can be reused (partial unique index where `active`). Cache the `summary` for a few seconds and invalidate on writes, or maintain counters.
- **F1 / G:** evaluate in the SDK against a locally cached ruleset (no network on the hot path); push changes with SSE or long-poll plus a polling fallback and a monotonically increasing ruleset version; ETag makes the poll cheap; CRC32 bucketing is allocation-light, but precompute the key hash prefix per flag and hash only the user id when profiling shows it matters. Add a "variant" (multivariate) result instead of a boolean, exposure logging sampled per (flag, user), and a flag-expiry date so dead flags get removed.
- **F2:** isolate per endpoint (a queue or a per-endpoint concurrency cap) so one 30 s endpoint cannot consume all workers; adaptive concurrency (AIMD) toward each endpoint; disable an endpoint after N days of failure and notify the owner; sign `timestamp.body` and let receivers reject old timestamps; ordering per endpoint is expensive (a single in-flight request per key) and usually better replaced by an event sequence number the receiver can use to reorder.
- **H1:** the follow-up is the rule behind each result: `Integer.valueOf` cache (JLS 5.1.7), `ArrayList` iterator `hasNext` vs `checkForComodification`, class initialisation order (JLS 12.4, 12.5), implicit narrowing in compound assignment (JLS 15.26.2), `String.split` trailing-empty rule, and the `equals`/`hashCode` contract.
- **H2:** adding an operation is one more method; adding a subtype touches every switch, which the compiler reports: that is the trade-off versus a visitor. For deep trees use an explicit stack or an iterative post-order. For `simplify`, iterate to a fixpoint or normalise (sort operands of `+` and `*`) if you want `x + 1` and `1 + x` to match; say why you did not fold `x - x` (valid, but only when evaluation cannot fail).
