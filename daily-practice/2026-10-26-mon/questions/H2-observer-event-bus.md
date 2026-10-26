> Code: `src/main/java/practice/d20261026/h2/EventBus.java` (`Subscription` is given). Test: `EventBusTest`. **30 min.**

# H2. OOP: the Observer pattern as a typed event bus

Implement `EventBus`. The contract is in the class Javadoc: typed subscription (a handler for an interface also gets its implementations), subscription-order delivery, fault isolation (one throwing handler does not stop the rest; errors go to a callback), safe cancel and subscribe DURING a publish, re-entrant publish.

Think first (5 min, paper): what data structure makes "subscribe during dispatch" safe without a `ConcurrentModificationException`? Where does the one unchecked cast live and why is it safe? What does `Consumer<? super E>` buy you over `Consumer<E>`?

Say out loud: Observer vs publish/subscribe with a broker (what is lost when it is in-process: ordering, durability, back-pressure); why a handler that cancels itself must not be a special case; how you would make it thread-safe (copy-on-write list, volatile flag) and what the memory cost is; the memory-leak trap of never calling `cancel()` (the bus holds the handler, the handler holds its owner) and the weak-reference alternative.
