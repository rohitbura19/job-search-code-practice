package practice.d20261014.b1;

import java.time.Instant;
import java.util.Set;

/** GIVEN - do not edit. */
public record Reading(String deviceId, String metric, double value, Instant ts, Set<String> tags) {}
