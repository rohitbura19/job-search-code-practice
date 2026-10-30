# Fri 30 Oct 2026 - practice

One focused block of about 3.5 to 4 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the invariant or recurrence in one sentence each; mark "needed hints" in the score card.

## Order and time boxes

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 integer sqrt, fast power, reverse digits (overflow care) | 10 | a1/Numbers.java | NumbersTest |
| 2 | A2 linked list: reverse, reverse a range, O(1)-space palindrome | 10 | a2/Lists.java | ListsTest |
| 3 | C1 intervals: merge, insert, meeting rooms | 20 | c1/Intervals.java | IntervalsTest |
| 4 | C2 greedy + heap: task cooldown, reorganize string | 20 | c2/Scheduling.java | SchedulingTest |
| 5 | D1 regular expression matching (`.` and `*`) | 20 | d1/Regex.java | RegexTest |
| 6 | E Spring Boot: atomic conditional UPDATE + Kafka producer/listener + `@ConfigurationProperties` + validation + ProblemDetail (`e-spring-inventory/`) | 30 | that project | StockRepositoryTest, StockControllerTest, StockServiceTest, KafkaPartsTest, StockPropsTest |
| 7 | B1 streams: flatMap, toMap with merge, Comparator chains, IntStream | 15 | b1/CampusStats.java | CampusStatsTest |
| 8 | H1 predict the output | 15 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 generics: PECS, wildcard capture, type-safe heterogeneous container (OOP) | 20 | h2/*.java | GenericsTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review each) | 34 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1: in-memory partitioned log with consumer groups (sketch 8, code 22) | 30 | my-designs/G-classes-sketch.md, then g/ | InMemoryBrokerTest |

Core set if short on time: 3, 5, 6, 11 and F1 out loud (about 1.5 h). Code paths are under `src/main/java/practice/d20261030/`. Run one test class: `mvn test -Dtest=IntervalsTest`. Reference solutions: `answers/` (Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities and the paper answers).

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

- **A1 / A2:** `sqrt` by binary search is O(log x); Newton's iteration converges in about 5 steps and `Math.sqrt` plus a +-1 correction is fastest. Follow-up: why does `mid * mid` overflow past 46340, and how does `mid <= x / mid` avoid it? `pow` by squaring has rounding error that grows with the exponent. The palindrome check mutates the list while it runs, so it is unsafe for concurrent readers: copy to an array or restore under a lock. `reverseBetween` is the building block of "reverse in k-groups".
- **B1:** `toMap` with a merge function and a `LinkedHashMap` supplier keeps input order. `flatMap` allocates a stream per student: on a hot path a plain loop or `mapMulti` is cheaper. Sorting counted entries is O(m log m) over distinct keys; for top-k only, a bounded heap is O(m log k). `distinct()` goes inside the `flatMap` (per student), not after it. Use `comparingInt` to avoid boxing.
- **C1:** `merge` is O(n log n) for the sort, O(n) if the input is already sorted. Repeated inserts belong in a `TreeMap<start, end>` (O(log n) each): that is the calendar-booking design. Rooms by two sorted arrays is O(n log n); a min-heap of end times also tells you which room. For 10^8 intervals: external sort, or split at gaps and merge partitions in parallel.
- **C2:** `leastInterval` is a counting argument O(N); the heap simulation is what you need when tasks have different cooldowns. `reorganize` can also place the most frequent letter at even indices, then the rest, with no heap. The condition `maxCount > (len + 1) / 2` is the proof: check it first and fail fast.
- **D1:** O(mn) time, O(n) space with two rows. An NFA simulated with a state set is O(m * states) with no backtracking (RE2); backtracking engines, `java.util.regex` included, go exponential on nested quantifiers: a ReDoS risk if user patterns reach `Pattern.compile`. Cap length, add timeouts, or use an automaton engine for untrusted patterns. A DFA costs up to O(2^n) to build but O(len(s)) per match: worth it when one pattern meets millions of strings.
- **E:** the conditional `UPDATE ... where onHand - reserved >= :qty` is a compare-and-set in one statement; `@Version` plus retry is the alternative when the check is too complex for SQL. The service publishes to Kafka **inside** the transaction: a failed commit leaves consumers acting on a reservation that does not exist, and a crash after commit loses the event. Fix with a transactional outbox (row in the same transaction, relay by poller or CDC), or `AFTER_COMMIT` if losing an event is acceptable. Consumers must be idempotent: store the event id or make the operation absolute. A permanently failing record needs `DefaultErrorHandler` with bounded back-off and a dead-letter topic; skipping poison pills silently hides them, so count them. Reservations need a TTL job for abandoned carts. A hot SKU is one row lock: shard the counter into N rows and sum on read.
- **F1 / G:** a partition is an append-only file: sequential I/O plus page cache is why a log beats a queue per consumer. Durability is `acks=all`, `min.insync.replicas=2` and an idempotent producer. Ordering is per partition only, so the key choice is the design; a hot key is a hot partition. Rebalances pause the group: use cooperative-sticky assignment and static membership. `InMemoryBroker` uses one monitor (simple, correct); per-partition locks would let publishers to different partitions run in parallel, at the price of a lock-ordering rule for the round-robin counter and for rebalance. `poll` copies records because handing out a live slice of the log would race with appends. Exactly-once means idempotent producer plus an offset commit in the same transaction as the output; otherwise design for at-least-once with idempotent handlers. Alert on lag growth rate, not absolute lag.
- **F2:** treat usage events as an immutable ledger keyed by `(customer, meter, event_id)` and derive aggregates from it, so any bug is fixed by recomputing; an invoice is a function of a closed ledger window and a price-book version. Dedupe with an idempotency key. Late events: aggregate by event time with a watermark; after an invoice closes, late usage becomes an adjustment line on the next one. Quota on the request path uses a local token bucket per gateway refilled from a shared counter in blocks (bounded over-use). Reconcile gateway counts with the aggregate hourly; a gap triggers replay.
- **H1:** rules behind each result: JLS 12.4/12.5 (initialisation order), 15.26.2 (compound assignment casts), 15.17 (`/`, `%`), 15.19 (shift masking), 5.1.3 (narrowing), 15.28 (constant strings), the `hashCode` contract, `PriorityQueue` javadoc (iteration is not sorted), 14.20.2 (finally), `java.util.stream` package doc (laziness).
- **H2:** PECS is variance: `? extends T` is covariant and read-only, `? super T` contravariant and write-only; `Comparable<? super T>` applies the same rule to a bound. The `Class<T>` key confines the unchecked cast to one place and makes failures happen at `put`; exact-class lookup keeps semantics simple (subtype lookup needs a precedence rule). It is the seed of Guice keys and `getBean(Class)`; generic types need a super type token (`ParameterizedTypeReference`). `reverseInPlace(List<?>)` needs a capture helper because only a generic method can name the captured type.
