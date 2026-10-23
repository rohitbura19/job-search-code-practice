# F1. Delayed job scheduler (HLD, 12 min out loud + 5 min self-review)

A platform service that lets other teams register a job to run at a future time (once, or on a cron) and calls their HTTP endpoint or publishes to a topic at that time. Examples: "expire this reservation in 15 minutes", "send the invoice reminder at 09:00 local time", "retry this webhook in 2 minutes".

Cover: requirements and non-goals (precision: seconds or milliseconds? at-least-once vs exactly-once), scale (say 50M pending jobs, 20k firings per second at peak, 1 KB payload), API (create, cancel, get, idempotency key), data model and the index that finds "due soon" jobs, how many scheduler nodes pick up due jobs WITHOUT double-firing (partitioning by time bucket + lease/fencing, or a hashed timing wheel per shard), clock skew, retries/backoff/dead letters, hot spikes (everyone schedules for 09:00:00), cancellation races (cancel arrives while firing), and observability (lag between due time and fire time).

Fill in `my-designs/F1-delayed-job-scheduler.md`. LLD of this feature: item G.
