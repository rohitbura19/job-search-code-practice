> Code: `src/main/java/practice/d20261025/h2/` (`Order`, `Pricing`, `BasePricing` are given; implement `DiscountPricing`, `TaxPricing`, `CappedPricing`, `CachingPricing`). Test: `PricingTest`.

# H2. OOP: the Decorator pattern for pricing (30 min)

Each decorator implements `Pricing` and wraps another `Pricing` (composition, not inheritance): discount (rounded down), tax in basis points (half up, exact arithmetic), a cap, and a per-order cache. Order of wrapping changes the result; decorators validate their arguments and are immutable (the cache is the only mutable one, and thread-safe).

Say out loud: decorator vs subclass explosion (`DiscountedTaxedPricing`...); decorator vs strategy vs chain of responsibility; why `final` classes with `private final` fields; what the caching decorator must not do (cache failures, hold a lock around a slow call); where a decorator breaks Liskov (it must keep the contract of `Pricing`, e.g. never return negative).
