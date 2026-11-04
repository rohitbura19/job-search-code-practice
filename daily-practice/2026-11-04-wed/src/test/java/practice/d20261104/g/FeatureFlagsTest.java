package practice.d20261104.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class FeatureFlagsTest {
    final FeatureFlags flags = new FeatureFlags();

    @Test void unknownFlagIsOffAndOnIsOn() {
        assertFalse(flags.isEnabled("nope", Context.of("u1")));
        flags.put(Flag.on("dark-mode"));
        assertTrue(flags.isEnabled("dark-mode", Context.of("u1")));
        assertTrue(flags.isEnabled("dark-mode", Context.of(null)), "100% includes anonymous users");
    }

    @Test void killSwitchBeatsAllowList() {
        flags.put(new Flag("f", false, Set.of("vip"), Map.of(), 100));
        assertFalse(flags.isEnabled("f", Context.of("vip")));
    }

    @Test void allowListSkipsRulesAndPercent() {
        flags.put(new Flag("f", true, Set.of("vip"), Map.of("country", Set.of("IN")), 0));
        assertTrue(flags.isEnabled("f", Context.of("vip", "country", "US")));
        assertFalse(flags.isEnabled("f", Context.of("other", "country", "IN")), "0% and not allow-listed");
    }

    @Test void attributeRulesGateTheRollout() {
        flags.put(new Flag("pay", true, Set.of(), Map.of("country", Set.of("IN", "US"), "plan", Set.of("pro")), 100));
        assertTrue(flags.isEnabled("pay", new Context("u", Map.of("country", "IN", "plan", "pro"))));
        assertFalse(flags.isEnabled("pay", new Context("u", Map.of("country", "IN", "plan", "free"))));
        assertFalse(flags.isEnabled("pay", new Context("u", Map.of("country", "IN"))), "missing attribute fails the rule");
        assertFalse(flags.isEnabled("pay", Context.of("u")));
    }

    @Test void percentEdgesAndAnonymous() {
        flags.put(Flag.rollout("zero", 0));
        flags.put(Flag.rollout("half", 50));
        for (int i = 0; i < 100; i++) assertFalse(flags.isEnabled("zero", Context.of("u" + i)));
        assertFalse(flags.isEnabled("half", Context.of(null)), "anonymous users are never in a partial rollout");
    }

    @Test void bucketIsStableInRangeAndSpreadEvenly() {
        assertEquals(FeatureFlags.bucket("f", "alice"), FeatureFlags.bucket("f", "alice"));
        assertNotEquals(FeatureFlags.bucket("f", "alice") + FeatureFlags.bucket("f", "bob") + FeatureFlags.bucket("g", "alice"), 0);
        int[] decile = new int[10];
        for (int i = 0; i < 50_000; i++) {
            int b = FeatureFlags.bucket("checkout-v2", "user-" + i);
            assertTrue(b >= 0 && b < 10_000);
            decile[b / 1000]++;
        }
        for (int c : decile) assertTrue(Math.abs(c - 5000) < 300, "uneven spread: " + Arrays.toString(decile));
    }

    @Test void rolloutMatchesPercentAndOnlyGrows() {
        Set<String> at10 = new HashSet<>(), at30 = new HashSet<>();
        for (int pct : new int[]{10, 30}) {
            FeatureFlags f = new FeatureFlags();
            f.put(Flag.rollout("new-search", pct));
            for (int i = 0; i < 20_000; i++) if (f.isEnabled("new-search", Context.of("u" + i))) (pct == 10 ? at10 : at30).add("u" + i);
        }
        assertTrue(Math.abs(at10.size() - 2000) < 250, "10% -> " + at10.size());
        assertTrue(Math.abs(at30.size() - 6000) < 350, "30% -> " + at30.size());
        assertTrue(at30.containsAll(at10), "raising the percentage must not drop anybody");
    }

    @Test void flagsRollOutIndependently() {
        int both = 0, total = 20_000;
        for (int i = 0; i < total; i++) {
            boolean a = FeatureFlags.bucket("A", "u" + i) < 5000, b = FeatureFlags.bucket("B", "u" + i) < 5000;
            if (a && b) both++;
        }
        assertTrue(Math.abs(both - total / 4) < 500, "flags A and B are correlated: " + both);
    }

    @Test void putRemoveGetSize() {
        assertEquals(0, flags.size());
        flags.put(Flag.on("a"));
        flags.put(Flag.rollout("a", 5));
        assertEquals(1, flags.size());
        assertEquals(5, flags.get("a").orElseThrow().percent());
        assertTrue(flags.remove("a"));
        assertFalse(flags.remove("a"));
        assertTrue(flags.get("a").isEmpty());
    }

    @Test void flagValidationAndDefensiveCopies() {
        assertThrows(NullPointerException.class, () -> Flag.on(null));
        assertThrows(IllegalArgumentException.class, () -> Flag.on(" "));
        assertThrows(IllegalArgumentException.class, () -> Flag.rollout("x", -1));
        assertThrows(IllegalArgumentException.class, () -> Flag.rollout("x", 101));
        Set<String> users = new HashSet<>(Set.of("a"));
        Set<String> vals = new HashSet<>(Set.of("IN"));
        Map<String, Set<String>> rules = new HashMap<>(Map.of("country", vals));
        Flag f = new Flag("x", true, users, rules, 100);
        users.add("b"); vals.add("US"); rules.put("plan", Set.of("pro"));
        assertEquals(Set.of("a"), f.allowUsers());
        assertEquals(Map.of("country", Set.of("IN")), f.attrRules());
        assertThrows(UnsupportedOperationException.class, () -> f.allowUsers().add("z"));
        assertThrows(UnsupportedOperationException.class, () -> f.attrRules().get("country").add("z"));
        Flag n = new Flag("n", true, null, null, 0);
        assertTrue(n.allowUsers().isEmpty() && n.attrRules().isEmpty());
    }

    @Test void replaceAllSwapsTheWholeSetAndLastDuplicateWins() {
        flags.put(Flag.on("old"));
        flags.replaceAll(List.of(Flag.on("a"), Flag.rollout("a", 0), Flag.on("b")));
        assertEquals(2, flags.size());
        assertTrue(flags.get("old").isEmpty());
        assertEquals(0, flags.get("a").orElseThrow().percent());
        flags.replaceAll(List.of());
        assertEquals(0, flags.size());
    }

    @Test void evaluateAllUsesOneSnapshot() throws Exception {
        Flag aOn = Flag.on("a"), bOn = Flag.on("b");
        Flag aOff = new Flag("a", false, Set.of(), Map.of(), 100), bOff = new Flag("b", false, Set.of(), Map.of(), 100);
        flags.replaceAll(List.of(aOn, bOn));
        AtomicBoolean stop = new AtomicBoolean();
        Thread writer = new Thread(() -> {
            boolean on = false;
            while (!stop.get()) { flags.replaceAll(on ? List.of(aOn, bOn) : List.of(aOff, bOff)); on = !on; }
        });
        writer.start();
        try {
            for (int i = 0; i < 200_000; i++) {
                Map<String, Boolean> m = flags.evaluateAll(Context.of("u"));
                assertEquals(m.get("a"), m.get("b"), "saw a half-applied update");
            }
        } finally { stop.set(true); writer.join(); }
    }

    @Test void concurrentPutsLoseNothing() throws Exception {
        List<Thread> ts = new ArrayList<>();
        for (int t = 0; t < 8; t++) {
            int id = t;
            Thread th = new Thread(() -> { for (int i = 0; i < 200; i++) flags.put(Flag.on("t" + id + "-" + i)); });
            ts.add(th); th.start();
        }
        for (Thread th : ts) th.join();
        assertEquals(1600, flags.size());
    }

    @Test void millionEvaluationsAreFast() {
        flags.put(new Flag("big", true, Set.of("vip"), Map.of("country", Set.of("IN", "US")), 37));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            Context[] ctxs = new Context[1000];
            for (int i = 0; i < ctxs.length; i++) ctxs[i] = Context.of("u" + i, "country", i % 2 == 0 ? "IN" : "FR");
            int on = 0;
            for (int r = 0; r < 1000; r++) for (Context c : ctxs) if (flags.isEnabled("big", c)) on++;
            assertTrue(on > 0);
        });
    }
}
