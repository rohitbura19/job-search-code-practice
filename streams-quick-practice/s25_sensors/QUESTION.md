<!-- From daily-practice/2026-11-10-tue -->
# B1 - Streams on sensor readings (15 min)

`Reading(sensor, site, minute, value)` is given. Use streams and collectors only (no loops, no mutable accumulators outside collectors).

1. `peakPerSite` - highest value per site (TreeMap by site).
2. `averagePerSensor` - average per sensor, only sensors with at least 2 readings.
3. `alwaysAbove(readings, threshold)` - sensors whose every reading is strictly above the threshold, alphabetical.
4. `topValuesPerSite(readings, n)` - the n highest values per site, descending, duplicates kept.
5. `busiestSite` - site with the most readings; ties go to the alphabetically smaller; `Optional.empty()` for no data.
6. `activeSensors(readings, from, to)` - per site the distinct sensors (sorted) with a reading in the half-open minute window `[from, to)`; sites without any are omitted.

Input up to 300,000 readings; every method must be a single pass per collector chain (3 s budget for all six).
