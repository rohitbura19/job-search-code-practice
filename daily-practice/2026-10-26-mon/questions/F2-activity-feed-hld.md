> No code. Write your answer in `my-designs/F2-activity-feed.md` (12 min out loud + 5 min self-review). No tests.

# F2. HLD: per-customer activity timeline ("what happened to my case/order")

Every customer sees a reverse-chronological timeline of events about their cases and orders (status changes, agent replies, refunds, uploads). Events come from about 15 backend services.

Cover: requirements and numbers (events/s, timeline reads/s, retention, max timeline length); how events enter (outbox + Kafka, schema, idempotent consumers); fan-out on write vs fan-out on read and the hybrid (what is stored per customer vs per case); the read API with cursor pagination (why not offset) and "new since" polling or push; ordering and clock skew between services; privacy (an agent-only note must not reach the customer; GDPR delete); storage choice and partition key; hot partitions (one enterprise customer with millions of events); replays and backfills; failure modes (consumer lag, duplicate events, a service emitting out of order).
