# Q06. Log Store with 1-Year Retention (no cloud)

**Duration:** 30 minutes (15 for code, 15 for design talk) · **Language:** Java · **Source:** IDFC onsite design round, per LeetCode ("collect logs, retain 1 year, focus on storage, no cloud services")

## Problem Statement

Build the storage layer for an application's logs. Logs must be kept for **365 days** on local disk only: no S3 and no CloudWatch. After 365 days they must be deleted automatically.

Logs are written to **segments**, which stand in for files:

- New entries go into the **active segment**.
- When the active segment reaches `maxEntriesPerSegment`, it is **rolled over**: closed, compressed through a `Compressor`, and archived. A new active segment then starts. In real life the limit is in bytes; entries are used here to keep tests simple.
- `purgeExpired()` deletes every archived segment whose **newest** entry is older than 365 days. Older than 365 days means `now − timestamp > 365 days`. The active segment is removed only if all of its entries have expired.
- `query(from, to, minLevel)` returns the matching entries, oldest first. It must **skip segments** whose time range doesn't overlap `[from, to]`.

## API

```java
enum Level { DEBUG, INFO, WARN, ERROR }
record LogEntry(Instant timestamp, Level level, String message) {}
interface Compressor { byte[] compress(List<LogEntry> entries); List<LogEntry> decompress(byte[] data); }

class LogStore {
    LogStore(int maxEntriesPerSegment, Duration retention, Clock clock, Compressor compressor)
    void append(LogEntry e)                      // timestamps are non-decreasing
    int purgeExpired()                           // returns the number of segments deleted
    List<LogEntry> query(Instant from, Instant to, Level minLevel)
    int segmentCount()                           // archived + active
    int segmentsScannedByLastQuery()             // proves the skip works
}
```

## Example (time written as "day N")

```
maxEntriesPerSegment = 2, retention = 365 days
append(day1 INFO "start"), append(day2 ERROR "db down"), append(day3 WARN "slow"),
append(day4 INFO "ok"), append(day5 ERROR "timeout")
-> segments: [d1,d2] archived, [d3,d4] archived, [d5] active  -> segmentCount() = 3

query(day3, day5, WARN) -> [day3 WARN "slow", day5 ERROR "timeout"]
segmentsScannedByLastQuery() = 2        // [d1,d2] skipped

clock = day 370
purgeExpired() -> 2                     // [d1,d2] and [d3,d4] are older than 365 days; d5 is exactly 365 -> kept
query(day1, day5, DEBUG) -> [day5 ERROR "timeout"]
```

## Expected Design

- `Segment` holds `minTs`, `maxTs`, `count` and either the entries (active) or compressed bytes (archived).
- `Deque` or `TreeMap<Instant, Segment>` ordered by start time, so purging only removes from the head and queries can binary search to the first overlapping segment.
- `Clock` is injected so tests can control time.
- `append` must be O(1) on average. Compression happens only at rollover.

## Design talk (the panel moves on to this)

1. **Sizing:** 2 GB of raw logs a day for 365 days is about 730 GB. Gzip at roughly 8–10× brings that to about 75–90 GB. How much disk would you provision, and with what alert threshold?
2. **File layout:** for example `/logs/app/2026/10/23/app-0001.log.gz`. Roll over by size and by day. Deleting a day's directory is cheap.
3. **Purge job:** a `ScheduledExecutorService` or cron job runs every night. It must be idempotent and safe to run twice.
4. **Write path:** an async appender writes through a bounded queue to a single writer thread. What happens when the queue is full: drop, block, or sample?
5. **Durability:** how often to fsync, and how not to lose the tail of the logs on a crash.
6. **Search:** a per-segment index of min/max timestamp and level counts, and a bloom filter for keywords. When would you move to something like ELK on-prem?
7. Compliance: masking PII and card numbers before storage, because this is a bank.
