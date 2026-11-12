<!-- From daily-practice/2026-11-02-mon -->
# B1. Streams and collections: staff statistics + an LRU map (15 min)


Implement the seven methods (Javadoc in the file defines ordering and tie-breaks):
`avgSalaryByDept` (TreeMap keys), `highestPaid` (Optional, name tie-break), `namesBySalaryDesc` (grouping with a downstream `mapping`), `topPerDept` (collectingAndThen + minBy, then a second ordering), `seniorCount` (partitioningBy keeps both keys), `wordFrequency` (flatMap + counting), `lruCache(capacity)` (LinkedHashMap with access order and `removeEldestEntry`).

Then answer on paper: (a) why does `Collectors.toMap` throw on duplicate keys and how do you merge? (b) what is the difference between `groupingBy` into `HashMap` and into `TreeMap::new` for iteration order? (c) is the LRU map thread-safe, what is the cheapest fix, and what would you use for a high-contention cache?
