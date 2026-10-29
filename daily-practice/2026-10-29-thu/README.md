# Thu 29 Oct 2026 - practice

One focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the invariant or recurrence in one sentence each; mark "needed hints" in the score card.

## Order and time boxes (about 3 h)

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 Roman numerals both ways (`questions/A1-roman-numerals.md`) | 15 | a1/Roman.java | RomanTest |
| 2 | A2 anagrams: check, group, find all in a string (counters + sliding window) | 15 | a2/Anagrams.java | AnagramsTest |
| 3 | C1 matrix in place: spiral order, rotate, set zeroes with O(1) space | 25 | c1/Matrices.java | MatricesTest |
| 4 | C2 binary search on the answer: Koko bananas + split array | 25 | c2/SearchOnAnswer.java | SearchOnAnswerTest |
| 5 | D1 longest valid parentheses (O(1) space) | 25 | d1/ValidParens.java | ValidParensTest |
| 6 | E Spring Boot: unique constraint + JPQL aggregate + bulk update + validation + paging + ProblemDetail (`e-spring-reviews/`) | 35 | that project | ReviewRepositoryTest, ReviewControllerTest, ReviewServiceTest, ReviewPropsTest |
| 7 | B1 streams and collectors on support tickets (`teeing`, `partitioningBy`) | 20 | b1/TicketStats.java | TicketStatsTest |
| 8 | H1 predict the output | 20 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 chain of responsibility + template method + decorator (OOP) | 30 | h2/*.java | ApprovalChainTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review) | 30 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1: seat holds with expiry (sketch 10, code 30) | 40 | my-designs/G-classes-sketch.md, then g/ | InMemorySeatHoldServiceTest |

Core set if short on time: 3, 4, 5, 6, 8 and F1 out loud (about 1.5 h). Code paths are under `src/main/java/practice/d20261029/`. Run one test class: `mvn test -Dtest=MatricesTest`. Reference solutions: `answers/` (the Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities and the paper answers).

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

- **A1 / A2:** Roman parsing is a one-symbol look-ahead; a 4-row lookup per decimal digit makes `toRoman` branch-free. Anagram keys: sorting each word is O(L log L); a 26-count signature is O(L) and also a valid key (join the counts), and for arbitrary Unicode use a `HashMap` of code points or sort. The sliding-window version avoids re-counting each window: `matched` changes only when a letter's count crosses its target, which is why a step is O(1) and the whole scan O(n) with O(1) space; it works on a stream. Hashing strings per window (rolling hash with a sorted multiset hash) is the route when the alphabet is huge, at the price of collision handling.
- **B1:** six methods, six passes; a single custom `Collector` computes all of them in one. `teeing` merges two downstream collectors without a second pass, which is the only option for a one-shot stream (a `Supplier<Stream>` re-reads the source). Grouping into a `TreeMap` costs O(m log m) over the m teams, not over the tickets; if you only need the top k assignees, a bounded heap beats sorting all of them. `partitioningBy` always returns both keys, `groupingBy` only the non-empty ones: choose by whether the caller dereferences the missing key. `parallelStream` only pays off for large CPU-bound sources with associative, stateless combiners.
- **C1:** the matrix tasks are cache stories: row-major traversal is sequential, column walks (the transpose, the right-hand edge of the spiral) stride by a whole row and miss the cache on big matrices, so block the transpose into tiles (e.g. 32 x 32) and it is several times faster at 3000 x 3000. In-place rotation by four-way cycles swaps each cell once with 3 reads/writes less than transpose + reverse but is harder to get right: use it only if profiling shows the extra pass matters. `setZeroes` with markers in the first row/column saves O(m + n) memory for one more subtle step; with two `boolean[]` arrays it is trivially correct and easier to parallelise (rows are independent in the fill phase), so prefer that unless memory is the constraint. For sparse matrices store only the non-zero coordinates, and for image rotation think of a rotation as a view (swap the index mapping) rather than a data move.
- **C2:** both solutions are O(n log range) because the predicate is a linear scan; the scan cannot be skipped, but the log factor shrinks by tightening bounds (`lo = ceil(sum / h)` for Koko, `max(max element, ceil(sum / k))` for splitting). The DP for `splitArray` is O(k n^2) (O(k n) with monotone-optimisation tricks): use it when the cost function is not monotone in a single threshold or when you need the split points; the binary search gives the value, and a second greedy pass at the optimum reconstructs the parts. For floating-point answers (a speed that need not be an integer) iterate a fixed number of times instead of comparing doubles with `==`. The common bug is `hi = mid - 1` with `lo < hi` (infinite loop or skipped answer): fix the invariant "answer in [lo, hi]" first.
- **D1:** the two-pass counter version is O(n) time, O(1) space, but it is a trick: the stack of indices (O(n) space) is easier to extend (return the substring, handle several bracket kinds) and the DP version generalises to "number of valid substrings". In production the question is usually "validate and locate the first error": one pass with a stack of open positions gives the exact offset of the error, which is what a parser needs. A streaming validator needs only the counter if there is a single bracket kind and a stack of kinds otherwise (depth bound = memory bound, so cap it for untrusted input).
- **E:** `avg(rating)` over every review of a hot product on each page view is O(reviews): store `count` and `sum` per product in a summary row updated in the same transaction as the insert (or on an event) and read that row; the JPQL aggregate stays as the reconciliation job. Offset paging costs O(offset); a deep review list needs keyset paging on `(createdAt, id)`, which the composite index already supports. The bulk `UPDATE` skips optimistic locking and lifecycle callbacks: fine for a status flip, wrong if hiding must be audited (then write an audit row in the same transaction or use an entity with `@Version`). The duplicate check is a friendly message only; two concurrent requests both pass it and the unique constraint decides, so the handler must map the constraint violation. Cap `size` server side (done) and `text` length (done) so one request cannot allocate unbounded memory; put a rate limit per author in front.
- **F1 / G:** the hold is a compare-and-set on N seats: in Redis a Lua script (`for each seat: if set then abort; then SET seat holder EX 300`) is atomic per shard, and one event is one hot key set, so shard an event's seats into sections (a hold may not span sections, or use a two-phase hold with ordered section locking to avoid deadlocks). The in-memory `InMemorySeatHoldService` holds one monitor per event: that is the simplest correct design and does not scale past one core per event; sectioned locks or a single-writer actor per section would. Lazy expiry (check `expiresAt` on read) is correct even if the sweeper is late; the sweeper (or the TTL key) only reclaims memory and fires the "seat available again" push. `confirm` must be idempotent and must re-check expiry under the same lock that books the seat; a payment callback that arrives after expiry should either re-acquire the seat if still free or refund. Serve the seat map from a read model updated by events (or a short-TTL cache) and tolerate staleness: the hold call is the only authority.
- **F2:** evaluation is a periodic query, so cost is (rules / interval) queries per second: batch rules that read the same series into one query, evaluate on the store's own recording rules where possible, and cache the last N minutes of each series so "for 5 minutes" is an in-memory window check instead of 5 minutes of re-query. Shard rules by consistent hash; on rebalance a new owner must start in PENDING with recomputed state rather than assume OK (or store state externally with a lease/epoch to fence the old owner). Notifications go through an outbox keyed by `(rule, incident id, state)` so a retry cannot double-page. Alert on the evaluator's own heartbeat with an independent, simpler system (a dead-man's switch rule that must always fire).
- **H1:** the follow-up is the rule behind each result: JLS 15.25 (conditional operator typing), 5.1.7 (boxing, `Integer` cache), 15.12.2 (overload phases), 14.20.3 (try-with-resources, suppressed exceptions), 14.20.2 (finally and return), `AbstractList.modCount` (fail-fast views), `Object.hashCode`/`equals` contract and `Comparator` consistency with equals (`TreeSet` docs).
- **H2:** the chain avoids a central `if/else` and lets rules be added or reordered without touching the others, at the cost of control flow that is invisible in one place and links that cannot see each other's result; a `List<Rule>` with an explicit loop is easier to debug and test, and is better when every rule must run (accumulate) rather than first-match. Make links immutable once the chain is built (publish through a final field) and the chain is safely shareable. The decorator keeps cross-cutting concerns (logging, metrics, caching) out of the links; for metrics, record latency and outcome per link, not just per chain, or you cannot see which link rejects most.
