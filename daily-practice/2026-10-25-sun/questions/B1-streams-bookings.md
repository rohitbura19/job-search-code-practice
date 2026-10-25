> Code: `src/main/java/practice/d20261025/b1/BookingStats.java` (`Booking` is given). Test: `BookingStatsTest`.

# B1. Streams and collectors on bookings (20 min)

Seven methods, each a stream pipeline (the Javadoc has the exact contract): `revenueByCity` (sorted by value desc, `LinkedHashMap`), `topGuestPerCity` (nested grouping, tie-break), `avgNightsByMonth` (`TreeMap<YearMonth, Double>`), `splitLongStays` (`partitioningBy` + `mapping`), `countAndRevenue` (`teeing`), `guestRoll` (case-insensitive distinct, first spelling kept), `biggest` (multi-key comparator).

Say out loud: why `groupingBy` returns no key for an empty group but `partitioningBy` always has both; what `Collectors.toMap` does on a duplicate key; why `averagingInt` on an empty group never happens; what `teeing` saves over two passes; why `sorted(...)` before `collect` cannot sort a `HashMap`.
