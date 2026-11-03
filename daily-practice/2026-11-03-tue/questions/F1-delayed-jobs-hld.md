# F1. HLD: delayed job / reminder scheduler (12 min out loud + 5 min review)

A feature of a big app: "run this callback / send this reminder at time T (or after delay D)", with retries and cancel. Examples: payment reminder in 24 h, auto-close a ticket after 7 days, retry a webhook in 30 s.
Requirements to clarify: 50 M jobs/day, peak 20 k due per second at round hours, delays from 1 s to 1 year, at-least-once execution within 5 s of the due time, cancel/reschedule, per-tenant fairness, retries with backoff and a dead-letter queue.
Cover: API, durable storage and how due jobs are found cheaply (time buckets / partition by minute vs a heap vs Redis ZSET vs DB poll with `SKIP LOCKED`), claiming without double run (lease + fencing), the hot "everything at 09:00" spike, clock skew, and what happens when a worker dies mid-job.
