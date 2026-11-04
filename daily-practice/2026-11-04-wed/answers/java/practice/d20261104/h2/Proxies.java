package practice.d20261104.h2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Proxy.newProxyInstance routes every interface call (and toString/hashCode/equals) to one InvocationHandler.
 * invoke() unwraps InvocationTargetException so callers see the target's own exception; a checked exception that the interface declares passes through as is.
 * memoizing: key = [method, args...] (ArrayList tolerates null args); ConcurrentHashMap.computeIfAbsent makes the first caller run the target while others wait.
 * A thrown exception inside computeIfAbsent leaves no mapping, so failures are never cached; a checked Throwable is smuggled through the lambda in a Carrier.
 * A null result is stored as a sentinel because ConcurrentHashMap rejects null values.
 */
public final class Proxies {
    private Proxies() {}

    private static final Object NULL = new Object();

    private static final class Carrier extends RuntimeException {
        final Throwable cause0;
        Carrier(Throwable t) { super(null, null, false, false); this.cause0 = t; }
    }

    private static Object call(Method m, Object target, Object[] args) throws Throwable {
        try {
            return m.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private static void requireInterface(Class<?> iface) {
        if (!iface.isInterface()) throw new IllegalArgumentException(iface + " is not an interface");
    }

    public static <T> T memoizing(Class<T> iface, T target) {
        requireInterface(iface);
        Map<List<Object>, Object> cache = new ConcurrentHashMap<>();
        return iface.cast(Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[]{iface}, (proxy, m, args) -> {
            if (!m.isAnnotationPresent(Memo.class)) return call(m, target, args);
            List<Object> key = new ArrayList<>();
            key.add(m);
            if (args != null) key.addAll(Arrays.asList(args));
            try {
                Object v = cache.computeIfAbsent(key, k -> {
                    try {
                        Object r = call(m, target, args);
                        return r == null ? NULL : r;
                    } catch (Throwable t) {
                        throw new Carrier(t);
                    }
                });
                return v == NULL ? null : v;
            } catch (Carrier c) {
                throw c.cause0;
            }
        }));
    }

    public static <T> T retrying(Class<T> iface, T target, int maxAttempts) {
        requireInterface(iface);
        if (maxAttempts < 1) throw new IllegalArgumentException("maxAttempts " + maxAttempts);
        return iface.cast(Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[]{iface}, (proxy, m, args) -> {
            for (int attempt = 1; ; attempt++) {
                try {
                    return call(m, target, args);
                } catch (RuntimeException e) {
                    if (attempt >= maxAttempts) throw e;
                }
            }
        }));
    }
}
