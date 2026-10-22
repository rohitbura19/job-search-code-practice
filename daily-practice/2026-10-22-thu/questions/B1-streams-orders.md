> Code: `b1/OrderStats.java` (+ given `Order`, `MinMax`). Test: `OrderStatsTest`. Then predict (a) to (e) on paper.

# B1. Order statistics with collectors (`groupingBy` + downstream, `partitioningBy`, `teeing`) + collection traps

`record Order(String id, String customer, String category, int qty, long unitCents)`; total of an order = `qty * unitCents`.

1. `revenueByCustomer`: revenue per customer, keys sorted.
2. `partitionByTotal(orders, threshold)`: ids in input order, split by `total >= threshold`; both keys always present, even for an empty list.
3. `categoriesByCustomer`: distinct categories per customer, customers and categories sorted.
4. `topCustomer`: highest revenue, a tie goes to the alphabetically first name.
5. `minMaxTotal`: smallest and largest total in ONE pass with one collector (`teeing`), `Optional.empty()` for no orders. Overflow: an order of 100,000 x 100,000 cents must work.
6. `topIds(orders, n)`: total descending, then id ascending.

Predict: (a) `Collectors.toMap(k, v)` with a duplicate key: what exception, and what does the 3-argument form fix? Which map type does `toMap` return and how do you get a sorted one? (b) `List.of(1,2,3).stream().map(x -> x * 2).toList().add(8)` vs `collect(Collectors.toList()).add(8)`. (c) `Stream.of(1,2,3,4).peek(print).anyMatch(x -> x == 2)`: what is printed? (d) `Arrays.asList(1,2,3).remove(0)`. (e) `new HashSet<>(List.of(1,2,3)).stream().parallel().reduce(0, (a, b) -> a - b)`: is the result reliable? Why is the reduce operator required to be associative?

Interviewer follow-up: why is `groupingBy` with a boolean not a `partitioningBy`; what does the default `HashMap` give up compared with `TreeMap::new`; when is `flatMapping` / `filtering` the right downstream; why not a parallel stream for 6 elements (and when is it worth it: splittable source, stateless operators, CPU-bound, big n).
