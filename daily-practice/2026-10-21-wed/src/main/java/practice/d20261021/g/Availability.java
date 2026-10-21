package practice.d20261021.g;

/** G - live status, written by the status pipeline and read by every search. Must be thread-safe. */
public final class Availability {

    public void update(String chargerId, Status status) {
        throw new UnsupportedOperationException("TODO");
    }

    /** A charger that never reported is OFFLINE (never offered as free). */
    public Status of(String chargerId) {
        throw new UnsupportedOperationException("TODO");
    }
}
