package practice.d20261011.b1;

import java.util.List;
import java.util.Map;

/** B1 - see questions/B1-streams-top-chargers.md. Test: SessionStatsTest. Use streams and Collectors. */
public final class SessionStats {
    private SessionStats() {}

    /** The 3 chargerIds with the highest TOTAL kWh, highest first (fewer if there are fewer chargers). */
    public static List<String> top3Chargers(List<Session> sessions) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Average kWh per site, as a map whose iteration order is by site name. */
    public static Map<String, Double> avgKwhBySite(List<Session> sessions) {
        throw new UnsupportedOperationException("TODO");
    }

    /** The site with the most DISTINCT chargers; null for an empty list. */
    public static String siteWithMostChargers(List<Session> sessions) {
        throw new UnsupportedOperationException("TODO");
    }
}
