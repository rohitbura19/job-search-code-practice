# F1 - HLD: distributed unique ID service (12 min out loud + 5 min review)
Feature of a large platform (orders, messages, posts): hand out 64-bit ids to hundreds of services.
Requirements: ~1M ids/s overall, ids roughly time-sortable, no coordination on the hot path, unique across regions, survives restarts and clock hiccups. Non-goals: gap-free, secret/unguessable.
Cover: id layout and capacity maths, worker-id assignment (who guarantees uniqueness of a worker id after restarts?), clock skew/backwards handling, library vs service, failure modes (two workers with one id), alternatives (UUIDv7, DB ticket servers, Redis INCR, ULID) and when each wins, hot-shard effects of time-ordered keys.
