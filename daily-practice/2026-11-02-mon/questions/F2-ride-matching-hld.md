# F2. HLD: nearby driver search and ride matching (12 min out loud + 5 min self-review)

> No code. Write your answer in `my-designs/F2-ride-matching.md`. No tests.

500k drivers send a location update every 4 s; riders request a ride and must get a match offer within 2 s. Support "show drivers within 2 km" on the map.

Cover: the geo index (geohash / S2 / H3 cells vs quadtree vs Redis GEO), write path for 125k updates/s and keeping only the latest position, TTL for offline drivers, cell-boundary and cell-size choices, the matching flow (candidate query, ranking by ETA, offer with timeout, one driver one offer at a time), double-booking prevention, sharding by city/cell, and what you drop under load.
