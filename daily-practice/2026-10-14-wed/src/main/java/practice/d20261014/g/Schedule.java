package practice.d20261014.g;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/** G - immutable, versioned schedule. Pure functions of (rules, instant): no clock inside. */
public final class Schedule implements ChargingConstraint {

    /** Copy the list (later changes by the caller must not leak in). */
    public Schedule(long version, ZoneId zone, List<Window> windows) { throw new UnsupportedOperationException("TODO"); }

    public long version() { throw new UnsupportedOperationException("TODO"); }

    /** Is charging allowed at 'now'? Start inclusive, end exclusive; a window belongs to the day it starts on. */
    public boolean isAllowed(Instant now) { throw new UnsupportedOperationException("TODO"); }

    @Override public boolean allows(Instant t) { return isAllowed(t); }

    /** Next window start or end strictly after 'now', looking at most 8 days ahead; empty if there are no windows. */
    public Optional<Instant> nextBoundary(Instant now) { throw new UnsupportedOperationException("TODO"); }
}
