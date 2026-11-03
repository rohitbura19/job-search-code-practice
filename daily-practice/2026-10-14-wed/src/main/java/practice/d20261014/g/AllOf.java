package practice.d20261014.g;

import java.time.Instant;
import java.util.List;

/** G - Composite: allowed only if EVERY part allows (an empty list allows). */
public record AllOf(List<ChargingConstraint> parts) implements ChargingConstraint {
    @Override public boolean allows(Instant t) { throw new UnsupportedOperationException("TODO"); }
}
