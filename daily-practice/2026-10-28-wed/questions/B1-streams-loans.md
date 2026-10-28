> Code: `src/main/java/practice/d20261028/b1/LoanStats.java` (`Loan` is given). Test: `LoanStatsTest`. **20 min.**

# B1. Streams and collectors on library loans

Implement the six methods of `LoanStats` using streams only (see the Javadoc on each). Focus: `mapping` + `toCollection`, `takeWhile`, `summingLong` then re-sorting entries into a `LinkedHashMap`, comparator tie-breaks, `collectingAndThen` (a collector inside a collector), `joining(sep, prefix, suffix)`.

Then answer on paper (no IDE):
(a) What does `Stream.of(1, 2, 3, 4, 1).takeWhile(x -> x < 3).toList()` return, and what would `filter` return?
(b) `Collectors.toMap(k, v)` with a duplicate key: which exception, and which overload avoids it?
(c) `Stream.iterate(1, x -> x * 2).limit(5)` vs `.takeWhile(x -> x < 20)`: result lists?
(d) Why is `groupingBy(..., counting())` result type `Map<K, Long>` and not `Map<K, Integer>`?
(e) A stream is consumed twice: which exception and message?
