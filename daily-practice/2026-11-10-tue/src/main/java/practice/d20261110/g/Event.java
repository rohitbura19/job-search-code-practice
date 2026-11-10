package practice.d20261110.g;

import java.util.Set;

/** Given. participants = organizer + attendees (organizer is always included). */
public record Event(String id, String title, TimeRange range, String organizer, Set<String> participants) {}
