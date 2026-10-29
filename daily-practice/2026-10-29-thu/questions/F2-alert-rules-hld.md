# F2. HLD: alert rule evaluation service (12 min out loud + 5 min self-review)

> Write your answer in `my-designs/F2-alert-rules.md`, then say it out loud. No tests.

Teams define rules such as "p99 latency of service X > 500 ms for 5 consecutive minutes" or "error rate > 2% for 10 minutes"; 200,000 rules, each evaluated every 30 to 60 seconds against a metrics store. Firing and resolving must notify chat/pager exactly once per incident, with deduplication, grouping and silences.

Cover: requirements and non-goals, rule API and data model, how evaluation is sharded across workers (consistent hashing of rules, rebalancing without double evaluation or gaps), query cost (batching rules on the same series, cached recent windows, pushdown to the store), the state machine (OK -> PENDING -> FIRING -> RESOLVED) and where it is stored, "for 5 minutes" semantics with missing data, the notification pipeline (dedupe key, retry, outbox), flapping and hysteresis, failure modes (evaluator crash mid-incident, store lag, clock skew), and meta-monitoring ("who alerts when the alerting is down?").
