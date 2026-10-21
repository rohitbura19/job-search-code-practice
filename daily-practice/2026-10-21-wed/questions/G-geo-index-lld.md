> Code: `g/GeoIndex.java`, `Availability.java`, `ChargerFinder.java` (`Charger`, `Status`, `Hit`, `Result`, `Geo.haversineKm` are given). Test: `GeoIndexTest`.

## G) LLD of the same question — the geo grid index for nearby chargers (25 min)

Design the in-process **GeoIndex** and **ChargerFinder** (F1). Requirements: `nearby(lat, lon, radiusKm, k, filter)` returns the `k` closest chargers within the radius that pass the filter, closest first, ties broken by id; the static catalogue (id, position, connectors) is immutable once built and shared by many threads; live status changes constantly and is kept OUTSIDE the index; "free right now" is a filter that reads the live status; coordinates are validated; longitude wraps at the antimeridian; a search near a pole works; the result reports how many cells and candidates were examined (so the cost is visible in tests); no I/O, no clock.

Write the classes, the `nearby` flow, and say how the grid, the distance and the top-k are handled. Then discuss patterns, tests and what you would change (what about a huge radius, or a density that varies a lot?).
