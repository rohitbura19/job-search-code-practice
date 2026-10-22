package practice.d20261022.g;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * maxPerWindow sends per (user, channel) in any sliding window of length window.
 * dedupeTtl: the same (user, dedupeKey) is a duplicate for this long after it was SENT.
 * Quiet hours are [quietStart, quietEnd) in the zone; the range may wrap midnight (22:00 to 07:00).
 */
public record GateConfig(int maxPerWindow, Duration window, Duration dedupeTtl,
                         LocalTime quietStart, LocalTime quietEnd, ZoneId zone) {}
