# Answers: H2-liskov-composition

(Spoilers. Open only after your attempt.)

**Reference**
- Liskov Substitution Principle: a subtype must be usable wherever the base type is, without changing correctness. `Square` strengthens the invariant (w == h), so `area()` is 16, not 20.
- Redesign: a `Shape` interface with `area()`, immutable `Rectangle(w,h)` and `Square(side)` as separate implementations. Immutability removes the broken setter contract.
- Abstract class: shared state, constructors, protected helpers, template method. Interface: capability or contract, multiple implementation, default methods for evolution without breaking implementors. Diamond: if two interfaces provide the same default method, the class must override and may call `A.super.m()`; a class method (or more specific interface) always wins over an interface default.
- Composition example: a `RetryingHttpClient` that wraps an `HttpClient` (decorator) rather than extending it, so retry logic is reusable across clients and testable with a fake.
