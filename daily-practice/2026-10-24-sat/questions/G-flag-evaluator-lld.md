> Code: `src/main/java/practice/d20261024/g/` (`Flag`, `FlagStore`, `FlagEvaluator` are given; implement `InMemoryFlagStore` and `DefaultFlagEvaluator`). Test: `DefaultFlagEvaluatorTest`.

# G. LLD of F1: the in-process flag evaluator (sketch 10 min, code 25 min)

Sketch in `my-designs/G-classes-sketch.md` first (what is immutable, where the store ends and the evaluator begins, how a new rule type would be added), then implement.

Contract (Javadoc of `FlagEvaluator`): unknown flag -> off; a disabled flag is off even for allow-listed users (kill switch); allow-listed users are on; everybody else is on iff `bucket(flag, user) < rolloutPercent`. The bucket is stable across runs and servers, independent per flag (so the same 10% of users do not get every experiment), and monotonic (raising 20% to 50% never switches a user off). A null or blank user id throws `IllegalArgumentException` before any lookup. The store is safe for concurrent readers and writers; a reader never sees a half-updated flag. 1M evaluations in under 3 s.

Say out loud: why `String.hashCode()` or `Object.hashCode()` are the wrong source of the bucket (the second is not even stable between runs); why the flag key is part of the hash input; what you would change to support several rule types (a `Rule` chain, first match wins) without a growing `if`; how a stale cache in a client is bounded (TTL + version).
