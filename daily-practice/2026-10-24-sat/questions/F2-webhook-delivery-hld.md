# F2. HLD: reliable webhook delivery to customer endpoints (12 min out loud + 5 min self-review)

A platform emits events (payment succeeded, order shipped) and must POST them to the HTTPS endpoint each customer registered: 20k events/s at peak, endpoints of wildly different speed and reliability.

Cover: requirements (at-least-once, per-endpoint ordering needed or not), API (register endpoint, event types, secret rotation, delivery log, manual replay), the pipeline (outbox in the producing service, queue, dispatcher workers), signing (HMAC of timestamp + body, replay window), retries with exponential backoff and jitter over about 3 days then a dead-letter state, idempotency for receivers (`event id` header), isolation of slow or dead endpoints (per-endpoint concurrency cap, circuit breaker, so one bad endpoint cannot starve the rest), SSRF protection (resolve and block private ranges, pin the IP, no redirects), data model and retention of delivery attempts, observability (success rate and lag per endpoint, auto-disable and notify), trade-offs.

Fill in `my-designs/F2-webhook-delivery.md`.
