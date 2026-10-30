# H2. Generics: PECS, wildcard capture, a type-safe heterogeneous container (20 min)

Code: `h2/Generics.java` (signatures are the exercise, do not change them) and `h2/TypedRegistry.java`. Test: `GenericsTest`.

Paper (compare with `answers/README.md`):
1. Why is `List<String>` not a `List<Object>`, but `List<String>` is a `List<? extends Object>`?
2. In `copy(List<? super T> dst, List<? extends T> src)`, what can you add to `dst` and what can you read from `src`? Why would `List<T>` for both reject `copy(numbers, integers)`?
3. Why can't you write `new T[n]`, `instanceof List<String>`, or overload `f(List<String>)` with `f(List<Integer>)`?
4. What does the `Class<T>` token in `TypedRegistry.put` buy you over `Map<String, Object>` plus a cast, and where can the `ClassCastException` still occur?
