> Code: `src/main/java/practice/d20261014/b1/TelemetryStats.java` (record `Reading` is GIVEN). Test: `TelemetryStatsTest`.

### B1. Device telemetry analytics (flatMapping, filtering, nested grouping)
```java
record Reading(String deviceId, String metric, double value, Instant ts, Set<String> tags) {}
```
Given `List<Reading> readings`:
1. `Map<String, Reading>` of the latest reading per device, keys sorted.
2. `Map<String, Map<String, Double>>`: metric -> deviceId -> average value, both levels sorted.
3. `Map<String, Set<String>>`: metric -> all distinct tags seen for that metric, sorted (one pipeline, no second pass).
4. Devices with at least 2 `temp` readings above 80.
5. Given a set of known device ids and a cutoff `Instant`: which known devices have been silent since the cutoff?

Interviewer follow-up: `Collectors.filtering` vs `stream().filter()` before `groupingBy` — what is different (hint: devices with zero matches)? How do you take the top K readings by value without sorting everything? How do you find the reading at or just before time `t` in a time-ordered structure? Why does `Arrays.asList(1,2,3).add(4)` throw? What does `subList` return and what happens if you change the parent?
