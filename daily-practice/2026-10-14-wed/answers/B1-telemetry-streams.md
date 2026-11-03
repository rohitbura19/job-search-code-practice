# Answers: B1-telemetry-streams

(Spoilers. Open only after your attempt.)

**Reference** (verified against sample data; outputs shown)
```java
Map<String, Reading> latest = readings.stream().collect(toMap(Reading::deviceId, Function.identity(),
    BinaryOperator.maxBy(Comparator.comparing(Reading::ts)), TreeMap::new));
// {d1=230.0, d2=95.0, d3=50.0}

Map<String, Map<String, Double>> avg = readings.stream().collect(groupingBy(Reading::metric, TreeMap::new,
    groupingBy(Reading::deviceId, TreeMap::new, averagingDouble(Reading::value))));
// {temp={d1=77.5, d2=92.5, d3=50.0}, volt={d1=230.0}}

Map<String, Set<String>> tags = readings.stream().collect(groupingBy(Reading::metric, TreeMap::new,
    flatMapping(r -> r.tags().stream(), toCollection(TreeSet::new))));
// {temp=[garage, indoor, outdoor], volt=[grid]}

List<String> hot = readings.stream().collect(groupingBy(Reading::deviceId, TreeMap::new,
        filtering(r -> r.metric().equals("temp") && r.value() > 80, counting())))
    .entrySet().stream().filter(e -> e.getValue() >= 2).map(Map.Entry::getKey).toList();
// [d2]

Set<String> active = readings.stream().filter(r -> r.ts().isAfter(cutoff)).map(Reading::deviceId).collect(toSet());
List<String> silent = known.stream().filter(id -> !active.contains(id)).toList();     // [d2, d3, d4] with the sample cutoff
```
Notes: `filtering` keeps the group key even when nothing matches (value 0), a `filter` before `groupingBy` drops that key entirely, which matters for "devices with zero alerts". A device that never reported at all (d4) only appears in the silent list because we start from the known-ids set, not from the readings. Top K: size-K min-heap (`PriorityQueue`) gives O(n log K) and O(K) memory. Reading at or before `t`: `TreeMap<Instant, Reading>.floorEntry(t)`, O(log n); for expiry use `headMap(cutoff).clear()`. `Arrays.asList` is a fixed-size list backed by the array (set allowed, add/remove throw `UnsupportedOperationException`). `subList` is a view: structural changes to the parent afterwards make any later use of the view throw `ConcurrentModificationException`. `EnumMap` for enum keys is faster and smaller than `HashMap`.
