package practice.d20261112.h2;

/** One link of the chain. Call next.proceed(request) to continue, or return without calling it to short-circuit. */
@FunctionalInterface
public interface Handler<R, S> {
    S handle(R request, Chain<R, S> next);

    interface Chain<R, S> {
        S proceed(R request);
    }
}
