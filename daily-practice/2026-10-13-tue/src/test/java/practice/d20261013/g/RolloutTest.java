package practice.d20261013.g;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RolloutTest {

    // ---------- DeviceState ----------

    @Test @DisplayName("legal transitions")
    void legal() {
        assertTrue(DeviceState.QUEUED.canMoveTo(DeviceState.NOTIFIED));
        assertTrue(DeviceState.QUEUED.canMoveTo(DeviceState.SKIPPED));
        assertTrue(DeviceState.NOTIFIED.canMoveTo(DeviceState.DOWNLOADING));
        assertTrue(DeviceState.NOTIFIED.canMoveTo(DeviceState.FAILED));
        assertTrue(DeviceState.NOTIFIED.canMoveTo(DeviceState.TIMED_OUT));
        assertTrue(DeviceState.DOWNLOADING.canMoveTo(DeviceState.INSTALLING));
        assertTrue(DeviceState.DOWNLOADING.canMoveTo(DeviceState.FAILED));
        assertTrue(DeviceState.INSTALLING.canMoveTo(DeviceState.SUCCEEDED));
        assertTrue(DeviceState.INSTALLING.canMoveTo(DeviceState.TIMED_OUT));
    }

    @Test @DisplayName("table-driven: every pair of states is checked against the legal set")
    void allPairs() {
        Set<String> legal = Set.of("QUEUED>NOTIFIED", "QUEUED>SKIPPED", "NOTIFIED>DOWNLOADING", "NOTIFIED>FAILED", "NOTIFIED>TIMED_OUT",
            "DOWNLOADING>INSTALLING", "DOWNLOADING>FAILED", "DOWNLOADING>TIMED_OUT", "INSTALLING>SUCCEEDED", "INSTALLING>FAILED", "INSTALLING>TIMED_OUT");
        int checked = 0;
        for (DeviceState a : DeviceState.values()) for (DeviceState b : DeviceState.values()) {
            assertEquals(legal.contains(a + ">" + b), a.canMoveTo(b), a + " -> " + b);
            checked++;
        }
        assertEquals(64, checked);
    }

    @Test @DisplayName("terminal states")
    void terminal() {
        for (DeviceState s : DeviceState.values()) {
            boolean expected = s == DeviceState.SUCCEEDED || s == DeviceState.FAILED || s == DeviceState.TIMED_OUT || s == DeviceState.SKIPPED;
            assertEquals(expected, s.terminal(), s.name());
        }
    }

    // ---------- eligibility ----------

    static Device dev(String id, String hw, boolean charging) { return new Device(id, hw, "2.3", charging, "site-1"); }
    static final Firmware FW = new Firmware("2.5", Set.of("A", "B"), "2.3");

    @Test @DisplayName("HwRevRule and NotChargingRule")
    void rules() {
        assertTrue(new HwRevRule().test(dev("d1", "A", false), FW));
        assertFalse(new HwRevRule().test(dev("d2", "C", false), FW));
        assertTrue(new NotChargingRule().test(dev("d3", "A", false), FW));
        assertFalse(new NotChargingRule().test(dev("d4", "A", true), FW));
    }

    @Test @DisplayName("Eligibility.eligible keeps input order and applies ALL rules")
    void eligible() {
        List<Device> all = List.of(dev("d1", "A", false), dev("d2", "C", false), dev("d3", "B", true), dev("d4", "B", false));
        List<Device> ok = Eligibility.eligible(all, FW, List.of(new HwRevRule(), new NotChargingRule()));
        assertEquals(List.of("d1", "d4"), ok.stream().map(Device::id).toList());
        assertEquals(4, Eligibility.eligible(all, FW, List.of()).size());
        assertEquals(3, Eligibility.eligible(all, FW, List.of(new HwRevRule())).size());
    }

    // ---------- FailureRateAbort ----------

    @Test @DisplayName("abort policy: below the minimum sample never aborts")
    void belowMinSample() {
        AbortPolicy p = new FailureRateAbort(0.02, 200);
        assertFalse(p.shouldAbort(new Stats(3, 1, 2)), "1 failure out of 3 is 33% but too small a sample");
        assertFalse(p.shouldAbort(new Stats(199, 199, 0)));
    }

    @Test @DisplayName("abort policy: exactly at the threshold does not abort, above it does")
    void threshold() {
        AbortPolicy p = new FailureRateAbort(0.02, 100);
        assertFalse(p.shouldAbort(new Stats(100, 2, 98)), "2 of 100 = exactly 2%");
        assertTrue(p.shouldAbort(new Stats(100, 3, 97)), "3 of 100 = 3%");
        assertFalse(p.shouldAbort(new Stats(100, 0, 100)));
    }

    @Test @DisplayName("abort policy: only completed devices count in the denominator")
    void denominator() {
        AbortPolicy p = new FailureRateAbort(0.5, 4);
        assertFalse(p.shouldAbort(new Stats(1000, 2, 2)), "2 of 4 done = 50%, not above 50%");
        assertTrue(p.shouldAbort(new Stats(1000, 3, 1)));
    }

    // ---------- Campaign ----------

    static Campaign campaign(AbortPolicy p) { return new Campaign("c1", List.of(new Wave(0, 1, Duration.ofHours(4))), p); }
    static final AbortPolicy NEVER = s -> false;

    @Test @DisplayName("campaign lifecycle: DRAFT -> RUNNING, start twice fails")
    void lifecycle() {
        Campaign c = campaign(NEVER);
        assertEquals(CampaignState.DRAFT, c.state());
        assertEquals("c1", c.id());
        c.start();
        assertEquals(CampaignState.RUNNING, c.state());
        assertThrows(IllegalStateException.class, c::start);
    }

    @Test @DisplayName("a device never seen is QUEUED")
    void unknown() { assertEquals(DeviceState.QUEUED, campaign(NEVER).deviceState("ghost")); }

    @Test @DisplayName("forward moves are applied")
    void forward() {
        Campaign c = campaign(NEVER); c.start();
        assertTrue(c.onDeviceEvent("d1", DeviceState.NOTIFIED));
        assertTrue(c.onDeviceEvent("d1", DeviceState.DOWNLOADING));
        assertTrue(c.onDeviceEvent("d1", DeviceState.INSTALLING));
        assertTrue(c.onDeviceEvent("d1", DeviceState.SUCCEEDED));
        assertEquals(DeviceState.SUCCEEDED, c.deviceState("d1"));
    }

    @Test @DisplayName("idempotent: the same event twice has one effect, illegal and backward events are ignored")
    void idempotent() {
        Campaign c = campaign(NEVER); c.start();
        assertTrue(c.onDeviceEvent("d1", DeviceState.NOTIFIED));
        assertFalse(c.onDeviceEvent("d1", DeviceState.NOTIFIED), "duplicate");
        assertFalse(c.onDeviceEvent("d1", DeviceState.INSTALLING), "skips DOWNLOADING");
        assertFalse(c.onDeviceEvent("d1", DeviceState.QUEUED), "backward");
        assertTrue(c.onDeviceEvent("d1", DeviceState.DOWNLOADING));
        assertFalse(c.onDeviceEvent("d1", DeviceState.NOTIFIED), "out-of-order late message");
        assertEquals(DeviceState.DOWNLOADING, c.deviceState("d1"));
        assertEquals(new Stats(1, 0, 0), c.stats());
    }

    @Test @DisplayName("terminal states are final")
    void terminalFinal() {
        Campaign c = campaign(NEVER); c.start();
        c.onDeviceEvent("d1", DeviceState.NOTIFIED); c.onDeviceEvent("d1", DeviceState.FAILED);
        assertFalse(c.onDeviceEvent("d1", DeviceState.DOWNLOADING));
        assertFalse(c.onDeviceEvent("d1", DeviceState.SUCCEEDED));
        assertEquals(DeviceState.FAILED, c.deviceState("d1"));
    }

    static void walkTo(Campaign c, String id, DeviceState... path) { for (DeviceState s : path) assertTrue(c.onDeviceEvent(id, s), id + " -> " + s); }

    @Test @DisplayName("stats: attempted / failed (FAILED + TIMED_OUT) / succeeded")
    void stats() {
        Campaign c = campaign(NEVER); c.start();
        walkTo(c, "ok", DeviceState.NOTIFIED, DeviceState.DOWNLOADING, DeviceState.INSTALLING, DeviceState.SUCCEEDED);
        walkTo(c, "bad", DeviceState.NOTIFIED, DeviceState.FAILED);
        walkTo(c, "slow", DeviceState.NOTIFIED, DeviceState.TIMED_OUT);
        walkTo(c, "mid", DeviceState.NOTIFIED, DeviceState.DOWNLOADING);
        assertEquals(new Stats(4, 2, 1), c.stats());
    }

    @Test @DisplayName("three failures with minSample 3 flip the campaign to ABORTED")
    void abortsOnThreeFailures() {
        Campaign c = campaign(new FailureRateAbort(0.5, 3)); c.start();
        for (String id : List.of("d1", "d2")) walkTo(c, id, DeviceState.NOTIFIED, DeviceState.FAILED);
        assertEquals(CampaignState.RUNNING, c.state(), "only 2 completed devices so far");
        walkTo(c, "d3", DeviceState.NOTIFIED, DeviceState.FAILED);
        assertEquals(CampaignState.ABORTED, c.state());
    }

    @Test @DisplayName("a campaign that is not RUNNING is never aborted by events; late events are still recorded")
    void onlyRunningAborts() {
        Campaign draft = campaign(new FailureRateAbort(0.0, 1));
        walkTo(draft, "d1", DeviceState.NOTIFIED, DeviceState.FAILED);
        assertEquals(CampaignState.DRAFT, draft.state());

        Campaign c = campaign(new FailureRateAbort(0.0, 1)); c.start();
        walkTo(c, "d1", DeviceState.NOTIFIED, DeviceState.FAILED);
        assertEquals(CampaignState.ABORTED, c.state());
        assertTrue(c.onDeviceEvent("late", DeviceState.NOTIFIED), "still recorded");
        assertEquals(CampaignState.ABORTED, c.state());
    }

    @Test @DisplayName("devicesIn returns sorted ids in the requested states")
    void devicesIn() {
        Campaign c = campaign(NEVER); c.start();
        walkTo(c, "b", DeviceState.NOTIFIED);
        walkTo(c, "a", DeviceState.NOTIFIED, DeviceState.DOWNLOADING);
        walkTo(c, "c", DeviceState.NOTIFIED);
        walkTo(c, "z", DeviceState.NOTIFIED, DeviceState.FAILED);
        assertEquals(List.of("b", "c"), c.devicesIn(DeviceState.NOTIFIED));
        assertEquals(List.of("a", "b", "c"), c.devicesIn(DeviceState.NOTIFIED, DeviceState.DOWNLOADING));
        assertEquals(List.of(), c.devicesIn(DeviceState.SUCCEEDED));
    }

    @Test @DisplayName("100 threads report the same transition for one device: exactly one is applied")
    void concurrent() throws Exception {
        Campaign c = campaign(NEVER); c.start();
        ExecutorService ex = Executors.newFixedThreadPool(16);
        AtomicInteger applied = new AtomicInteger();
        CountDownLatch go = new CountDownLatch(1);
        List<java.util.concurrent.Future<?>> fs = new ArrayList<>();
        for (int i = 0; i < 100; i++) fs.add(ex.submit(() -> {
            go.await();
            if (c.onDeviceEvent("d1", DeviceState.NOTIFIED)) applied.incrementAndGet();
            return null;
        }));
        go.countDown();
        for (var f : fs) f.get(10, TimeUnit.SECONDS);
        ex.shutdown();
        assertEquals(1, applied.get());
        assertEquals(new Stats(1, 0, 0), c.stats());
    }

    @Test @DisplayName("random event sequences never reach an illegal state and stats stay consistent")
    void randomSequences() {
        Random rnd = new Random(13);
        DeviceState[] all = DeviceState.values();
        Campaign c = campaign(NEVER); c.start();
        for (int i = 0; i < 20_000; i++) {
            String id = "d" + rnd.nextInt(50);
            DeviceState before = c.deviceState(id), to = all[rnd.nextInt(all.length)];
            boolean applied = c.onDeviceEvent(id, to);
            assertEquals(before.canMoveTo(to), applied);
            assertEquals(applied ? to : before, c.deviceState(id));
        }
        int failed = 0, ok = 0, att = 0;
        for (int i = 0; i < 50; i++) {
            DeviceState s = c.deviceState("d" + i);
            if (s != DeviceState.QUEUED) att++;
            if (s == DeviceState.FAILED || s == DeviceState.TIMED_OUT) failed++;
            if (s == DeviceState.SUCCEEDED) ok++;
        }
        assertEquals(new Stats(att, failed, ok), c.stats());
    }

    @Test @DisplayName("200,000 devices: stats() must be O(1), not a scan of every device on every event")
    void scales() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            Campaign c = campaign(NEVER); c.start();
            for (int i = 0; i < 200_000; i++) c.onDeviceEvent("d" + i, DeviceState.NOTIFIED);
            assertEquals(new Stats(200_000, 0, 0), c.stats());
        });
    }

    // ---------- StatusConsumer ----------

    static final class FakeGateway implements DeviceGateway {
        final List<String> calls = new ArrayList<>();
        @Override public void notifyUpdate(String deviceId, String jobId, String firmwareUrl) { calls.add("notify " + deviceId); }
        @Override public void cancelUpdate(String deviceId, String jobId) { calls.add("cancel " + deviceId + " " + jobId); }
    }

    @Test @DisplayName("FAILED report: duplicates are ignored and nothing is cancelled while the campaign keeps running")
    void consumerNoAbort() {
        Campaign c = campaign(new FailureRateAbort(0.5, 3)); c.start();
        FakeGateway gw = new FakeGateway();
        StatusConsumer sc = new StatusConsumer(c, gw);
        assertTrue(sc.handle("d1", DeviceState.NOTIFIED));
        assertTrue(sc.handle("d1", DeviceState.FAILED));
        assertFalse(sc.handle("d1", DeviceState.FAILED), "SQS delivered it twice");
        assertTrue(gw.calls.isEmpty());
        assertEquals(CampaignState.RUNNING, c.state());
    }

    @Test @DisplayName("the report that aborts the campaign cancels NOTIFIED and DOWNLOADING devices once, in id order")
    void consumerAborts() {
        Campaign c = campaign(new FailureRateAbort(0.5, 3)); c.start();
        FakeGateway gw = new FakeGateway();
        StatusConsumer sc = new StatusConsumer(c, gw);
        sc.handle("d5", DeviceState.NOTIFIED);
        sc.handle("d4", DeviceState.NOTIFIED); sc.handle("d4", DeviceState.DOWNLOADING);
        sc.handle("d6", DeviceState.NOTIFIED); sc.handle("d6", DeviceState.DOWNLOADING); sc.handle("d6", DeviceState.INSTALLING);
        for (String id : List.of("d1", "d2", "d3")) { sc.handle(id, DeviceState.NOTIFIED); sc.handle(id, DeviceState.FAILED); }
        assertEquals(CampaignState.ABORTED, c.state());
        assertEquals(List.of("cancel d4 c1:d4", "cancel d5 c1:d5"), gw.calls);   // d6 is already INSTALLING: not cancelled

        sc.handle("d7", DeviceState.NOTIFIED); sc.handle("d7", DeviceState.FAILED);   // late events after the abort
        assertEquals(2, gw.calls.size(), "no second round of cancels");
    }
}
