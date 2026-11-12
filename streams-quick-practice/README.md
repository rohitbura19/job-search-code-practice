# Java Streams quick practice

All 27 streams / collections questions from the daily kits. Each folder has:

- `QUESTION.md` - the task
- the data classes and the streams class with empty methods (`throw new UnsupportedOperationException("TODO")`) - write your pipelines here
- `*Check.java` - run it to check your work on the kit's sample data. It prints `OK`, `WRONG -> expected ... but was ...`, or `TODO` per method. Plain Java, no JUnit.

## How to run

From this folder (compile once, then run the check for the topic you did):

```
javac -d out $(find . -name "*.java")          # Git Bash
java -cp out s27_warehouse.WarehouseStreamsCheck
```

IntelliJ / VS Code: mark `streams-quick-practice` as a source root and run the `main` in the `*Check` class.

Answers: `daily-practice/<day>/answers/` (day is in the first line of each `QUESTION.md`).

## Topics

| Folder | Day | Practises |
|---|---|---|
| [s01_topchargers](s01_topchargers/) | 2026-10-11-sun | Charging sessions: top chargers, grouping, sorting |
| [s02_employeeanalytics](s02_employeeanalytics/) | 2026-10-12-mon | Employee analytics (Collectors deep dive) |
| [s03_orderanalyticslru](s03_orderanalyticslru/) | 2026-10-13-tue | Order analytics + LRU (Collectors, sorting, LinkedHashMap) |
| [s04_telemetrystreams](s04_telemetrystreams/) | 2026-10-14-wed | Device telemetry analytics (flatMapping, filtering, nested grouping) |
| [s05_collections](s05_collections/) | 2026-10-21-wed | Energy events: summarizingInt, reducing, nullsLast, iterate + list views, access-order maps, IdentityHashMap, BitSet |
| [s06_orders](s06_orders/) | 2026-10-22-thu | Orders: groupingBy + downstream, partitioningBy, teeing + collection traps |
| [s07_trips](s07_trips/) | 2026-10-23-fri | Streams and collectors on ride trips |
| [s08_inventory](s08_inventory/) | 2026-10-24-sat | Collectors and sorted maps on warehouse stock |
| [s09_bookings](s09_bookings/) | 2026-10-25-sun | Streams and collectors on bookings |
| [s10_payments](s10_payments/) | 2026-10-26-mon | Streams and collectors on payments |
| [s11_shipments](s11_shipments/) | 2026-10-27-tue | Streams and collectors on shipments |
| [s12_loans](s12_loans/) | 2026-10-28-wed | Streams and collectors on library loans |
| [s13_tickets](s13_tickets/) | 2026-10-29-thu | Streams and collectors on support tickets |
| [s14_campus](s14_campus/) | 2026-10-30-fri | Streams: flatMap, toMap, Comparator chains, IntStream |
| [s15_movies](s15_movies/) | 2026-10-31-sat | Streams / collectors on movie ratings |
| [s16_logs](s16_logs/) | 2026-11-01-sun | Streams on service logs: TreeMap grouping, maxBy, flatMap, dropWhile/takeWhile, iterate, mapMulti |
| [s17_staff](s17_staff/) | 2026-11-02-mon | Streams and collections: staff statistics + an LRU map |
| [s18_collections](s18_collections/) | 2026-11-03-tue | Java collections: the Map/List default methods |
| [s19_orders](s19_orders/) | 2026-11-04-wed | Streams and collectors on orders |
| [s20_library](s20_library/) | 2026-11-05-thu | Streams and collections: a library |
| [s21_staff](s21_staff/) | 2026-11-06-fri | Streams / collections: staff |
| [s22_inventory](s22_inventory/) | 2026-11-07-sat | Streams / collectors |
| [s23_transactions](s23_transactions/) | 2026-11-08-sun | Streams on bank transactions |
| [s24_trips](s24_trips/) | 2026-11-09-mon | Streams on ride trips |
| [s25_sensors](s25_sensors/) | 2026-11-10-tue | Streams on sensor readings |
| [s26_enrollments](s26_enrollments/) | 2026-11-11-wed | Streams on enrollments |
| [s27_warehouse](s27_warehouse/) | 2026-11-12-thu | Streams on warehouse stock movements |
