# F2. HLD: real-time game leaderboard (12 min out loud + 5 min review)

A feature of a big app: global and per-region top-100, "my rank" for any player, daily/weekly/all-time boards.
Requirements to clarify: 50 M players, 20 k score updates/s, reads 10x writes, rank freshness under 2 s, ties broken by earlier achievement, weekly reset.
Cover: Redis sorted set vs DB index vs in-memory skip list, sharding a single huge board (score-range shards, rank = sum of higher shards + local rank), the approximate rank for far-down players, write path (queue, batching, idempotent score events), time-windowed boards (one key per window, TTL), cheap top-N cache, and failure/rebuild from the event log.
