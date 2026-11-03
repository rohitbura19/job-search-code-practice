package practice.d20261014.g;

import java.time.Instant;

/** GIVEN - do not edit. Specification pattern: one reason charging may or may not be allowed. */
public interface ChargingConstraint {
    boolean allows(Instant t);
}
