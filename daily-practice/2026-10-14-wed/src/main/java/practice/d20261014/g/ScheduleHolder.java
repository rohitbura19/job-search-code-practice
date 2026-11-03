package practice.d20261014.g;

/** G - what the charger keeps: one active schedule, replaced atomically, late or duplicate deliveries ignored. */
public final class ScheduleHolder {

    /** Null until the first schedule is accepted. */
    public Schedule current() { throw new UnsupportedOperationException("TODO"); }

    /** Accept only a strictly newer version than the current one. Returns true if it was accepted. Thread-safe. */
    public boolean offer(Schedule incoming) { throw new UnsupportedOperationException("TODO"); }
}
