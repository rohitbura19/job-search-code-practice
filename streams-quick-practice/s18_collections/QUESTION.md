<!-- From daily-practice/2026-11-03-tue -->
# B1. Java collections: the Map/List default methods (15 min)


Implement with `merge`, `computeIfAbsent`, `removeIf` and `Map.Entry` comparators (streams allowed where natural):

1. `wordCounts(List<String>)` with `Map.merge`.
2. `byInitial(List<String>)`: `SortedMap<Character, List<String>>` keyed by the lower-cased first letter.
3. `removeShort(List<String>, minLen)`: in place via `removeIf`; returns the removed count.
4. `mergeAll(List<Map<String,Integer>>)`: key-wise sum; totals of 0 disappear (a `merge` function returning null removes the key).
5. `invert(Map<String,Integer>)`: count -> sorted words, keys ascending.
6. `topK(Map<String,Integer>, k)`: count desc, then alphabetical.

Paper: what does `merge` do when the remapping function returns null? Why does `ConcurrentHashMap.merge` give you an atomic counter and `get` + `put` does not? Why does removing inside a for-each loop throw?
