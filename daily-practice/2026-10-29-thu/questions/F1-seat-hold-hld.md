# F1. HLD: seat selection with temporary holds for a ticketed event (12 min out loud + 5 min self-review)

> Write your answer in `my-designs/F1-seat-hold.md`, then say it out loud. No tests. The LLD of this feature is G.

A user opens a seat map for a concert, picks 1 to 6 seats, and has 5 minutes to pay. While held the seats are unavailable to others; after 5 minutes without payment they return to the pool. A hot event sells 50,000 seats in the first minute with 500,000 people watching the seat map.

Cover: requirements and non-goals, APIs (seat map, hold, confirm, release), data model, where the seat state lives (database vs Redis vs both) and how the all-or-nothing hold is made atomic, how holds expire (lazy check vs TTL key vs sweeper) and how payment confirms (idempotency, hold expiring DURING payment), how the seat map is served to 500,000 viewers (push vs poll, staleness), a virtual waiting room, hot-partition handling (one event = one hot key), failure modes (Redis loss, double confirm, payment callback after expiry), and metrics.
