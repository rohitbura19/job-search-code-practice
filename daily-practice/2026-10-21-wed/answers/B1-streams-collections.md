# Answers: B1-streams-collections

(Spoilers. Open only after your attempt.)

**Reference** (all output below verified by running the code on JDK 21)
```java
// 1
TreeMap<String, IntSummaryStatistics> st = evs.stream().collect(groupingBy(Ev::site, TreeMap::new, summarizingInt(Ev::kwh)));
// siteA: IntSummaryStatistics{count=3, sum=24, min=5, average=8.000000, max=12}
// siteB: count=2, sum=11, min=3, average=5.500000, max=8      siteC: count=1, sum=20, min=20, average=20.000000, max=20
new IntSummaryStatistics();   // IntSummaryStatistics{count=0, sum=0, min=2147483647, average=0.000000, max=-2147483648}
// 2
TreeMap<String, Double> avg = evs.stream().collect(groupingBy(Ev::site, TreeMap::new, averagingInt(Ev::kwh)));    // {siteA=8.0, siteB=5.5, siteC=20.0}
TreeMap<String, Integer> byType = evs.stream().collect(groupingBy(Ev::type, TreeMap::new, reducing(0, Ev::kwh, Integer::sum)));   // {CHARGE=47, EXPORT=8}
// 3
evs.stream().sorted(Comparator.comparing(Ev::note, Comparator.nullsLast(Comparator.naturalOrder()))
                              .thenComparing(Ev::kwh, Comparator.reverseOrder())).map(Ev::id).toList();           // [e3, e1, e6, e5, e4, e2]
// without nullsLast: Comparator.comparing(Ev::note) throws NullPointerException on the first null note
// 4
Stream.iterate(1, n -> n < 200, n -> n * 3).toList();      // [1, 3, 9, 27, 81]   (Java 9: seed, hasNext, next, like a for loop)
Stream.iterate(1, n -> n * 3).limit(5).toList();           // [1, 3, 9, 27, 81]   (infinite stream, cut by limit; needs a COUNT instead of a condition)
// 5
Stream.of(List.of(1,2), List.<Integer>of(), List.of(3)).flatMap(List::stream).mapToInt(Integer::intValue).sum();   // 6
```
- Part 1: `summarizingInt` computes five statistics in one pass. An EMPTY stream gives `min = Integer.MAX_VALUE` and `max = Integer.MIN_VALUE` (the identities of `min` and `max`), so an empty group shows nonsense numbers: check `getCount() > 0` before you display them. Note that `sum` is a `long` (no `int` overflow) while the `average` is a `double`. Part 2: `averagingInt` returns a `Double` (0.0 for an empty group); `reducing(identity, mapper, op)` needs a NEUTRAL identity (`0` for a sum), exactly as `reduce` does (the 18 Oct parallel trap), and `summingInt` is the readable, fused form. Part 3: `thenComparing(keyExtractor, keyComparator)` takes both; `nullsLast` wraps the comparator for the key only; `nullsFirst` is the mirror. Part 4: the three-argument `iterate` ends when `hasNext` is false, with no `limit`; the two-argument one is infinite.
- (a) `[99, 2, 3]` in `arr` after `set(0, 99)`: the list is a WRITE-THROUGH view of the array. `l.add(4)` and `l.remove(0)` throw `UnsupportedOperationException` (the size is fixed). After `arr[1] = 55` the list shows `[99, 55, 3]`. `Arrays.asList(new int[]{1,2,3}).size()` is **1** (a primitive array is ONE object, so you get a `List<int[]>`), while `Arrays.asList(1,2,3).size()` is 3. Use `new ArrayList<>(Arrays.asList(...))` for a real mutable list, and `List.of` / `List.copyOf` for an immutable one.
- (b) After `sub.set(0, 20)` the parent is `[1, 20, 3, 4, 5]` and `sub` is `[20, 3, 4]`; after `sub.clear()` the parent is `[1, 5]` and `sub` is `[]` (the view writes through, so `parent.subList(a, b).clear()` is the idiomatic way to delete a range). After `p2.add(6)` a call to `s2.size()` throws `ConcurrentModificationException` (the view checks the parent's `modCount`). `new ArrayList<>(p3.subList(0, 2))` is a COPY (`[1, 2]` and unaffected by later changes); keeping a raw `subList` of a huge list keeps the whole parent alive.
- (c) `[c, a, d]` (put a, b, c; `get(a)` moves a to the end, giving b, c, a; putting d makes the size 4, so the eldest, b, is removed). After `getOrDefault("c", 0)` the order is `[a, d, c]` (it counts as an access too). Calling `get` inside a for-each over the key set throws `ConcurrentModificationException` (verified), because in access-order mode every `get` is a structural change. An insertion-order map keeps `{a=9, b=2}` after `put(a,1) put(b,2) put(a,9)`: re-putting a key does not move it. This is the minimal LRU cache (a `LinkedHashMap` subclass with `accessOrder = true` and `removeEldestEntry`), good for a small single-threaded cache; wrap it in `Collections.synchronizedMap` or use a library cache for concurrent use.
- (d) `HashMap` size 1 and `IdentityHashMap` size 2: it compares keys with `==`, so two equal-but-distinct `String` objects are different keys. With `Integer` keys: `127, 127` is ONE entry (the Integer cache from the 19 Oct sheet returns the same object) and `1000, 1000` is TWO entries, so the total size is 3. Use it where identity is the point: visited sets for object graphs, deep-copy maps, serialisation cycle detection.
- (e) `{1, 5, 64} card=3 length=65 size=128 next(2)=5 get(1000)=false`: `length` is the highest set bit plus one, `size` is the number of bits of storage currently allocated (two `long`s), `get` of an unset or out-of-range positive index is just `false`. `set(-1)` throws `IndexOutOfBoundsException`. `and` with `{5, 7}` gives `{5}`, `or` gives `{1, 5, 7, 64}`, `intersects` is `true`. One bit per flag: 10^6 flags cost about 125 KB, versus tens of MB for a `HashSet<Integer>`.
- (f) `Stream.of("a", null).toList()` prints `[a, null]` (nulls allowed, result unmodifiable). `collect(toUnmodifiableList())` throws `NullPointerException`. `Stream.of("a").toList().add("b")` throws `UnsupportedOperationException`. `Stream.of("a").collect(toList()).add("b")` works and gives `[a, b]` (today it is an `ArrayList`, but the contract does not promise it). `List.copyOf` of a list that contains null throws `NullPointerException`. So `toList()` is "unmodifiable, nulls allowed", `Collectors.toList()` is "mutable, unspecified type", and `List.of`/`toUnmodifiableList`/`copyOf` are "immutable, null-hostile".
