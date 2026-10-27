package practice.e;

// TODO: make this a JPA embeddable record. Columns: fw_version (NOT NULL) and fw_channel (NOT NULL, stored as the enum NAME, not the ordinal).
//   Why not plain "version" / "channel" columns? Think about reserved words and about what else `Device` could embed later.
public record Firmware(String version, Channel channel) {}
