# B1. Streams on service logs: TreeMap grouping, maxBy, flatMap, dropWhile/takeWhile, iterate, mapMulti (15 min)

`b1/LogStats.java` with the given `LogLine(service, level, ms, msg)`. Streams and collectors only; read the Javadoc on each method for the exact tie-break and edge rules.

Paper: (a) why does `dropWhile`/`takeWhile` give "the first burst" while `filter` would not? Which of them are short-circuiting and what happens on an unordered stream? (b) `mapMulti` vs `flatMap`: when is `mapMulti` cheaper? (c) why does `groupingBy(..., TreeMap::new, counting())` return a `SortedMap` and what is the cost vs `HashMap`?
