# Answers: G-geo-index-lld

(Spoilers. Open only after your attempt.)

**Reference** (compiled and run; every output line below is real)
```java
record Charger(String id, double lat, double lon, int connectors) {
    Charger { if (lat < -90 || lat > 90 || lon < -180 || lon > 180 || Double.isNaN(lat) || Double.isNaN(lon)) throw new IllegalArgumentException("bad coordinates for " + id); }
}
enum Status { FREE, BUSY, FAULTED, OFFLINE }
record Hit(Charger charger, double km) {}
record Result(List<Hit> hits, int cellsVisited, int examined) {}
static final double R = 6371.0088;                                    // mean Earth radius in km

static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
    double p1 = Math.toRadians(lat1), p2 = Math.toRadians(lat2), dp = p2 - p1, dl = Math.toRadians(lon2 - lon1);
    double h = Math.sin(dp / 2) * Math.sin(dp / 2) + Math.cos(p1) * Math.cos(p2) * Math.sin(dl / 2) * Math.sin(dl / 2);
    return 2 * R * Math.asin(Math.min(1, Math.sqrt(h)));              // min(1, ..) guards rounding just above 1 for antipodal points
}

static final class GeoIndex {                                         // immutable after construction: share it, swap it atomically on a catalogue reload
    private final double cell; private final int rows, cols; private final Map<Long, List<Charger>> cells;
    GeoIndex(Collection<Charger> chargers, double cellDeg) {
        this.cell = cellDeg; this.rows = (int) Math.ceil(180 / cellDeg); this.cols = (int) Math.ceil(360 / cellDeg);
        Map<Long, List<Charger>> m = new HashMap<>();
        for (Charger c : chargers) m.computeIfAbsent(key(row(c.lat()), col(c.lon())), k -> new ArrayList<>()).add(c);
        m.replaceAll((k, v) -> List.copyOf(v)); this.cells = Map.copyOf(m);
    }
    private int row(double lat) { return Math.min(rows - 1, Math.max(0, (int) Math.floor((lat + 90) / cell))); }
    private int col(double lon) { return Math.floorMod((int) Math.floor((lon + 180) / cell), cols); }      // -180 and 180 are the same meridian
    private long key(int r, int c) { return (long) r * cols + c; }

    Result nearby(double lat, double lon, double radiusKm, int k, Predicate<Charger> filter) {
        if (k <= 0 || radiusKm < 0) return new Result(List.of(), 0, 0);
        double ang = radiusKm / R;                                    // radius as an angle (radians)
        int r0 = row(lat - Math.toDegrees(ang)), r1 = row(lat + Math.toDegrees(ang));
        double cosLat = Math.cos(Math.toRadians(lat)); boolean allCols = false; int c0 = 0, nCols = 0;
        if (ang >= Math.PI / 2 || Math.sin(ang) >= cosLat) allCols = true;                     // the circle contains a pole: every meridian is possible
        else {
            double dLon = Math.toDegrees(Math.asin(Math.sin(ang) / cosLat));                   // exact longitude half-width of a circle on a sphere
            int a = (int) Math.floor((lon - dLon + 180) / cell), b = (int) Math.floor((lon + dLon + 180) / cell);
            if (b - a + 1 >= cols) allCols = true; else { c0 = a; nCols = b - a + 1; }
        }
        if (allCols) { c0 = 0; nCols = cols; }
        PriorityQueue<Hit> heap = new PriorityQueue<>(Comparator.comparingDouble(Hit::km).thenComparing(h -> h.charger().id()).reversed());   // WORST kept hit on top
        int visited = 0, examined = 0;
        for (int r = r0; r <= r1; r++) for (int i = 0; i < nCols; i++) {
            List<Charger> list = cells.get(key(r, Math.floorMod(c0 + i, cols))); visited++;      // floorMod: the column range may wrap past 0 or past cols
            if (list == null) continue;
            for (Charger c : list) {
                examined++;
                if (!filter.test(c)) continue;                        // cheap predicates first, the trigonometry only for survivors
                double d = haversineKm(lat, lon, c.lat(), c.lon());
                if (d > radiusKm) continue;                           // the cells are a bounding box; the exact distance decides
                Hit h = new Hit(c, d);
                if (heap.size() < k) heap.add(h); else if (heap.comparator().compare(h, heap.peek()) > 0) { heap.poll(); heap.add(h); }   // better than the current worst
            }
        }
        List<Hit> out = new ArrayList<>(heap); out.sort(Comparator.comparingDouble(Hit::km).thenComparing(h -> h.charger().id()));
        return new Result(out, visited, examined);
    }
}
static final class Availability {                                     // written by the status pipeline, read by every search
    private final ConcurrentMap<String, Status> status = new ConcurrentHashMap<>();
    void update(String id, Status s) { status.put(id, s); }
    Status of(String id) { return status.getOrDefault(id, Status.OFFLINE); }              // unknown is never offered as free
}
static final class ChargerFinder {
    private final GeoIndex index; private final Availability live;
    ChargerFinder(GeoIndex index, Availability live) { this.index = index; this.live = live; }
    Result findFree(double lat, double lon, double radiusKm, int k, int minConnectors) {
        return index.nearby(lat, lon, radiusKm, k, c -> c.connectors() >= minConnectors && live.of(c.id()) == Status.FREE);
    }
}
```
Test setup: a 0.05 degree grid (about 5.5 km); 20,604 chargers, about a quarter of them packed into a Bangalore-sized box and the rest spread over the globe, plus 300 chargers around the antimeridian, 300 within one degree of the north pole, and the edge cases at longitude 180 and -180 and at both poles; random status FREE for two thirds of them. Verified results:
- `haversine(Bangalore, Chennai) = 290.2 km`; two points 0.2 degrees apart across the antimeridian on the equator are `22.2 km` apart (the plain longitude difference would say 359.8 degrees); pole to pole is `20015.1 km` (half the circumference).
- **4,000 random queries** (a Bangalore centre, around the antimeridian, near the north pole, anywhere; radii 0.5 to 2,000 km; `k` 1 to 8; a random `minConnectors`) returned exactly the same ids, in the same order, as a brute-force scan of every charger (2,299 of the queries returned at least one hit).
- **Cost.** Bangalore, 3 km, `k = 5`: 5 hits, **4 cells** visited, **318 candidates** examined out of 20,604. On 300,000 chargers spread over an India-sized box, 2,000 indexed queries (5 km, `k = 10`) took about **55 ms in total** (about 7 candidates examined on average), while 20 brute-force queries took about **1,100 ms** (about 55 ms EACH): roughly a 2,000x difference per query. Building the index for 300,000 chargers took about 400 ms.
- **North pole, 50 km:** found `northpole` and two of the pole-ring chargers, visiting 64,800 cells (all longitudes, nine rows). **Antimeridian:** a charger at -179.99 and one at 179.98, searched from 179.99: `E` at 1.11 km, `W` at 2.22 km (the column range wrapped around). A coordinate such as latitude 91 is rejected at construction.
- **The bounding-box mistake.** At 60 degrees north, 10 km is `0.0899` degrees of longitude with the naive `r / 111.195` and `0.1799` with the exact formula: the naive box is half as wide as it should be and misses every charger in the outer half (the longitude lines converge towards the poles).
- **The huge-radius trap.** A 20,000 km search visited **25,920,000** cells (almost all empty). The index answers correctly, but the cost is the number of cells in the box, not the number of chargers.

Patterns: Value Objects (`Charger`, `Hit`, `Result`; validated in the compact constructor); an immutable index built once and swapped atomically (copy-on-write at the reference level), with the volatile live data in a separate concurrent structure (separation of static and dynamic state); the filter as a `Predicate` (Strategy) so `connector`, `power` and `free` rules are composed, not hard-coded; the finder as the thin application service that wires index and availability (Dependency Injection, no singletons).

Concurrency: the index never changes after construction, so any number of threads read it without a lock; a catalogue reload builds a NEW index and publishes it through a `volatile` or `AtomicReference` field. `Availability` uses a `ConcurrentHashMap`; a search sees each connector's state at some recent moment, which is exactly the contract ("about 30 s fresh"). The search itself is allocation-light and holds no lock.

Test plan: the brute-force oracle comparison above (the strongest test; make the oracle independent and use the SAME distance function); table cases at cell boundaries (a point exactly on a cell edge), on the antimeridian, on both poles, at the exact radius (`d == radiusKm` is included); `k` larger than the number of hits; `k = 0` and a negative radius; ties broken by id; the filter that rejects everything; unknown status never returned as free; a build with zero chargers; a thread test (many readers while the availability map is updated); property: increasing the radius never removes a hit; a test that counts `cellsVisited` so a regression to a full scan is caught.

Principal-level changes to discuss:
- **Large radius.** Cap the radius at the API (and return an error or clamp), or iterate over the non-empty cells when `rows × cols` of the box exceeds the number of occupied cells; that turns the 25.9 million lookups above into at most the number of occupied cells.
- **Uneven density.** One cell size cannot fit a city centre and a desert: the city cell holds thousands of chargers (every query scans them all), the desert cell holds none. Use an adaptive structure (a quadtree / k-d tree / S2 cells at several levels) or a finer grid with a second coarse level; measure `examined` per query by region before tuning.
- **Exact k-nearest without a radius.** "The 10 closest, however far" needs an expanding search: start with a small ring, double the radius until `k` hits are inside the CURRENT radius (hits farther than the ring's inscribed circle may be beaten by a charger in the next ring), and stop when the k-th best distance is below the searched radius.
- **Distance.** Haversine is exact enough for chargers; for a ranking by DRIVING time use a routing service after the index narrows the candidates (top 20 by straight line, then the route matrix), and say the order can change.
- **Live data in the index.** Putting status into the cell lists would make every update a write to a shared structure; keeping it separate and read at filter time costs one hash lookup per candidate, which is fine. If the free-only filter is the common case, keep a per-cell count of free chargers to skip empty cells.
- **Persistence.** The index is a cache of the catalogue: rebuild from the snapshot at startup, serve only when built (readiness), and compare the count against the source after a reload (a half-loaded index silently returns fewer results).
