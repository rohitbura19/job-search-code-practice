package practice.d20261106.h2;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * One CopyOnWriteArrayList of entries: iteration works on a snapshot, so handlers may (un)subscribe during delivery; reads are lock-free,
 * writes copy (fine: subscribe/cancel are rare, publish is hot). Matching is Class.isAssignableFrom. Each handler call is isolated in
 * try/catch so one bad subscriber cannot break delivery. A cancelled flag also stops a handler that was cancelled mid-publish.
 */
public class EventBus {
    private static final class Entry {
        final Class<?> type;
        final Consumer<Object> handler;
        volatile boolean cancelled;

        @SuppressWarnings("unchecked")
        Entry(Class<?> type, Consumer<?> handler) {
            this.type = type;
            this.handler = (Consumer<Object>) handler;
        }
    }

    private final CopyOnWriteArrayList<Entry> entries = new CopyOnWriteArrayList<>();

    public <T> Subscription subscribe(Class<T> type, Consumer<? super T> handler) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(handler);
        Entry e = new Entry(type, handler);
        entries.add(e);
        return () -> {
            e.cancelled = true;
            entries.remove(e);
        };
    }

    public int publish(Object event) {
        Objects.requireNonNull(event);
        int matched = 0, ok = 0;
        for (Entry e : entries) {
            if (e.cancelled || !e.type.isAssignableFrom(event.getClass())) continue;
            matched++;
            try {
                e.handler.accept(event);
                ok++;
            } catch (RuntimeException ex) {
                // isolated on purpose
            }
        }
        if (matched == 0 && !(event instanceof DeadEvent)) publish(new DeadEvent(event));
        return ok;
    }
}
