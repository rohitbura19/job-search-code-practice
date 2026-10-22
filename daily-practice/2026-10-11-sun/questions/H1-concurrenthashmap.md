> Practice: read `h1/H1Snippets.java`, type your predicted outputs into `h1/H1Predictions.java`, run `H1PredictionsTest`. The races in the question itself (items 1 to 4) are paper-only because their output is not deterministic; the snippets cover the deterministic parts.

## H) Core Java (1) + OOPS (1)

### H1. Core Java: ConcurrentHashMap and check-then-act
```java
Map<String, Integer> hits = new ConcurrentHashMap<>();
// Thread-safe?
Integer c = hits.get(key);
hits.put(key, c == null ? 1 : c + 1);
```
1. Is this correct? Why / why not?
2. Fix it in one line.
3. How is `ConcurrentHashMap` thread-safe in Java 8+ (versus Java 7 segments)? Can it hold null keys/values, and why not?
4. `size()` and iterators — what consistency do they give?
