package practice.d20261104.h2;

/**
 * Cross-cutting behaviour with java.lang.reflect.Proxy: the mechanism behind Spring AOP's JDK proxies (@Transactional, @Cacheable, @Retryable).
 * Both factories require an interface (IllegalArgumentException otherwise) and return a proxy implementing it that delegates to target.
 */
public final class Proxies {
    private Proxies() {}

    /**
     * Methods annotated @Memo (on the INTERFACE method) are cached per (method, argument list), compared with equals; all other methods, including
     * toString/hashCode/equals, go straight to the target. null arguments and null results must work (cache a null result too). Exceptions are never cached.
     * The exception the target throws must reach the caller unchanged (unwrap InvocationTargetException; a declared checked exception must not turn into
     * UndeclaredThrowableException). For concurrent calls with the same key the target runs at most once.
     */
    public static <T> T memoizing(Class<T> iface, T target) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Every method call is attempted up to maxAttempts times in total (maxAttempts >= 1, else IllegalArgumentException). Only RuntimeException triggers a retry;
     * a checked exception or an Error is rethrown immediately. After the last failed attempt the LAST exception is rethrown unchanged.
     */
    public static <T> T retrying(Class<T> iface, T target, int maxAttempts) { throw new UnsupportedOperationException("TODO"); }
}
