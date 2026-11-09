# B1 - Streams on ride trips (15 min) - `b1/TripStreams.java`

`Trip(id, driver, city, km, fareCents, rating)` is given. Seven one-pipeline methods: fare total per city (sorted keys), top driver per city by total fare (ties: alphabetical) via a downstream `collectingAndThen`, drivers per rating with descending keys, partition by long trips (both keys present), `takeWhile` leading highly-rated trips, `dropWhile` short starting trips, joined driver summary `"[Ann, Bob]"`. Details are in the method Javadoc. No loops, no external mutable state.
