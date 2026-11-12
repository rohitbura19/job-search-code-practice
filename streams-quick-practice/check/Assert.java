package check;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** Minimal stand-in for JUnit's Assertions, so the checks run with plain `java`. */
public final class Assert {
    private Assert() {}

    @FunctionalInterface
    public interface Executable { void execute() throws Throwable; }

    @FunctionalInterface
    public interface ThrowingSupplier<T> { T get() throws Throwable; }

    private static AssertionError fail(String msg, Object expected, Object actual) {
        String m = (msg == null ? "" : msg + " ") + "expected <" + str(expected) + "> but was <" + str(actual) + ">";
        return new AssertionError(m);
    }

    private static String str(Object o) {
        if (o instanceof Object[] a) return Arrays.deepToString(a);
        if (o instanceof int[] a) return Arrays.toString(a);
        if (o instanceof long[] a) return Arrays.toString(a);
        if (o instanceof double[] a) return Arrays.toString(a);
        return String.valueOf(o);
    }

    // ---- assertEquals: the same overload set JUnit offers, so calls resolve identically
    public static void assertEquals(Object e, Object a) { assertEquals(e, a, (String) null); }
    public static void assertEquals(Object e, Object a, String msg) { if (!Objects.deepEquals(e, a)) throw fail(msg, e, a); }
    public static void assertEquals(int e, int a) { assertEquals(e, a, (String) null); }
    public static void assertEquals(int e, int a, String msg) { if (e != a) throw fail(msg, e, a); }
    public static void assertEquals(int e, Integer a) { assertEquals((Object) e, a); }
    public static void assertEquals(Integer e, int a) { assertEquals(e, (Object) a); }
    public static void assertEquals(Integer e, Integer a) { assertEquals((Object) e, a); }
    public static void assertEquals(long e, long a) { assertEquals(e, a, (String) null); }
    public static void assertEquals(long e, long a, String msg) { if (e != a) throw fail(msg, e, a); }
    public static void assertEquals(long e, Long a) { assertEquals((Object) e, a); }
    public static void assertEquals(Long e, long a) { assertEquals(e, (Object) a); }
    public static void assertEquals(Long e, Long a) { assertEquals((Object) e, a); }
    public static void assertEquals(char e, char a) { if (e != a) throw fail(null, e, a); }
    public static void assertEquals(double e, double a) { assertEquals(e, a, (String) null); }
    public static void assertEquals(double e, double a, String msg) { if (Double.compare(e, a) != 0) throw fail(msg, e, a); }
    public static void assertEquals(double e, Double a) { assertEquals((Object) e, a); }
    public static void assertEquals(Double e, double a) { assertEquals(e, (Object) a); }
    public static void assertEquals(Double e, Double a) { assertEquals((Object) e, a); }
    public static void assertEquals(double e, double a, double delta) { assertEquals(e, a, delta, null); }
    public static void assertEquals(double e, double a, double delta, String msg) {
        if (Math.abs(e - a) > delta && Double.compare(e, a) != 0) throw fail(msg, e, a);
    }

    public static void assertArrayEquals(int[] e, int[] a) { if (!Arrays.equals(e, a)) throw fail(null, e, a); }
    public static void assertArrayEquals(long[] e, long[] a) { if (!Arrays.equals(e, a)) throw fail(null, e, a); }
    public static void assertArrayEquals(double[] e, double[] a) { if (!Arrays.equals(e, a)) throw fail(null, e, a); }
    public static void assertArrayEquals(Object[] e, Object[] a) { if (!Arrays.deepEquals(e, a)) throw fail(null, e, a); }

    public static void assertTrue(boolean c) { assertTrue(c, null); }
    public static void assertTrue(boolean c, String msg) { if (!c) throw new AssertionError(msg == null ? "expected true" : msg); }
    public static void assertFalse(boolean c) { assertFalse(c, null); }
    public static void assertFalse(boolean c, String msg) { if (c) throw new AssertionError(msg == null ? "expected false" : msg); }
    public static void assertNull(Object o) { if (o != null) throw fail(null, null, o); }
    public static void assertNull(Object o, String msg) { if (o != null) throw fail(msg, null, o); }
    public static void assertNotNull(Object o) { if (o == null) throw new AssertionError("expected not null"); }
    public static void assertSame(Object e, Object a) { if (e != a) throw fail("not same:", e, a); }
    public static void assertSame(Object e, Object a, String msg) { if (e != a) throw fail(msg, e, a); }

    public static <T extends Throwable> T assertThrows(Class<T> type, Executable ex) {
        try {
            ex.execute();
        } catch (Throwable t) {
            if (type.isInstance(t)) return type.cast(t);
            if (t instanceof UnsupportedOperationException && "TODO".equals(t.getMessage())) throw (UnsupportedOperationException) t;
            throw new AssertionError("expected " + type.getSimpleName() + " but got " + t, t);
        }
        throw new AssertionError("expected " + type.getSimpleName() + " to be thrown");
    }

    public static void assertTimeoutPreemptively(Duration limit, Executable ex) {
        assertTimeoutPreemptively(limit, () -> { ex.execute(); return null; });
    }

    public static <T> T assertTimeoutPreemptively(Duration limit, ThrowingSupplier<T> s) {
        ExecutorService pool = Executors.newSingleThreadExecutor(r -> { Thread t = new Thread(r); t.setDaemon(true); return t; });
        try {
            Future<T> f = pool.submit(() -> {
                try { return s.get(); } catch (Exception | Error e) { throw e; } catch (Throwable t) { throw new RuntimeException(t); }
            });
            return f.get(limit.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new AssertionError("too slow: over " + limit.toMillis() + " ms");
        } catch (java.util.concurrent.ExecutionException e) {
            Throwable c = e.getCause();
            if (c instanceof RuntimeException r) throw r;
            if (c instanceof Error er) throw er;
            throw new RuntimeException(c);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError("interrupted");
        } finally {
            pool.shutdownNow();
        }
    }
}
