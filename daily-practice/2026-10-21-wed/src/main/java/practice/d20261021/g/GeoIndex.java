package practice.d20261021.g;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * G - see questions/G-geo-index-lld.md. Test: GeoIndexTest.
 * A uniform latitude/longitude grid of cellDeg degrees. Immutable after construction.
 */
public final class GeoIndex {

    public GeoIndex(Collection<Charger> chargers, double cellDeg) {
        // TODO: bucket every charger into its cell
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Up to k chargers within radiusKm (inclusive) that pass the filter, closest first, ties by id.
     * Use Geo.haversineKm. Handle the antimeridian and the poles. Return the cost counters in Result.
     * k <= 0 or radiusKm < 0 -> empty result.
     */
    public Result nearby(double lat, double lon, double radiusKm, int k, Predicate<Charger> filter) {
        throw new UnsupportedOperationException("TODO");
    }
}
