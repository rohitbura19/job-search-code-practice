> Code: `src/main/java/practice/d20261027/h1/H1Predictions.java`; the snippets are in `H1Snippets.java` (read only). Test: `H1PredictionsTest`. **20 min.**

# H1. Predict the output (core Java)

Read `H1Snippets.s1()` to `s6()`. WITHOUT running anything, type the exact `String` each returns into `H1Predictions`, then run the test (a failing test shows the real value: write down what surprised you).

1. `return` vs `finally`: values captured before `finally`, a `return` inside `finally`, `continue` inside `try`.
2. Numeric edges: `Math.abs(MIN_VALUE)`, overflow, narrowing `double -> int`, `NaN`, `-0.0`, operator precedence.
3. Collections: `List.of` vs `Arrays.asList`, `subList` views, removing inside a for-each, a `TreeSet` with a comparator.
4. Overload resolution phases (widening vs boxing vs varargs) and static method hiding vs overriding.
5. try-with-resources: closing order, primary vs suppressed exceptions.
6. The conditional operator: numeric promotion, constant `int` vs `char`, unboxing a `null`.

Say out loud, for each: which JLS rule decides it.
