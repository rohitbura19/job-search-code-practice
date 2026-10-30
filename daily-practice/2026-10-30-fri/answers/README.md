# Reference solutions (open only after trying)

Only files you must write are here (given types stay in `src/main/java`). The Java files mirror `src/main/java`; to check them, copy `answers/java/*` over `src/main/java/` in a scratch copy and run `mvn test`.
The Spring solution is under `e-spring-inventory/src/` (copy over the project's `src/`). Each file's header comment has the idea and the complexity.

| Item | File | Time | Space |
|---|---|---|---|
| A1 | java/.../a1/Numbers.java | sqrt O(log x); pow O(log \|n\|); reverse O(digits) | O(1) |
| A2 | java/.../a2/Lists.java | O(n) each | O(1) |
| B1 | java/.../b1/CampusStats.java | O(entries) per method; sorted outputs O(m log m) | O(m) |
| C1 | java/.../c1/Intervals.java | merge O(n log n); insert O(n); rooms O(n log n) | O(n) |
| C2 | java/.../c2/Scheduling.java | leastInterval O(N); reorganize O(N log 26) | O(1) |
| D1 | java/.../d1/Regex.java | O(\|s\| \|p\|) | O(\|p\|) |
| E | e-spring-inventory/src/... | - | - |
| G | java/.../g/InMemoryBroker.java | publish O(1); poll O(max + partitions); commit O(members) | O(records + groups) |
| H1 | java/.../h1/H1Predictions.java | - | the reasoning is in the header |
| H2 | java/.../h2/Generics.java, TypedRegistry.java | O(n); O(1) | O(1) |

B1 paper answers: (a) `toMap` without a merge function throws `IllegalStateException: Duplicate key ...`; add the third argument `(a, b) -> ...`; the default map is a `HashMap` (unspecified order, null values throw NPE); the four-argument overload takes a supplier such as `LinkedHashMap::new`. (b) `map(Student::courses)` is a `Stream<List<Course>>`, one list per student; `flatMap(s -> s.courses().stream())` flattens it to `Stream<Course>`. (c) `Stream.toList()` returns an unmodifiable list that allows nulls; `Collectors.toList()` returns an unspecified, in practice mutable `ArrayList`, which also allows nulls; `List.copyOf` and `Collectors.toUnmodifiableList()` reject nulls. (d) `comparing(name).reversed()` reverses everything built so far, so a following `thenComparing(id)` is still ascending, while `thenComparing(...).reversed()` at the end would reverse the whole chain including the tie-breakers; `comparing(name, reverseOrder())` reverses only that key.

E paper answer: the event is sent before the transaction commits. If the commit then fails (a deadlock, a constraint), consumers have acted on a reservation that does not exist; if the JVM dies between commit and send, the event is lost. Write an outbox row in the same transaction and publish from it, or publish `AFTER_COMMIT` and accept the loss window.

H2 paper answers: (1) `List<String>` is not a `List<Object>` because lists are mutable: through a `List<Object>` you could add an `Integer` to what is really a list of strings. `List<? extends Object>` forbids adding (except null), so it is safe. (2) `dst` is a consumer: you can add `T` (or subtypes) but only read `Object`; `src` is a producer: you can read `T` but add nothing. With `List<T>` for both, `T` would have to equal both `Number` and `Integer`, so `copy(numbers, integers)` does not compile; `? super T` / `? extends T` lets `T` be inferred as `Integer`. (3) Erasure: the runtime does not know `T` or `List`'s element type, so it cannot allocate `T[]`, test `instanceof List<String>` (only `List<?>`), and both `f(List<String>)` and `f(List<Integer>)` erase to `f(List)` (name clash). (4) The token makes the compiler tie key type to value type (`put(String.class, 3)` does not compile) and gives a runtime `cast`; the unchecked cast is confined to one library line. A `ClassCastException` can still occur at `put` when the caller passes a raw `Class` (heap pollution), which is why `put` casts with `type.cast(value)` before storing.
