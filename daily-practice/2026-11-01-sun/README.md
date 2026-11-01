# Sun 1 Nov 2026 - practice

One focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the invariant or recurrence in one sentence each; mark "needed hints" in the score card.

## Order and time boxes

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 happy number, summary ranges, isomorphic strings | 10 | a1/RangesAndHappy.java | RangesAndHappyTest |
| 2 | A2 valid Sudoku, plus one, longest common prefix | 10 | a2/GridAndDigits.java | GridAndDigitsTest |
| 3 | C1 Union-Find: provinces, redundant connection, longest consecutive | 20 | c1/Connectivity.java | ConnectivityTest |
| 4 | C2 backtracking: subsets II, parentheses, N-Queens | 20 | c2/Backtracking.java | BacktrackingTest |
| 5 | D1 shortest palindrome (KMP, O(n)) | 15 | d1/ShortestPalindrome.java | ShortestPalindromeTest |
| 6 | E Spring Boot: `@ElementCollection` + JPQL join + projection + `@EntityGraph` + `@Cacheable` + `@ConfigurationProperties` + ProblemDetail (`e-spring-reading-list/`) | 30 | that project | ArticleRepositoryTest, ArticleControllerTest, ArticleServiceTest, TagCountsCacheTest, ReadingPropsTest |
| 7 | B1 streams: TreeMap grouping, maxBy, flatMap, dropWhile/takeWhile, iterate, mapMulti | 15 | b1/LogStats.java | LogStatsTest |
| 8 | H1 predict the output | 15 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 immutable Comparable value object (Semver) | 20 | h2/Semver.java | SemverTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review each) | 34 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1: in-memory chat core, idempotent send, gap-free seq, read markers (sketch 8, code 22) | 30 | my-designs/G-classes-sketch.md, then g/InMemoryChatService.java | InMemoryChatServiceTest |

Core set if short on time: 3, 5, 6, 11 and F1 out loud (about 1.5 h). Code is under `src/main/java/practice/d20261101/`. Run one test class: `mvn test -Dtest=ConnectivityTest` Reference solutions: `answers/` (Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities).

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

- **A1 / A2:** Floyd on the digit sequence is O(1) space; a `HashSet` is simpler but O(cycle). Known fact worth stating: every unhappy number falls into the cycle through 4. `summaryRanges` must widen to `long` before subtracting. Isomorphism needs both directions or `badc/baba` passes; for ASCII use two `int[128]` of last-seen positions. Sudoku with bitmasks is O(81) and allocation-free; the follow-up is the solver (backtracking with the most-constrained cell first). `plusOne` is the carry loop that big-number add and increment of a counter in a database row both reduce to.
- **B1:** `dropWhile`/`takeWhile` are order-dependent: on an unordered parallel stream their result is not deterministic. `mapMulti` avoids creating a throwaway `Stream` per element and is the right tool when most elements map to 0 or 1 outputs. A `TreeMap` factory costs O(log m) per insert; sort once at the end if you only need ordered output. Top-k words: a bounded min-heap is O(m log k) against a full sort O(m log m). Saturating arithmetic (or `Math.addExact` + catch) is the safe way to run a sequence to the type's limit.
- **C1:** union by size + path halving gives amortised inverse-Ackermann. DSU supports only merges: for deletions use offline reverse processing or a dynamic-connectivity structure. For the matrix form, an adjacency list makes it O(V + E); the matrix scan is O(n^2) regardless. Longest consecutive with a hash set is O(n) expected; with a sort it is O(n log n) but O(1) extra space: say which constraint you optimise. A DSU keyed by value (union x with x+1) solves the same problem online.
- **C2:** pruning is the whole game: the duplicate-skip guard keeps sibling branches distinct (the output is exact, no post-hoc `Set`), and parentheses never builds an invalid prefix. N-Queens with bitmasks is the fastest simple form; symmetry (mirror the first row) halves the work. Generating 208 012 strings is output-bound: ask whether the caller needs a lazy iterator instead of a list. Recursion depth is O(n), safe here; for deep searches convert to an explicit stack.
- **D1:** KMP gives O(n) with a separator; Z-function is the same idea; a rolling hash is O(n) expected but needs a collision story (double mod, verify the candidate). Manacher finds the longest palindromic prefix too. The sentinel char must lie outside the alphabet: say how you would handle arbitrary Unicode (use a delimiter-free two-array comparison, or compute the prefix function over `s` and then over `reverse(s)` against it).
- **E:** `@ElementCollection` rows have no identity: the whole collection can be deleted and re-inserted on update (use a `Set`, or promote to an entity when you need to update single rows or add columns). The join returns one row per matching tag, so a query for several tags needs `distinct` and a different count query. `open-in-view=false` makes lazy access outside the transaction fail fast: build DTOs in the service. `@Cacheable` stores the object reference: cache immutable records, never entities or projection proxies; with several nodes use a shared cache (Redis) or a short TTL, and note `allEntries=true` eviction is coarse. The exists-then-save duplicate check is racy: the unique index is the guard, translate `DataIntegrityViolationException` to 409. Offset paging degrades with depth: use keyset on `id`. Tag statistics become a materialised view or a counter table when the table is large.
- **F1 / G:** the unit of ordering is the conversation: one writer (a partition/shard owner) assigns `seq`, so sends to one conversation serialise while different conversations scale out. Clients sync with "give me everything after seq N", which makes delivery, reconnect and missed pushes the same code path; the push channel is only a hint. Idempotency by `(sender, clientMsgId)` turns at-least-once retries into effectively-once. Large groups: store one copy of the message and per-user cursors, never per-recipient copies; fan out the notification, not the data; read receipts for big groups are aggregated or dropped. Hot conversation = single-partition limit: batch appends, and cap group size. Dedupe maps need a window (TTL) or they grow without bound. In the LLD the per-conversation monitor is enough; striping or a single-threaded actor per conversation scales further, and a persistent log (Kafka partition per conversation key) gives durability and replay.
- **F2:** exact counts per shard are fine up to millions of distinct tags; count-min sketch + a heap of candidates caps memory but over-counts (never under-counts), so verify the top candidates against exact counts. Trending is a velocity signal: compare the current window with a baseline (z-score or ratio with a floor) instead of ranking by volume, or the same giant tags win every minute. Per-shard top-k lists merge safely only if each shard returns more than k (take 3k) or counts are exact per tag after a key-based repartition. Keep the serving layer dumb: a precomputed list in a cache, rebuilt every minute. Spam: weight by distinct users, not posts, and rate-limit per account.
- **H1:** rules behind each result: JLS 12.4/12.5 (class and instance initialisation order), 14.20.2 (finally, abrupt completion), 15.26.2 (compound assignment casts), 15.17.2/15.18 (string concatenation, left to right), 5.1.3 (narrowing of floating to int saturates), 5.1.7 + Integer cache (-128..127), 15.25 (conditional typing), array covariance (ArrayStoreException), `Arrays.asList` write-through view, hashCode in a mutated key, `String.split` trailing empties, constant folding and `String.intern`.
- **H2:** equals/hashCode/compareTo consistency decides whether `HashSet` and `TreeSet` agree; `BigDecimal` is the cautionary tale. Immutability buys thread safety, safe hash keys and caching of `hashCode`. Compare unbounded numeric strings by length then text instead of parsing. In a real library you would also add ranges (`^1.2.3`, `~1.2`) as a separate matcher type, not as flags on `Semver`; and a `record` is an option when the validation lives in a static factory.
