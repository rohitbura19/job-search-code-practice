# Sun 25 Oct 2026 - practice

One focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the recurrence or invariant in one sentence each; mark "needed hints" in the score card.

## Order and time boxes (about 3 h)

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 intersection of two arrays II (`questions/A1-intersection.md`) | 15 | a1/Intersect.java | IntersectTest |
| 2 | A2 min stack | 15 | a2/MinStack.java | MinStackTest |
| 3 | C1 product except self + subarray sum = k | 25 | c1/Prefix.java | PrefixTest |
| 4 | C2 accounts merge (union-find) | 25 | c2/Accounts.java | AccountsTest |
| 5 | D1 median of two sorted arrays | 25 | d1/Median.java | MedianTest |
| 6 | E Spring Boot: atomic redeem + clock + validation + ProblemDetail (`e-spring-coupons/`) | 35 | that project | CouponRepositoryTest, CouponControllerTest, CouponServiceTest, CouponPropertiesTest |
| 7 | B1 streams and collectors on bookings | 20 | b1/BookingStats.java | BookingStatsTest |
| 8 | H1 predict the output | 20 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 Decorator pattern for pricing (OOP) | 30 | h2/*Pricing.java | PricingTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review) | 30 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1 (sketch 10, code 25) | 35 | my-designs/G-classes-sketch.md, then g/ | DefaultUrlShortenerTest |

Core set if short on time: 4, 5, 6, 8 and F1 out loud (about 1.5 h). Code paths are under `src/main/java/practice/d20261025/`. Run one test class: `mvn test -Dtest=MedianTest`. Reference solutions: `answers/` (the Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities).

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

- **A1 / A2:** for A1 with data that does not fit in memory: external sort both sides and merge, or hash-partition both into buckets that do fit and intersect bucket by bucket. A radix sort beats comparison sort on ints. For A2 the two-array version trades 2x memory for simplicity; the single-array `x - min` encoding halves it but needs `long` to be safe at the extremes. A follow-up is a `MaxStack`/`popMax`, or a min that supports arbitrary deletion (a lazy-deletion heap or a balanced multiset).
- **B1:** seven passes over the list could be one pass with a custom collector or a loop updating several maps; keep streams for clarity and loop when profiling says so. `Collectors.toMap` throws on a duplicate key unless you give a merge function; parallel streams need associative, non-interfering collectors (do not use the `StringBuilder` trick). Money as `long` paise avoids floating point entirely.
- **C1:** both are O(n); (b) is the template for "count subarrays with property X" with a prefix-state map (sum divisible by k, equal 0s and 1s, XOR equals k). The count itself can exceed `int` (up to n(n+1)/2): return `long` in a real API. For (a), mention the zeros-count shortcut (two zeros -> all zero; one zero -> only that index is non-zero) as an alternative that also avoids overflow surprises.
- **C2:** O(E alpha(A)) with path compression and union by size; the iterative `find` avoids stack overflow on adversarial chains. In a streaming setting keep the union-find incremental (new accounts arrive, links never split); for deletions you need offline dynamic connectivity or recomputation. Sorting the output is the real cost, O(E log E); skip it if the caller does not need an order.
- **D1:** O(log min(n, m)); the same partition idea gives the k-th element of two sorted arrays, and with k sorted arrays a heap or a binary search on the VALUE (count of elements <= x) is simpler. For a stream of numbers use two heaps (O(log n) per add, O(1) median), or a Fenwick tree over value ranges when values are bounded.
- **E:** the conditional `UPDATE` is the lock; it needs no `@Version` and no retry. Under heavy contention on one hot coupon every redeem serialises on that row: shard the counter into N sub-rows (redeem a random shard, fall back to others) or reserve slots in Redis with `DECR` and reconcile. Make the redeem idempotent with a client-supplied redemption id (unique constraint) so a retry after a timeout does not burn a second slot. `LocalDate.now(clock)` uses the server zone of the `Clock`; for a coupon valid "through the end of the day" decide which time zone owns the day and store it.
- **F1 / G:** redirects are read-heavy and cacheable: a CDN/edge cache plus an in-process LRU for hot codes, `302` when you need every click (analytics, expiry changes) and `301` when you accept browser caching. 62^7 = 3.5 trillion codes, enough for 100M links/month for decades; a counter shared by many writers needs block allocation (each node takes 10,000 ids from a coordinator) and the Base62 output is guessable, so use a bijective scramble (Feistel/xor-multiply) of the id when links must not be enumerable. Clicks: write to a log (Kafka), aggregate in batches, never one row update per click; per-link counters are eventually consistent. Abuse: scan target URLs, rate-limit creation, allow takedown by code.
- **F2:** presence is soft state with a TTL: a heartbeat refreshes a key (`SET user:42 online EX 60`), expiry IS the offline event, and "last seen" is written to durable storage only on a clean disconnect or lazily on TTL expiry. Fan-out is the danger: send changes only to users who have the contact on screen (subscribe/unsubscribe as the list scrolls), coalesce flaps with a debounce (30 s), and for celebrity accounts switch to pull with a short cache. Multi-device: online if any device is; keep one key per device.
- **H1:** the follow-up is the rule behind each result: JLS 15.25 (conditional typing), list views and `modCount`, JLS 14.20.3 (try-with-resources, suppressed), JLS 15.12.2 (three-phase overload resolution), JLS 5.1.3 / 15.4 (narrowing, IEEE 754), stream laziness and the one-shot rule.
- **H2:** every decorator keeps the `Pricing` contract (non-negative, deterministic), so wrapping order is the only design decision; make that order explicit in a builder or a small configuration object instead of nested constructors scattered in the code. The caching decorator belongs OUTSIDE the cheap decorators and INSIDE nothing mutable; with a bounded cache (Caffeine, size + expiry) it becomes safe in production. Decorator vs chain of responsibility: a chain lets a link stop the flow, a decorator always delegates (or deliberately does not).
