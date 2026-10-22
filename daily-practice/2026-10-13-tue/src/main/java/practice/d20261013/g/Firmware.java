package practice.d20261013.g;

import java.util.Set;

/** GIVEN. */
public record Firmware(String version, Set<String> compatibleHwRevs, String minFromVersion) {}
