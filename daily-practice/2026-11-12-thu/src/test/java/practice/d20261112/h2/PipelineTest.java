package practice.d20261112.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class PipelineTest {
    private static Handler<String, String> logging(String name, List<String> log) {
        return (req, next) -> { log.add("before " + name); String res = next.proceed(req); log.add("after " + name); return res; };
    }

    @Test void runsInOrderAndUnwindsInReverse() {
        List<String> log = new ArrayList<>();
        Pipeline<String, String> p = new Pipeline<>(List.of(logging("A", log), logging("B", log)), r -> { log.add("terminal"); return r.toUpperCase(); });
        assertEquals("HI", p.execute("hi"));
        assertEquals(List.of("before A", "before B", "terminal", "after B", "after A"), log);
    }
    @Test void emptyChainCallsTerminal() {
        assertEquals(5, new Pipeline<Integer, Integer>(List.of(), r -> r + 4).execute(1));
    }
    @Test void handlersCanTransformRequestAndResponse() {
        Handler<Integer, String> plusOne = (r, n) -> n.proceed(r + 1);
        Handler<Integer, String> brackets = (r, n) -> "[" + n.proceed(r) + "]";
        Pipeline<Integer, String> p = new Pipeline<>(List.of(brackets, plusOne, plusOne), r -> "v" + r);
        assertEquals("[v7]", p.execute(5));
    }
    @Test void shortCircuitSkipsTheRest() {
        AtomicInteger terminalCalls = new AtomicInteger();
        Pipeline<String, String> p = new Pipeline<>(List.of(Pipeline.<String, String>guard(String::isEmpty, "rejected")), r -> { terminalCalls.incrementAndGet(); return r; });
        assertEquals("rejected", p.execute(""));
        assertEquals(0, terminalCalls.get());
        assertEquals("x", p.execute("x"));
        assertEquals(1, terminalCalls.get());
    }
    @Test void retryReRunsTheRestOfTheChain() {
        AtomicInteger calls = new AtomicInteger();
        List<String> log = new ArrayList<>();
        Pipeline<String, String> p = new Pipeline<>(List.of(Pipeline.<String, String>retry(3), logging("inner", log)), r -> {
            if (calls.incrementAndGet() < 3) throw new IllegalStateException("boom" + calls.get());
            return "ok";
        });
        assertEquals("ok", p.execute("x"));
        assertEquals(3, calls.get());
        assertEquals(List.of("before inner", "before inner", "before inner", "after inner"), log);
    }
    @Test void retryGivesUpAndRethrowsLast() {
        AtomicInteger calls = new AtomicInteger();
        Pipeline<String, String> p = new Pipeline<>(List.of(Pipeline.<String, String>retry(2)), r -> { throw new IllegalStateException("fail" + calls.incrementAndGet()); });
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> p.execute("x"));
        assertEquals("fail2", e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> Pipeline.retry(0));
    }
    @Test void listIsCopiedAndNullsRejected() {
        List<Handler<String, String>> hs = new ArrayList<>();
        Pipeline<String, String> p = new Pipeline<>(hs, r -> r);
        hs.add((r, n) -> "changed");
        assertEquals("a", p.execute("a"));
        List<Handler<String, String>> withNull = new ArrayList<>();
        withNull.add(null);
        assertThrows(NullPointerException.class, () -> new Pipeline<>(withNull, r -> r));
    }
    @Test void reusableAndThreadSafe() throws Exception {
        Pipeline<Integer, Integer> p = new Pipeline<>(List.of((r, n) -> n.proceed(r + 1), (r, n) -> n.proceed(r * 2) + 1), r -> r);
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<Boolean>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) fs.add(pool.submit(() -> { for (int i = 0; i < 20_000; i++) if (p.execute(i) != (i + 1) * 2 + 1) return false; return true; }));
        for (Future<Boolean> f : fs) assertTrue(f.get(10, TimeUnit.SECONDS));
        pool.shutdown();
    }
    @Test void deepChainWorks() {
        List<Handler<Integer, Integer>> hs = new ArrayList<>();
        for (int i = 0; i < 1000; i++) hs.add((r, n) -> n.proceed(r + 1));
        assertEquals(1000, new Pipeline<>(hs, r -> r).execute(0));
    }
}
