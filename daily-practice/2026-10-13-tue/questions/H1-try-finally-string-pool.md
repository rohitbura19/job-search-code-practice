> Practice: read `h1/H1Snippets.java`, type your predicted outputs into `h1/H1Predictions.java`, run `H1PredictionsTest`. Items 4 and 5 below are paper-only. One extra puzzle (LINE6, `hashCode` without `equals`) is in the code only.

### H1. Core Java: try/finally, String pool, try-with-resources
```java
static int f() { try { return 1; } finally { System.out.println("fin"); } }
static int g() { int x = 1; try { return x; } finally { x = 2; } }
static int h() { try { throw new RuntimeException("a"); } finally { return 3; } }

String a = "java";
String b = new String("java");
String c = b.intern();
String ja = "ja";
final String fja = "ja";
System.out.println(a == b);
System.out.println(a == c);
System.out.println(a.equals(b));
System.out.println("ja" + "va" == a);
System.out.println((ja + "va") == a);
System.out.println((fja + "va") == a);

class R implements AutoCloseable {
    final String n; R(String n) { this.n = n; System.out.println("open " + n); }
    public void close() { System.out.println("close " + n); throw new IllegalStateException("close-" + n); }
}
try (R r1 = new R("1"); R r2 = new R("2")) {
    throw new RuntimeException("body");
} catch (Exception e) {
    System.out.println(e.getMessage() + " " + Arrays.toString(e.getSuppressed()));
}
```
1. What do `f()`, `g()` and `h()` print or return, and why?
2. Predict the six boolean lines.
3. Predict the try-with-resources output including the suppressed list.
4. Why is `String` immutable? `StringBuilder` vs `StringBuffer`? What is the `equals`/`hashCode` contract, and what breaks if you override only one?
5. Checked vs unchecked exceptions: your rule for choosing, and why swallowing or `return` inside `finally` is dangerous.
