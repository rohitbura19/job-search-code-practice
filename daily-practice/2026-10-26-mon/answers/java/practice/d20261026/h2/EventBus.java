package practice.d20261026.h2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Observer with a snapshot per publish: publish copies the entry list first, so a subscribe during dispatch cannot show up and
 * a concurrent-modification error is impossible; each entry has its own `active` flag that cancel() clears, and dispatch checks
 * the flag right before calling, which gives "cancel takes effect immediately". The entries list only grows (cancelled entries
 * are pruned lazily on the next subscribe to keep memory bounded). Type-safety: the unchecked cast is confined to one place and
 * justified by the isInstance check. Not thread-safe by design; for threads use CopyOnWriteArrayList and a volatile flag.
 */
public class EventBus {
    private static final class Entry<E> implements Subscription {
        final Class<E> type;
        final Consumer<? super E> handler;
        boolean active = true;

        Entry(Class<E> type, Consumer<? super E> handler) { this.type = type; this.handler = handler; }

        @Override public void cancel() { active = false; }
        @Override public boolean isActive() { return active; }

        boolean deliver(Object event) {
            handler.accept(type.cast(event));
            return true;
        }
    }

    private final BiConsumer<Object, Throwable> errors;
    private List<Entry<?>> entries = new ArrayList<>();

    public EventBus(BiConsumer<Object, Throwable> errorHandler) { this.errors = Objects.requireNonNull(errorHandler); }

    public <E> Subscription subscribe(Class<E> type, Consumer<? super E> handler) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(handler);
        List<Entry<?>> next = new ArrayList<>(entries.size() + 1);       // copy-on-write: running publishes keep their old snapshot
        for (Entry<?> e : entries) if (e.active) next.add(e);
        Entry<E> entry = new Entry<>(type, handler);
        next.add(entry);
        entries = next;
        return entry;
    }

    public int publish(Object event) {
        if (event == null) throw new IllegalArgumentException("event");
        List<Entry<?>> snapshot = entries;                                 // never mutated after creation
        int delivered = 0;
        for (Entry<?> e : snapshot) {
            if (!e.active || !e.type.isInstance(event)) continue;
            try {
                if (e.deliver(event)) delivered++;
            } catch (RuntimeException ex) {
                errors.accept(event, ex);
            }
        }
        return delivered;
    }
}
