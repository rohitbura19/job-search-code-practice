package practice.e;

import java.time.Instant;

// TODO: Bean Validation: stationId not blank, max 40; startsAt required and in the future; capacity required 1..50.
//       Which type must capacity be (int or Integer) for a MISSING value to be reported instead of silently becoming 0?
public record CreateSlotRequest(String stationId, Instant startsAt, int capacity) {}
