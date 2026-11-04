# G. LLD of F1: in-memory feature flag evaluator (sketch 8 + code 22 min)

> Sketch in `my-designs/G-classes-sketch.md` first. Code: `src/main/java/practice/d20261104/g/FeatureFlags.java` and the validation of `Flag` (`Context` is given). Test: `FeatureFlagsTest`.

- `Flag` is an immutable record: validate in the compact constructor and keep deep immutable copies of its sets and map.
- `FeatureFlags.isEnabled(key, ctx)` evaluates in this order: unknown flag -> false; kill switch (`enabled == false`) -> false; allow-listed user -> true; attribute rules (all must match, a missing attribute fails) -> else false; then the percentage via a stable bucket in `[0, 10_000)`.
- `bucket(key, userId)` is deterministic, independent per flag, evenly spread and monotone: a user in at 10 % is still in at 30 %.
- Readers never block and never see a half-applied update: `replaceAll` swaps the whole set; `evaluateAll` evaluates all flags against one snapshot.

Think about: copy-on-write snapshot vs `ConcurrentHashMap`; why raw `String.hashCode()` is a weak bucket function; what you would log for experiment exposure; how a poller/stream feeds `replaceAll`.
