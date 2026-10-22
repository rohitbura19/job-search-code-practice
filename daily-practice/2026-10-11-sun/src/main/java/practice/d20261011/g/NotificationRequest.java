package practice.d20261011.g;

import java.util.Map;

/** GIVEN - do not edit. */
public record NotificationRequest(String eventId, String userId, String type,
                                  Map<String, String> params, Priority priority) {}
