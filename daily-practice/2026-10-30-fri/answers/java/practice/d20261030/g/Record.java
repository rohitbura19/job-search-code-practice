package practice.d20261030.g;

/** Given. One message in one partition's log. */
public record Record(String topic, int partition, long offset, String key, String value) {}
