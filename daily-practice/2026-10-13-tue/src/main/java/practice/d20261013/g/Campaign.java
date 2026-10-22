package practice.d20261013.g;

import java.util.List;

/**
 * Aggregate root: the ONLY place that mutates device states, so the invariants live here. Thread-safe.
 * A device never seen before is QUEUED. stats() must be O(1) (keep counters, do not scan all devices on every event).
 */
public class Campaign {
    public Campaign(String id, List<Wave> waves, AbortPolicy abort) {
        // TODO
    }

    public String id() { throw new UnsupportedOperationException("TODO"); }

    public CampaignState state() { throw new UnsupportedOperationException("TODO"); }

    /** DRAFT -> RUNNING. From any other state: IllegalStateException. */
    public void start() { throw new UnsupportedOperationException("TODO"); }

    /**
     * Idempotent, forward-only. Returns true if the transition was applied; false for a duplicate (same state) or an illegal move.
     * After an applied event, while the campaign is RUNNING, ask the abort policy and move to ABORTED if it says so.
     * Events are still recorded after the campaign is ABORTED (late reports from devices).
     */
    public boolean onDeviceEvent(String deviceId, DeviceState to) { throw new UnsupportedOperationException("TODO"); }

    public DeviceState deviceState(String deviceId) { throw new UnsupportedOperationException("TODO"); }

    public Stats stats() { throw new UnsupportedOperationException("TODO"); }

    /** Ids (sorted ascending) of the devices currently in one of the given states. */
    public List<String> devicesIn(DeviceState... states) { throw new UnsupportedOperationException("TODO"); }
}
