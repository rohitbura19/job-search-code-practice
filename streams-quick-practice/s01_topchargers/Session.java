package s01_topchargers;

import java.time.Instant;

/** GIVEN - do not edit. */
public record Session(String chargerId, String siteId, double kwh, Instant start) {}
