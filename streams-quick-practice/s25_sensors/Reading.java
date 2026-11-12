package s25_sensors;

/** Given. A sensor sits at one site; value is an integer measurement (e.g. tenths of a degree). */
public record Reading(String sensor, String site, int minute, int value) {}
