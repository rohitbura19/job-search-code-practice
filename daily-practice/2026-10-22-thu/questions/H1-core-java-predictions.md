> Code: `h1/H1Snippets.java` (read only), `h1/H1Predictions.java` (write the expected `String`s). Test: `H1PredictionsTest` compares with the real output. **15 min. Predict on paper BEFORE running.**

# H1. Core Java: predict the output (6 snippets)

1. `finally` and `return`: a `return` of a primitive, a `return` inside `finally` while an exception flies, a `StringBuilder` returned then modified.
2. Initialisation order with `static` and instance blocks, a constructor calling an overridable method, a `final String` constant field.
3. Overload resolution: widening vs boxing vs varargs; `char`, `byte`, `Integer`, `(Object) null`, `double`.
4. `==` on `Integer` (cache), compile-time constant strings, `intern`, `new String`.
5. Numeric traps: casts, overflow before widening, integer division, `%` with negatives, `Math.abs(MIN_VALUE)`, compound assignment, `NaN`.
6. `List.remove(int)` vs `remove(Object)`; removing inside a for-each: when is `ConcurrentModificationException` thrown and when not.

Interviewer follow-up: JLS phases of overload resolution; why `hashCode`/`equals` on mutable keys break `HashMap`; why calling an overridable method from a constructor is a bug; what `-XX:AutoBoxCacheMax` changes; how `fail-fast` iterators work (`modCount`) and what `CopyOnWriteArrayList` and `removeIf` do instead.
