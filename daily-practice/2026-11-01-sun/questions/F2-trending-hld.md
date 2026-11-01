# F2. HLD: trending hashtags (12 min out loud + 5 min self-review)

Feature of a social app: show the top 20 trending hashtags for a country and for the whole world, refreshed every minute. 200 k posts per second at peak, each with 0-5 hashtags, a long tail of tags and a few that go viral in minutes.

Cover: requirements (what "trending" means: velocity vs volume, decay, spam resistance), APIs, data model, ingestion path (stream, partitioning, per-window counting), heavy-hitter algorithms (count-min sketch + heap vs exact counts per shard, space/accuracy trade-off), time windows (sliding vs tumbling, decay), merging shard results into a global top-k, serving and caching, bot and abuse filtering, backfill and exactly-once concerns, hot-key handling, failure modes and metrics.
Template: `my-designs/F2-trending.md`.
