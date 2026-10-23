# F2. Real-time leaderboard (HLD, 12 min out loud + 5 min self-review)

A game or loyalty app shows a global top-100, "my rank", and the 5 players above and below me, updated within a second of a score change. 20M players, 3k score updates per second, 50k reads per second.

Cover: requirements (daily/weekly/all-time boards, ties, resets at midnight IST), API, data structure for rank queries (Redis sorted set vs database `ORDER BY` + index vs skip list in memory), memory estimate, sharding one huge board (score-range partitions + a merge of tops, vs approximate rank for the tail), write path (idempotent score events from Kafka, out-of-order and duplicate events), read path (cache the top-100 for 1 s, rank via `ZREVRANK`), period rollover without a thundering herd, durability (rebuild from the event log), anti-cheat hooks, and failure modes.

Fill in `my-designs/F2-leaderboard.md`.
