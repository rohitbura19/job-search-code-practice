package practice.d20261022.g;

/** dedupeKey may be null (never a duplicate). urgent notifications ignore quiet hours only. */
public record Notification(String userId, Channel channel, String dedupeKey, boolean urgent) {}
