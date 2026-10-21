package practice.d20261021.g;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class GeoIndexTest {

    static final Predicate<Charger> ALL = c -> true;
    static List<String> ids(Result r) { return r.hits().stream().map(h -> h.charger().id()).toList(); }

    // a few chargers around central Bangalore
    static final List<Charger> BLR = List.of(
            new Charger("mg-road", 12.9756, 77.6050, 2),
            new Charger("indiranagar", 12.9784, 77.6408, 4),
            new Charger("koramangala", 12.9352, 77.6245, 1),
            new Charger("whitefield", 12.9698, 77.7500, 2),
            new Charger("airport", 13.1986, 77.7066, 6));
    static final double LAT = 12.9716, LON = 77.5946;   // the search point (city centre)

    @Test @DisplayName("closest first, only inside the radius")
    void closestFirst() {
        GeoIndex idx = new GeoIndex(BLR, 0.05);
        assertEquals(List.of("mg-road", "indiranagar", "koramangala"), ids(idx.nearby(LAT, LON, 6, 10, ALL)));
    }

    @Test @DisplayName("k limits the result")
    void kLimit() {
        GeoIndex idx = new GeoIndex(BLR, 0.05);
        assertEquals(List.of("mg-road", "indiranagar"), ids(idx.nearby(LAT, LON, 50, 2, ALL)));
        assertEquals(5, idx.nearby(LAT, LON, 50, 10, ALL).hits().size());
    }

    @Test @DisplayName("k = 0 or a negative radius returns nothing")
    void degenerate() {
        GeoIndex idx = new GeoIndex(BLR, 0.05);
        assertTrue(idx.nearby(LAT, LON, 10, 0, ALL).hits().isEmpty());
        assertTrue(idx.nearby(LAT, LON, -1, 5, ALL).hits().isEmpty());
        assertTrue(new GeoIndex(List.of(), 0.05).nearby(LAT, LON, 10, 5, ALL).hits().isEmpty());
    }

    @Test @DisplayName("distances are reported and sorted ascending")
    void distances() {
        Result r = new GeoIndex(BLR, 0.05).nearby(LAT, LON, 50, 10, ALL);
        for (int i = 1; i < r.hits().size(); i++) assertTrue(r.hits().get(i - 1).km() <= r.hits().get(i).km());
        assertEquals(Geo.haversineKm(LAT, LON, 12.9756, 77.6050), r.hits().get(0).km(), 1e-9);
    }

    @Test @DisplayName("equal distances are ordered by id")
    void tiesById() {
        List<Charger> cs = List.of(new Charger("b", 10.01, 20, 1), new Charger("a", 9.99, 20, 1));
        assertEquals(List.of("a", "b"), ids(new GeoIndex(cs, 0.05).nearby(10, 20, 5, 5, ALL)));
    }

    @Test @DisplayName("search wraps around the antimeridian (lon 180 / -180)")
    void antimeridian() {
        List<Charger> cs = List.of(new Charger("W", 0, -179.99, 1), new Charger("E", 0, 179.98, 1), new Charger("far", 0, 170, 1));
        assertEquals(List.of("E", "W"), ids(new GeoIndex(cs, 0.05).nearby(0, 179.99, 5, 5, ALL)));
    }

    @Test @DisplayName("search near the north pole considers every longitude")
    void nearPole() {
        List<Charger> cs = List.of(new Charger("p1", 89.95, 0, 1), new Charger("p2", 89.9, 180, 1), new Charger("p3", 89.85, -90, 1), new Charger("south", 80, 0, 1));
        assertEquals(List.of("p1", "p2", "p3"), ids(new GeoIndex(cs, 0.05).nearby(90, 0, 50, 10, ALL)));
    }

    @Test @DisplayName("high latitude: the longitude window must be wider than radius/111 km")
    void highLatitudeWindow() {
        // at 60N, 10 km east is about 0.18 degrees of longitude; a naive 0.09 degree window misses it
        List<Charger> cs = List.of(new Charger("east", 60, 10.16, 1));
        assertEquals(List.of("east"), ids(new GeoIndex(cs, 0.05).nearby(60, 10, 10, 5, ALL)));
    }

    @Test @DisplayName("ChargerFinder returns only FREE chargers with enough connectors; unknown status is not free")
    void finderFilters() {
        Availability live = new Availability();
        live.update("mg-road", Status.BUSY);
        live.update("indiranagar", Status.FREE);
        live.update("koramangala", Status.FREE);
        // whitefield and airport never reported: must not be offered
        ChargerFinder f = new ChargerFinder(new GeoIndex(BLR, 0.05), live);
        assertEquals(List.of("indiranagar", "koramangala"), ids(f.findFree(LAT, LON, 50, 10, 1)));
        assertEquals(List.of("indiranagar"), ids(f.findFree(LAT, LON, 50, 10, 2)));
        live.update("indiranagar", Status.FAULTED);
        assertEquals(List.of("koramangala"), ids(f.findFree(LAT, LON, 50, 10, 1)));
    }

    @Test @DisplayName("a small radius visits only a few cells (no full scan)")
    void smallRadiusIsCheap() {
        Result r = new GeoIndex(BLR, 0.05).nearby(LAT, LON, 3, 5, ALL);
        assertTrue(r.cellsVisited() <= 16, "visited " + r.cellsVisited());
    }

    @Test @DisplayName("1,500 random queries match a brute-force scan")
    void randomAgainstBrute() {
        Random rnd = new Random(5); List<Charger> all = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            boolean city = rnd.nextBoolean();
            double lat = city ? 12.8 + rnd.nextDouble() * 0.4 : rnd.nextDouble() * 180 - 90;
            double lon = city ? 77.4 + rnd.nextDouble() * 0.4 : rnd.nextDouble() * 360 - 180;
            all.add(new Charger("c" + i, lat, lon, 1 + rnd.nextInt(4)));
        }
        for (int i = 0; i < 200; i++) all.add(new Charger("am" + i, rnd.nextDouble() * 4 - 2, rnd.nextBoolean() ? 179.9 + rnd.nextDouble() * 0.1 : -180 + rnd.nextDouble() * 0.1, 2));
        for (int i = 0; i < 200; i++) all.add(new Charger("np" + i, 89 + rnd.nextDouble(), rnd.nextDouble() * 360 - 180, 2));
        GeoIndex idx = new GeoIndex(all, 0.05);
        for (int t = 0; t < 1500; t++) {
            int mode = rnd.nextInt(4); double lat, lon;
            if (mode == 0) { lat = 12.8 + rnd.nextDouble() * 0.4; lon = 77.4 + rnd.nextDouble() * 0.4; }
            else if (mode == 1) { lat = rnd.nextDouble() * 4 - 2; lon = rnd.nextBoolean() ? 179.8 + rnd.nextDouble() * 0.2 : -180 + rnd.nextDouble() * 0.2; }
            else if (mode == 2) { lat = 88 + rnd.nextDouble() * 2; lon = rnd.nextDouble() * 360 - 180; }
            else { lat = rnd.nextDouble() * 180 - 90; lon = rnd.nextDouble() * 360 - 180; }
            double radius = new double[]{0.5, 2, 10, 50, 300}[rnd.nextInt(5)]; int k = 1 + rnd.nextInt(8); int minConn = 1 + rnd.nextInt(3);
            Predicate<Charger> f = c -> c.connectors() >= minConn;
            assertEquals(brute(all, lat, lon, radius, k, f), ids(idx.nearby(lat, lon, radius, k, f)), "query " + lat + "," + lon + " r=" + radius);
        }
    }

    static List<String> brute(List<Charger> all, double lat, double lon, double r, int k, Predicate<Charger> f) {
        List<Hit> hs = new ArrayList<>();
        for (Charger c : all) if (f.test(c)) { double d = Geo.haversineKm(lat, lon, c.lat(), c.lon()); if (d <= r) hs.add(new Hit(c, d)); }
        hs.sort(Comparator.comparingDouble(Hit::km).thenComparing(h -> h.charger().id()));
        return hs.stream().limit(k).map(h -> h.charger().id()).toList();
    }
}
