> Code: `src/main/java/practice/d20261026/h1/H1Predictions.java`; the snippets are in `H1Snippets.java` (read only). Test: `H1PredictionsTest`. **20 min.**

# H1. Predict the output (core Java)

Read `H1Snippets.s1()` to `s6()`. WITHOUT running anything, type the exact `String` each returns into `H1Predictions`, then run the test (a failing test shows the real value: write down what surprised you).

1. `Integer` cache vs `==`, `Long.equals(Integer)`, compile-time constant strings, `intern()`.
2. Static/instance initialisation order with inheritance and an overridable method called from a constructor (watch what triggers class initialisation).
3. A mutable `HashMap` key, `equals` without `hashCode`, `List.remove(int)` vs `remove(Object)`.
4. `char` arithmetic and compound assignment, string concatenation order, shifts, `/` and `%` on negatives.
5. Array covariance, `Arrays.asList(int[])`, shallow `clone()`, varargs binding, array `equals`.
6. Records (compact constructor), pattern-matching `switch`, unboxing `null` in a conditional.

Say out loud, for each: which JLS rule decides it.
