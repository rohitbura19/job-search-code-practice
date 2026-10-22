package practice.d20261013.g;

/**
 * Per-device state machine. The constants are GIVEN; implement the two methods.
 * Legal moves (forward only):
 *   QUEUED -> NOTIFIED, SKIPPED
 *   NOTIFIED -> DOWNLOADING, FAILED, TIMED_OUT
 *   DOWNLOADING -> INSTALLING, FAILED, TIMED_OUT
 *   INSTALLING -> SUCCEEDED, FAILED, TIMED_OUT
 * SUCCEEDED, FAILED, TIMED_OUT, SKIPPED are terminal. Nothing ever moves back to QUEUED, and a state never moves to itself.
 */
public enum DeviceState {
    QUEUED, NOTIFIED, DOWNLOADING, INSTALLING, SUCCEEDED, FAILED, TIMED_OUT, SKIPPED;

    public boolean canMoveTo(DeviceState target) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean terminal() {
        throw new UnsupportedOperationException("TODO");
    }
}
