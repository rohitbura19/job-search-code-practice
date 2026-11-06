package practice.d20261106.h2;

import java.util.function.Consumer;

/**
 * In-process typed event bus. Handlers for a supertype or interface also receive subclass events
 * (subscribe(Object.class, h) sees everything). Delivery happens on the publisher's thread in subscription order.
 * Thread-safe; a handler may subscribe, cancel or publish from inside a callback without ConcurrentModificationException.
 */
public class EventBus {
    /** NullPointerException on null arguments. */
    public <T> Subscription subscribe(Class<T> type, Consumer<? super T> handler) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Calls every handler whose type is assignable from event.getClass(). A handler that throws does not stop the others.
     * Returns the number of handlers that completed normally. If NO handler matched and the event is not itself a DeadEvent,
     * publishes new DeadEvent(event) (its handlers are not counted in the return value). NullPointerException on null.
     */
    public int publish(Object event) { throw new UnsupportedOperationException("TODO"); }
}
