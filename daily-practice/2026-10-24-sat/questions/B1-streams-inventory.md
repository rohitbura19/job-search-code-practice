> Code: `src/main/java/practice/d20261024/b1/` (`Stock` is given; implement `InventoryStats`). Test: `InventoryStatsTest`.

# B1. Collectors and sorted maps on warehouse stock (20 min)

`Stock(sku, warehouse, qty, expiry)` lines; one sku appears on many lines. Implement the six methods of `InventoryStats` (rules in the Javadoc): `toMap` with a merge function and a `TreeMap` supplier, `groupingBy` with `mapping` into a `TreeSet` finished with `collectingAndThen`, `min` with a two-key `Comparator`, a `NavigableMap` range query with `subMap`, `toMap` with `BinaryOperator.maxBy`.

Say out loud: what `Collectors.toMap(k, v)` does on a duplicate key and which overload fixes it; why `groupingBy` returns a `HashMap` unless you pass a map factory; what `Stream.toList()` returns vs `Collectors.toList()` (mutability, nulls); why `subMap` is a live view and what happens if you modify it; `comparing(...).reversed()` after `thenComparing` reverses the WHOLE comparator.
