package practice.d20261112.h2;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Middleware pipeline (Chain of Responsibility), like servlet filters.
 *  - handlers run in list order; handler i receives a Chain whose proceed() runs handlers i+1.. and finally `terminal`.
 *  - a handler may change the request it passes on, change the response it returns, or not call proceed() at all.
 *  - a handler may call proceed() more than once; each call re-runs the rest of the chain from scratch.
 *  - the constructor copies the list (later changes to the caller's list have no effect) and rejects null handlers (NullPointerException).
 *  - execute() must be thread-safe and re-entrant: many threads, many calls, no per-pipeline mutable state.
 */
public final class Pipeline<R, S> {
    public Pipeline(List<Handler<R, S>> handlers, Function<R, S> terminal) { throw new UnsupportedOperationException("TODO"); }

    public S execute(R request) { throw new UnsupportedOperationException("TODO"); }

    /** Retries the rest of the chain up to maxAttempts times while it throws RuntimeException; rethrows the last one. maxAttempts < 1 -> IllegalArgumentException. */
    public static <R, S> Handler<R, S> retry(int maxAttempts) { throw new UnsupportedOperationException("TODO"); }

    /** If reject.test(request) is true return `fallback` WITHOUT calling the rest of the chain, else continue. */
    public static <R, S> Handler<R, S> guard(Predicate<R> reject, S fallback) { throw new UnsupportedOperationException("TODO"); }
}
