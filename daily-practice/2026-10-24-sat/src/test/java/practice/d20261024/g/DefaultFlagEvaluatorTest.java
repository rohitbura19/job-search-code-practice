package practice.d20261024.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultFlagEvaluatorTest {
    FlagStore store;
    FlagEvaluator ev;

    @BeforeEach void setUp() {
        store = new InMemoryFlagStore();
        ev = new DefaultFlagEvaluator(store);
    }

    static Set<String> enabledUsers(FlagEvaluator ev, String key, int users) {
        return IntStream.range(0, users).mapToObj(i -> "user-" + i).filter(u -> ev.isEnabled(key, u)).collect(Collectors.toSet());
    }

    @Test void storeFindsWhatWasPutAndReplaces() {
        assertTrue(store.find("x").isEmpty());
        store.put(Flag.on("x"));
        assertEquals(100, store.find("x").orElseThrow().rolloutPercent());
        store.put(Flag.rollout("x", 10));
        assertEquals(10, store.find("x").orElseThrow().rolloutPercent());
    }

    @Test void flagValidatesAndIsImmutable() {
        assertThrows(IllegalArgumentException.class, () -> new Flag(" ", true, 10, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> new Flag(null, true, 10, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> new Flag("k", true, -1, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> new Flag("k", true, 101, Set.of()));
        Set<String> mutable = new HashSet<>(Set.of("a"));
        Flag f = new Flag("k", true, 0, mutable);
        mutable.add("b");
        assertEquals(Set.of("a"), f.allowList(), "defensive copy");
        assertThrows(UnsupportedOperationException.class, () -> f.allowList().add("c"));
    }

    @Test void unknownFlagIsOff() {
        assertFalse(ev.isEnabled("missing", "u1"));
    }

    @Test void zeroAndHundredPercent() {
        store.put(Flag.rollout("none", 0));
        store.put(Flag.on("all"));
        assertEquals(Set.of(), enabledUsers(ev, "none", 2000));
        assertEquals(2000, enabledUsers(ev, "all", 2000).size());
    }

    @Test void killSwitchBeatsAllowListAndRollout() {
        store.put(new Flag("k", false, 100, Set.of("vip")));
        assertFalse(ev.isEnabled("k", "vip"));
        assertFalse(ev.isEnabled("k", "u1"));
    }

    @Test void allowListWinsAtZeroPercent() {
        store.put(new Flag("k", true, 0, Set.of("vip")));
        assertTrue(ev.isEnabled("k", "vip"));
        assertFalse(ev.isEnabled("k", "u1"));
    }

    @Test void decisionIsStickyAndMonotonicInThePercentage() {
        store.put(Flag.rollout("k", 20));
        Set<String> at20 = enabledUsers(ev, "k", 5000);
        assertEquals(at20, enabledUsers(ev, "k", 5000), "same user, same answer");
        store.put(Flag.rollout("k", 50));
        Set<String> at50 = enabledUsers(ev, "k", 5000);
        assertTrue(at50.containsAll(at20), "raising the percentage never switches anybody off");
        store.put(Flag.rollout("k", 100));
        assertEquals(5000, enabledUsers(ev, "k", 5000).size());
    }

    @Test void splitIsRoughlyProportional() {
        store.put(Flag.rollout("k", 30));
        int on = enabledUsers(ev, "k", 20_000).size();
        assertTrue(on > 5_400 && on < 6_600, "30% of 20000, got " + on);
    }

    @Test void flagsBucketIndependently() {
        store.put(Flag.rollout("a", 50));
        store.put(Flag.rollout("b", 50));
        assertNotEquals(enabledUsers(ev, "a", 2000), enabledUsers(ev, "b", 2000));
    }

    @Test void badUserIdsAreRejected() {
        store.put(Flag.on("k"));
        assertThrows(IllegalArgumentException.class, () -> ev.isEnabled("k", null));
        assertThrows(IllegalArgumentException.class, () -> ev.isEnabled("k", " "));
        assertThrows(IllegalArgumentException.class, () -> ev.isEnabled("missing", null), "validated before the lookup");
    }

    @Test void concurrentUpdatesNeverProduceAMixedDefinition() throws Exception {
        store.put(new Flag("k", true, 100, Set.of()));
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            Future<?> writer = pool.submit(() -> {
                for (int i = 0; i < 50_000; i++) store.put(i % 2 == 0 ? new Flag("k", false, 100, Set.of()) : new Flag("k", true, 100, Set.of()));
            });
            List<Future<Boolean>> readers = new ArrayList<>();
            for (int t = 0; t < 3; t++) readers.add(pool.submit(() -> {
                for (int i = 0; i < 50_000; i++) ev.isEnabled("k", "u" + i);
                return true;
            }));
            writer.get(10, TimeUnit.SECONDS);
            for (Future<Boolean> f : readers) assertTrue(f.get(10, TimeUnit.SECONDS));
        } finally {
            pool.shutdownNow();
        }
    }

    @Test void largeEvaluationIsFast() {
        store.put(Flag.rollout("k", 25));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            int n = 0;
            for (int i = 0; i < 1_000_000; i++) if (ev.isEnabled("k", "user-" + i)) n++;
            assertTrue(n > 200_000 && n < 300_000);
        });
    }
}
