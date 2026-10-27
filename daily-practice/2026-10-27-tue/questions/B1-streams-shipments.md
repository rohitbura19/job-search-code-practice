> Code: `src/main/java/practice/d20261027/b1/ShipmentStats.java` (`Shipment` is given). Test: `ShipmentStatsTest`. **20 min.**

# B1. Streams and collectors on shipments

Implement the 8 methods with streams and collectors (read the Javadoc of each for tie-breaks and sorting):
`totalCostByCarrier`, `carriersByWarehouse` (sorted map of sorted sets), `heaviestIdByWarehouse`, `delayedSplit` (`partitioningBy`, both keys always), `tagsCsv` (`flatMap`), `weightStatsByMonth` (`summarizingInt` into a sorted map), `topCarriersByCost`, `busiestDay`.

Say out loud: `groupingBy` with a map factory vs sorting afterwards; `toMap` without a merge function on duplicate keys; why cost is `long`; `maxBy` returns an `Optional` per group - why is `orElseThrow` safe inside `groupingBy`?
