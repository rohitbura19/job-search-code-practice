> Code: `src/main/java/practice/d20261023/b1/TripStats.java` (+ `Trip`, `FareRange`). Test: `TripStatsTest`.

# B1. Streams and collectors on ride trips (20 min)

Implement the seven methods of `TripStats` (full contracts are in the Javadoc): `revenueByCity`, `avgKmByDriver`, `driversByCity`, `topDriverByRevenue`, `cancelledSplit`, `cityReport`, `fareRange`.

Goals: `groupingBy` with a `TreeMap` factory and a downstream collector (`summingLong`, `averagingInt`, `mapping` + `collectingAndThen`, `counting`); `partitioningBy` (both keys always present); a sort on `Map.Entry` with a comparator chain; `Collectors.teeing` for min and max in one pass; `joining`.

Say out loud: why money is `long` cents; why `toMap` needs a merge function; what `groupingBy` returns for an empty stream (empty map) versus `partitioningBy`; when you would drop the stream for a loop.
