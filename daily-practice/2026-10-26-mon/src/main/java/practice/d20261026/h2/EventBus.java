package practice.d20261026.h2;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A single-threaded, typed in-process event bus (Observer pattern). Contract:
 * - subscribe(type, handler): the handler receives every published event that `type.isInstance(event)` (so a handler for an
 *   interface or superclass also receives subtypes). Handlers are called in SUBSCRIPTION order across all types.
 * - publish(event) returns how many handlers returned normally. A handler that throws must not stop the others; the bus
 *   reports (event, throwable) to the error handler given at construction and goes on. Publishing null -> IllegalArgumentException.
 * - Subscribing during a publish does not affect that publish (only later ones). Cancelling during a publish DOES: a cancelled
 *   handler that has not run yet in the current publish is skipped. A handler may publish again (re-entrant, depth-first).
 * - subscribe with a null type or handler -> NullPointerException (fail fast, at subscribe time).
 */
public class EventBus {
    public EventBus(BiConsumer<Object, Throwable> errorHandler) {}

    public <E> Subscription subscribe(Class<E> type, Consumer<? super E> handler) { throw new UnsupportedOperationException("TODO"); }

    public int publish(Object event) { throw new UnsupportedOperationException("TODO"); }
}
