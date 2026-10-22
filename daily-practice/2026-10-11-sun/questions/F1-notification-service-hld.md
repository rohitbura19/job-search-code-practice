> No code. Write your answer in `my-designs/F1-notification-service.md` (15 min), say it out loud once, compare with `answers/F-hld-references.md` only afterwards.

## F) HLD — two small features of a big app (30 min, about 15 each)

Use this frame: requirements (functional, non-functional, scale numbers) -> API -> data model -> high-level diagram -> deep dive on the one hard part -> failure modes -> metrics.

### F1. Notification service for an EV-charging app (push/SMS/email)
Events: "charging started", "charging complete", "charger faulted", "payment failed". 5M users, 500k chargers, peak 20k events/sec. Users have per-channel preferences and quiet hours. Must not send duplicates, must be fast for "faulted" alerts (<5 s), and cheap for marketing mails.

Think about: ingest (IoT Core rule -> SQS), priority queues (urgent vs bulk), template + preference lookup, per-channel workers, provider failover (two SMS vendors), retries with backoff + DLQ, dedupe key, rate limiting per user, delivery status tracking.
