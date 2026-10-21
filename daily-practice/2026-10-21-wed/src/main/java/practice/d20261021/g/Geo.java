package practice.d20261021.g;

/** GIVEN - great-circle distance. Use this exact function so your results match the tests. */
public final class Geo {
    private Geo() {}
    public static final double EARTH_RADIUS_KM = 6371.0088;

    public static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double p1 = Math.toRadians(lat1), p2 = Math.toRadians(lat2), dp = p2 - p1, dl = Math.toRadians(lon2 - lon1);
        double h = Math.sin(dp / 2) * Math.sin(dp / 2) + Math.cos(p1) * Math.cos(p2) * Math.sin(dl / 2) * Math.sin(dl / 2);
        return 2 * EARTH_RADIUS_KM * Math.asin(Math.min(1, Math.sqrt(h)));
    }
}
