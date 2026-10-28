> Code: `src/main/java/practice/d20261028/h1/H1Predictions.java`; the snippets are in `H1Snippets.java` (read only). Test: `H1PredictionsTest`. **20 min.**

# H1. Predict the output (core Java)

Read `H1Snippets.s1()` to `s6()`. WITHOUT running anything, type the exact `String` each returns into `H1Predictions`, then run the test (a failing test shows the real value: write down what surprised you).

1. Initialization order: static blocks, field initialisers, instance blocks, constructors, and an overridable method called from a super constructor.
2. `char`/`byte`/`int` arithmetic: compound assignment casts, `i = i++ + ++i`, shift distance masking, `>>` vs `>>>`, `/` and `%` on negatives.
3. Collections: `remove(int)` vs `remove(Object)`, `Arrays.asList(int[])`, the `Integer` cache, a mutated `HashSet` key, `PriorityQueue` order, `Deque` push vs add.
4. Strings: `split` and trailing empties, compile-time constants vs runtime concatenation vs `intern`, `compareTo`, `replaceAll` takes a regex.
5. Generics and arrays: erasure, heap pollution (where does the `ClassCastException` appear?), array covariance, class names of factory lists.
6. Control flow: switch fall-through with `default` in the middle, switch expression `yield`, labeled `break`/`continue`, `&&` vs `&`, `x = x++ + 1`.

Say out loud, for each: which JLS rule decides it.
