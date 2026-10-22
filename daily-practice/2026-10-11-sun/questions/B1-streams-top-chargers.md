> Code: `b1/SessionStats.java` (`Session` is given). Test: `SessionStatsTest`.

## B) Streams / Collections revision (1)

### B1. Top 3 chargers by energy (Enphase flavour)
```java
record Session(String chargerId, String siteId, double kwh, Instant start) {}
```
Given `List<Session> sessions`:
1. Return the top 3 `chargerId`s by total kWh.
2. Return `Map<String, Double>` average kWh per site, sites sorted by name.
3. Return the site that has the most distinct chargers.

Interviewer follow-up: what changes with `parallelStream()`? When is it wrong to use it? What does `Collectors.toMap` do on duplicate keys?
