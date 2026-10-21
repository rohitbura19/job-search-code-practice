> Code: `b1/EventStats.java` (tasks 1 to 5). Predict (a) to (f) on paper, then check in answers. Test: `EventStatsTest`.

### B1. Energy event statistics (`summarizingInt`, `reducing`, `nullsLast`, `iterate`) + list views, access-order maps, `IdentityHashMap`, `BitSet`, `toList` variants
```java
record Ev(String id, String site, String type, int kwh, String note) {}   // note may be null
```
Given `List<Ev> evs` (6 events):

| id | site | type | kwh | note |
|---|---|---|---|---|
| e1 | siteA | CHARGE | 12 | ok |
| e2 | siteB | CHARGE | 8 | null |
| e3 | siteA | EXPORT | 5 | grid |
| e4 | siteC | CHARGE | 20 | null |
| e5 | siteB | EXPORT | 3 | peak |
| e6 | siteA | CHARGE | 7 | ok |

1. Per site (sorted), count, min, max, sum and average of `kwh` in ONE collector. What do you get for an EMPTY stream (`min`, `max`)?
2. Per site (sorted), the average as a `Double`; per type (sorted), the total with `reducing` (and say why this is the same as `summingInt` here).
3. Sort the events by note (alphabetical, events with NO note last), then by `kwh` descending; print the ids. What happens without the null handling?
4. The powers of 3 below 200 with the three-argument `Stream.iterate`; how is it different from `iterate(1, n -> n * 3).limit(5)`?
5. Flatten `List.of(List.of(1,2), List.of(), List.of(3))` and sum it.

Then predict: (a) `Integer[] arr = {1,2,3}; List<Integer> l = Arrays.asList(arr);` then `l.set(0, 99)`, `l.add(4)`, `l.remove(0)`, `arr[1] = 55`: what happens to `l` and `arr`? What is `Arrays.asList(new int[]{1,2,3}).size()`? (b) `parent = new ArrayList<>(List.of(1,2,3,4,5)); sub = parent.subList(1,4);` then `sub.set(0, 20)`, then `sub.clear()`; and separately `s2 = p2.subList(1,3); p2.add(6); s2.size()`; and `new ArrayList<>(p3.subList(0,2))` followed by `p3.add(6)`. (c) A `LinkedHashMap<>(16, 0.75f, true)` with `removeEldestEntry` returning `size() > 3`: put a, b, c; `get(a)`; put d: the key order? Then `getOrDefault("c", 0)`: the key order? Then a for-each loop that calls `get` on each key. (d) `new String("k")` twice as keys in a `HashMap` versus an `IdentityHashMap`; and `IdentityHashMap<Integer,String>` with `127, 127, 1000, 1000` as keys. (e) A `BitSet` with bits 1, 5, 64: `toString`, `cardinality`, `length`, `size`, `nextSetBit(2)`, `get(1000)`, `set(-1)`; and `and` / `or` with `{5, 7}`. (f) `Stream.of("a", null).toList()`, `.collect(toUnmodifiableList())`, `Stream.of("a").toList().add("b")`, and `Stream.of("a").collect(toList()).add("b")`.

Interviewer follow-up: why does `IntSummaryStatistics` report `min = Integer.MAX_VALUE` when empty, and what do you do about it? Why is `Arrays.asList` "fixed-size but writable" and what is the backing array doing? What is a `subList` view and why is it a classic memory and correctness trap (the parent list pins memory; a structural change of the parent breaks the view)? Why does `get()` count as a structural modification in an access-order map, and how do you build an LRU cache from `LinkedHashMap` (and why would you still not share it between threads without a lock)? When is `IdentityHashMap` the right tool (graph deep copy: the 20 Oct Clone Graph; serialisation cycles)? When would you pick a `BitSet` (dense small-integer sets, flags, sieve) over a `HashSet<Integer>`? Why does `Stream.toList()` allow nulls but `List.of` / `toUnmodifiableList` do not?
