package practice.d20261021.g;

/** GIVEN - static catalogue data for one charger (immutable). */
public record Charger(String id, double lat, double lon, int connectors) {
    public Charger {
        if (Double.isNaN(lat) || Double.isNaN(lon) || lat < -90 || lat > 90 || lon < -180 || lon > 180)
            throw new IllegalArgumentException("bad coordinates for " + id);
    }
}
