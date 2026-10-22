# Answers: H2-overloading-singleton

(Spoilers. Open only after your attempt.)

**Reference**
- Prints `B-obj`, `A-str`, `B-obj`. Overload resolution uses the STATIC (compile-time) types: `who(o)` has static type `Object` -> picks `who(Object)`, then the runtime class `B` overrides it -> `B-obj`. `who("hi")` -> most specific is `who(String)`, which `B` does not override -> `A-str`. `who(5)`: the compiler only looks at `A`'s methods (static type `A`), `Integer` is boxed to `Object` -> `who(Object)` -> `B-obj`; `B.who(Integer)` is never reachable through an `A` reference.
- Compile time: which overload (signature). Runtime: which override (virtual dispatch on the real object). `static` methods and fields are not polymorphic; they are hidden and resolved by the reference type.
- Singleton: (1) `enum Singleton { INSTANCE }` — simplest, serialization and reflection safe, best default. (2) Initialization-on-demand holder class — lazy and lock free thanks to class loading guarantees. (3) Double-checked locking — requires a `volatile` field, or a half-constructed object can be seen. Spring singleton scope is one instance per application context, not per JVM, and is not enforced by the class itself.
- Open/Closed Principle: add a new payment type by adding a new class behind a `PaymentStrategy` interface (plus registry), without editing the dispatching code.

Line 4 of the snippets (static and field hiding) prints `P Q-inst P-field Q-field`: the static method and the field are chosen by the reference type `P`, the instance method by the real object `Q`.
