package practice.d20261112.h2;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Chain of Responsibility / middleware. The chain position lives in an immutable per-call object (index passed down the stack), never in a
 * field of Pipeline: that makes the pipeline thread-safe and lets one handler call proceed() several times (retry) - each call re-runs the rest.
 * execute: O(handlers) per call, O(handlers) stack depth.
 */
public final class Pipeline<R, S> {
    private final List<Handler<R, S>> handlers;
    private final Function<R, S> terminal;

    public Pipeline(List<Handler<R, S>> handlers, Function<R, S> terminal) {
        this.handlers = List.copyOf(handlers);           // defensive copy, rejects null elements
        this.terminal = Objects.requireNonNull(terminal);
    }

    public S execute(R request) {
        return new Link(0).proceed(request);
    }

    private final class Link implements Handler.Chain<R, S> {
        private final int index;
        Link(int index) { this.index = index; }
        @Override public S proceed(R request) {
            if (index == handlers.size()) return terminal.apply(request);
            return handlers.get(index).handle(request, new Link(index + 1));
        }
    }

    /** Retries the rest of the chain up to maxAttempts times while it throws RuntimeException; rethrows the last one. */
    public static <R, S> Handler<R, S> retry(int maxAttempts) {
        if (maxAttempts < 1) throw new IllegalArgumentException("maxAttempts");
        return (req, next) -> {
            RuntimeException last = null;
            for (int i = 0; i < maxAttempts; i++) {
                try { return next.proceed(req); } catch (RuntimeException e) { last = e; }
            }
            throw last;
        };
    }

    /** Short-circuits with `fallback` when `reject` says the request must not go further. */
    public static <R, S> Handler<R, S> guard(Predicate<R> reject, S fallback) {
        return (req, next) -> reject.test(req) ? fallback : next.proceed(req);
    }
}
