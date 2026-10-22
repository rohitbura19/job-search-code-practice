# Answers: G-firmware-rollout-lld

(Spoilers. Open only after your attempt.)

**Reference** (the core below was compiled and run: three failures with `minSample = 3` flips the campaign to `ABORTED`)
```java
enum CampaignState { DRAFT, RUNNING, PAUSED, COMPLETED, ABORTED }

enum DeviceState {
    QUEUED, NOTIFIED, DOWNLOADING, INSTALLING, SUCCEEDED, FAILED, TIMED_OUT, SKIPPED;
    private static final Map<DeviceState, Set<DeviceState>> NEXT = Map.of(
        QUEUED,      EnumSet.of(NOTIFIED, SKIPPED),
        NOTIFIED,    EnumSet.of(DOWNLOADING, FAILED, TIMED_OUT),
        DOWNLOADING, EnumSet.of(INSTALLING, FAILED, TIMED_OUT),
        INSTALLING,  EnumSet.of(SUCCEEDED, FAILED, TIMED_OUT));
    boolean canMoveTo(DeviceState t) { return NEXT.getOrDefault(this, Set.of()).contains(t); }
    boolean terminal() { return !NEXT.containsKey(this); }
}

record Device(String id, String hwRev, String fwVersion, boolean charging, String siteId) {}
record Firmware(String version, Set<String> compatibleHwRevs, String minFromVersion) {}
record Wave(int index, int percent, Duration soak) {}
record Stats(int attempted, int failed, int succeeded) {}

interface EligibilityRule { boolean test(Device d, Firmware fw); }          // Chain / Specification: all must pass
class HwRevRule implements EligibilityRule { public boolean test(Device d, Firmware fw) { return fw.compatibleHwRevs().contains(d.hwRev()); } }
class NotChargingRule implements EligibilityRule { public boolean test(Device d, Firmware fw) { return !d.charging(); } }

interface WaveSelector { List<Device> select(Wave wave, List<Device> eligible); }  // Strategy: random %, by region, by hw rev
interface AbortPolicy { boolean shouldAbort(Stats s); }                            // Strategy
class FailureRateAbort implements AbortPolicy {
    private final double maxRate; private final int minSample;
    FailureRateAbort(double maxRate, int minSample) { this.maxRate = maxRate; this.minSample = minSample; }
    public boolean shouldAbort(Stats s) {
        int done = s.failed() + s.succeeded();
        return done >= minSample && (double) s.failed() / done > maxRate;           // minimum sample avoids 1-of-3 aborts
    }
}
interface DeviceGateway { void notifyUpdate(String deviceId, String jobId, String firmwareUrl); }   // MQTT behind an interface

class Campaign {                                                                    // aggregate root
    private final String id; private final List<Wave> waves; private final AbortPolicy abort;
    private CampaignState state = CampaignState.DRAFT;
    private final Map<String, DeviceState> devices = new HashMap<>();
    Campaign(String id, List<Wave> waves, AbortPolicy abort) { this.id = id; this.waves = List.copyOf(waves); this.abort = abort; }

    synchronized void start() {
        if (state != CampaignState.DRAFT) throw new IllegalStateException("cannot start from " + state);
        state = CampaignState.RUNNING;
    }
    synchronized boolean onDeviceEvent(String deviceId, DeviceState to) {          // idempotent, forward-only
        DeviceState cur = devices.getOrDefault(deviceId, DeviceState.QUEUED);
        if (cur == to || !cur.canMoveTo(to)) return false;                          // duplicate or illegal -> ignore
        devices.put(deviceId, to);
        if (state == CampaignState.RUNNING && abort.shouldAbort(stats())) state = CampaignState.ABORTED;
        return true;
    }
    synchronized Stats stats() {
        int f = 0, ok = 0, att = 0;
        for (DeviceState d : devices.values()) {
            if (d != DeviceState.QUEUED) att++;
            if (d == DeviceState.FAILED || d == DeviceState.TIMED_OUT) f++;
            if (d == DeviceState.SUCCEEDED) ok++;
        }
        return new Stats(att, f, ok);
    }
}
```
Kit note: the tests require `stats()` to be O(1) (keep three counters, update them on each applied transition) and add `cancelUpdate(deviceId, jobId)` to `DeviceGateway`, accessors on `Campaign` (`id()`, `state()`, `deviceState(id)`, `devicesIn(states...)`) and the `StatusConsumer` class below.

Flow "charger reports FAILED": MQTT -> IoT rule -> SQS -> `StatusConsumer` loads the campaign, calls `campaign.onDeviceEvent(id, FAILED)`; if it returns true, persist the transition (conditional write: `WHERE state = :expected`), publish `DeviceFailed`; `WaveEvaluator` checks `abort.shouldAbort(stats)`; on ABORTED the `RolloutScheduler` stops sending new notifications and a `CancelJob` command is sent to devices still in NOTIFIED/DOWNLOADING through `DeviceGateway`.

Patterns: State (device and campaign lifecycle), Strategy (`WaveSelector`, `AbortPolicy`), Specification/Chain (eligibility rules), Command (a job sent to a device, with an id, retryable and cancellable), Observer (events to audit log and metrics), Adapter (`DeviceGateway` hides AWS IoT), Aggregate root (`Campaign` is the only thing that mutates device states, so invariants live in one place).

Concurrency points: status events from thousands of devices arrive on many consumer threads. The `synchronized` aggregate above is fine in one JVM, but with several pods the real guard is the database: conditional update (`UPDATE ... SET state=:to WHERE device=:d AND state=:from`) or DynamoDB `ConditionExpression`, and optimistic `@Version` on the campaign row. SQS is at-least-once and unordered (standard queue), so the forward-only state machine is what makes the consumer idempotent.

Test plan: table-driven test of every legal and illegal state transition; `FailureRateAbort` boundary tests (exactly at threshold, below the minimum sample); fake `DeviceGateway` that records calls; inject a `Clock` for soak-time logic; replay the same event twice and assert one effect; concurrency test with 100 threads sending events for the same device, assert a single terminal state; property test: random event sequences never reach an illegal state.

Principal-level changes to discuss: move from an in-memory aggregate to an event-sourced campaign so the audit trail and replays are free; split `Campaign` (policy) from `DeviceRollout` (per device) because a campaign with 500k devices cannot be one object in memory; keep counters as sharded counters rather than scanning `devices.values()` on every event (the loop above is O(n) per event, which is O(n²) overall: first thing the interviewer will point at); make `stats()` incremental (update counters on each transition).

`StatusConsumer` as used by the tests (sketch):
```java
boolean handle(String deviceId, DeviceState to) {
    CampaignState before = campaign.state();
    boolean applied = campaign.onDeviceEvent(deviceId, to);
    if (applied && before != CampaignState.ABORTED && campaign.state() == CampaignState.ABORTED)
        for (String d : campaign.devicesIn(DeviceState.NOTIFIED, DeviceState.DOWNLOADING))
            gateway.cancelUpdate(d, campaign.id() + ":" + d);
    return applied;
}
```
