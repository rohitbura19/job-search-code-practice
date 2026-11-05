# H1. Core Java: predict the output (15 min, no running)

> Code: `h1/H1Predictions.java`; snippets in `H1Snippets.java` (read only). Test: `H1PredictionsTest`. **Predict on paper BEFORE running.**

Six snippets: initialisation order with an overridden method called from a constructor; `try`/`finally` return semantics with `continue` and `throw`; boxing, the Integer cache, compound assignment narrowing, ternary typing and char arithmetic; collection traps (`remove(int)` vs `remove(Object)`, `Arrays.asList` write-through, views vs copies, mutated hash keys); string interning and `split`; overload resolution phases.
Write the exact `String` each `sN()` returns. For every miss write the JLS rule in one line. (`s1` prints static-init markers, which happen once per JVM: run it in a fresh JVM, as the test does.)
