# Answers: H1-try-finally-string-pool

(Spoilers. Open only after your attempt.)

**Reference** (outputs verified by running the code)
- `f()` prints `fin` and returns `1`. `g()` returns `1`: the return value is evaluated and saved before `finally` runs, so `x = 2` does not change it. `h()` returns `3` and the `RuntimeException` vanishes: a `return` in `finally` overrides any pending exception or return. Never do it.
- Booleans: `false`, `true`, `true`, `true`, `false`, `true`. `new String("java")` creates a new heap object, so `a == b` is false. `intern()` returns the pooled instance, so `a == c` is true. `"ja" + "va"` is a compile-time constant folded to `"java"`, so it is the pooled instance: true. `ja + "va"` uses a non-final variable, evaluated at runtime into a new String: false. With `final String fja = "ja"` it becomes a constant expression again: true. Always compare with `equals`.
- try-with-resources: prints `open 1`, `open 2`, `close 2`, `close 1` (reverse order of opening). Output: `body [java.lang.IllegalStateException: close-2, java.lang.IllegalStateException: close-1]`. The body's exception is primary; exceptions from `close()` are attached with `addSuppressed`, so nothing is lost (unlike the old finally-with-close pattern, where the close exception would replace the original one).
- Immutable `String` gives: string pool sharing, a cached hash code (safe as HashMap keys), thread safety for free, and security (a class name or path cannot change after validation). `StringBuilder` is unsynchronized and fast; `StringBuffer` is synchronized, rarely needed. Note that `+` in a loop creates many temporary objects, use a builder.
- Contract: equal objects must have equal hash codes; `equals` must be reflexive, symmetric, transitive, consistent, and false for null. Override `equals` only: two equal objects hash to different buckets, so `HashSet.contains` returns false for an equal object. Override `hashCode` only: equal-hash but never equal, so duplicates are kept.
- Checked exceptions: recoverable conditions the caller must handle (I/O, a payment gateway refusing); unchecked: programming errors or failures where the caller cannot sensibly recover (illegal argument, illegal state). Modern services mostly wrap checked ones into a domain unchecked exception at module boundaries and translate it once in a global handler. Swallowing hides failures; `return` in `finally` hides exceptions.

Kit note: LINE6 in the code (`hashCode` overridden, `equals` not; two `OnlyHash(1)` added to a `HashSet`) gives `2 false`.
