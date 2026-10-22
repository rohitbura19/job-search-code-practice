# Answers: B1-streams-top-chargers

(Spoilers. Open only after your attempt.)

**Reference**
```java
List<String> top3 = sessions.stream()
    .collect(Collectors.groupingBy(Session::chargerId, Collectors.summingDouble(Session::kwh)))
    .entrySet().stream()
    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
    .limit(3).map(Map.Entry::getKey).toList();

Map<String, Double> avgBySite = sessions.stream()
    .collect(Collectors.groupingBy(Session::siteId, TreeMap::new, Collectors.averagingDouble(Session::kwh)));

String siteWithMostChargers = sessions.stream()
    .collect(Collectors.groupingBy(Session::siteId,
             Collectors.mapping(Session::chargerId, Collectors.toSet())))
    .entrySet().stream()
    .max(Comparator.comparingInt(e -> e.getValue().size()))
    .map(Map.Entry::getKey).orElse(null);
```
Notes: `toMap` throws `IllegalStateException` on duplicate keys unless you pass a merge function. `parallelStream` uses the common ForkJoinPool, is bad for blocking IO or order-dependent or small collections, and needs stateless, non-interfering lambdas. Full sort is O(n log n); top-K with a size-K min-heap is O(n log K).
