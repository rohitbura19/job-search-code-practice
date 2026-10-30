# F2. HLD: usage metering and billing for an API platform (12 min out loud + 5 min self-review)

Customers call a public API; we charge per call and per GB with monthly tiers, show near-real-time usage in a dashboard, and enforce a hard quota.

Functional: ingest usage events from gateways (may be retried or duplicated), aggregate per customer / meter / hour, dashboard (< 1 min delay), invoice at month end, quota check on the request path.
Non-functional: 200k events/s, exactly-once **billing effect**, late events up to 24 h, audit trail, invoice reproducible.

Cover: event schema and idempotency key, ingestion buffer, stream aggregation windows and late data, hot vs cold storage, quota enforcement (approximate counter vs exact), invoice generation, reconciliation, correcting a bug after invoices went out. Write in `my-designs/F2-usage-metering.md`. No tests.
