# Answers: H1-concurrenthashmap

(Spoilers. Open only after your attempt.)

**Reference**
- Not correct: each call is atomic but the sequence is not; two threads can both read 5 and write 6 (lost update).
- Fix: `hits.merge(key, 1, Integer::sum);` (or `compute`). The function must be short and must not touch the same map.
- Java 8+: no segments. Empty bin insert uses CAS; otherwise `synchronized` on the head node of the bin only. Reads are lock-free via `volatile` node fields. Bins treeify at 8 collisions (and table >= 64). Resizing is cooperative (multiple threads help transfer).
- No null keys or values: `get()` returning null would be ambiguous (absent vs mapped to null) and there is no way to disambiguate atomically in a concurrent map.
- Iterators are weakly consistent (no `ConcurrentModificationException`, may or may not reflect concurrent updates). `size()` is an estimate under concurrent writes; use `mappingCount()` for large maps.
- Follow-up: `LongAdder` for hot counters under heavy contention (striped cells), `hits.computeIfAbsent(k, x -> new LongAdder()).increment()`.
