# Answers: B1-order-analytics-lru

(Spoilers. Open only after your attempt.)

**Reference**
```java
List<String> top3 = lines.stream()
    .collect(Collectors.groupingBy(OrderLine::customerId, Collectors.summingDouble(l -> l.qty() * l.price())))
    .entrySet().stream()
    .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder())
            .thenComparing(Map.Entry.comparingByKey()))
    .limit(3).map(Map.Entry::getKey).toList();

Map<String, String> skusByCity = lines.stream().collect(Collectors.groupingBy(
    OrderLine::city, TreeMap::new,
    Collectors.collectingAndThen(
        Collectors.mapping(OrderLine::sku, Collectors.toCollection(TreeSet::new)),
        s -> String.join(",", s))));

Map<String, Long> countBySku = lines.stream()
    .collect(Collectors.groupingBy(OrderLine::sku, Collectors.counting()))
    .entrySet().stream()
    .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

String firstBig = lines.stream().filter(l -> l.qty() > 10).findFirst()
    .map(OrderLine::orderId).orElse("NONE");

// LRU in a few lines
class Lru<K, V> extends LinkedHashMap<K, V> {
    private final int cap;
    Lru(int cap) { super(16, 0.75f, true); this.cap = cap; }          // true = access order
    @Override protected boolean removeEldestEntry(Map.Entry<K, V> e) { return size() > cap; }
}
```
Notes: `toMap` into a plain `HashMap` loses the sorted order, so pass `LinkedHashMap::new`. The LRU above is NOT thread-safe (even `get` mutates order); wrap with `Collections.synchronizedMap` for light use, or use Caffeine for real workloads. `Comparable` = natural order defined by the class itself (one), `Comparator` = external, many. `ArrayList` wins almost always (cache locality); `LinkedList` only for heavy head/tail deque operations, and then `ArrayDeque` is better. `CopyOnWriteArrayList`: read-mostly lists such as listeners; every write copies the array. `List.of` is truly immutable and rejects nulls; `unmodifiableList` is just a read-only view over a list someone else can still change. `Optional` is for return types; as a field or parameter it adds an allocation and a third state (null Optional).
