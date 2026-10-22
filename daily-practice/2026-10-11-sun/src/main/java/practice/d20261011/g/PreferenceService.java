package practice.d20261011.g;

import java.time.Instant;
import java.util.Set;

/** GIVEN - do not edit. */
public interface PreferenceService {
    Set<Channel> allowedChannels(String userId, String type);
    boolean inQuietHours(String userId, Instant now);
}
