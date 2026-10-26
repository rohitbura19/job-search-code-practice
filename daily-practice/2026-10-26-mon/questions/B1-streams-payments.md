> Code: `src/main/java/practice/d20261026/b1/PaymentStats.java` (`Payment`, `Status`, `AmountRange` are given). Test: `PaymentStatsTest`. **20 min.**

# B1. Streams and collectors on payments

Implement the eight methods (the Javadoc on each states the exact contract). Use streams and collectors; each should be one pipeline.

1. `settledTotalByMerchant`: `groupingBy` into a sorted map with a downstream summing collector.
2. `countByStatus`: every enum constant is a key, even with zero payments (what does plain `groupingBy` do about that?).
3. `topMerchant`: maximum with a deterministic tie-break.
4. `largestSettledPerMethod`: `toMap` with a merge function and a `TreeMap` supplier; the result must not depend on input order.
5. `averageSettledPerDay`: `averagingLong` downstream, sorted by date.
6. `settledRange`: min and max in ONE pass (`Collectors.teeing`).
7. `methodsByMerchant`: `mapping` into a `TreeSet`.
8. `topIds`: comparator chain, `limit`, `joining`.

Predict on paper before running: what happens if (4) has no merge function and two payments share a method? What does `Collectors.averagingLong` return for an empty group (can a group be empty)? Which of these would break with `parallelStream()` if you used a shared `ArrayList`?
