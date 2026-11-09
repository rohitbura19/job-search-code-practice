# F2 - Ticket booking: seat hold during checkout (HLD, 12 min + 5 review)

Users pick seats for a concert, the system holds them for 10 minutes while they pay, then confirms or releases.

Requirements: 50k seats per event, flash sale with 500k users in the first minute, no seat is ever sold twice, a hold expires automatically, payment may succeed after the hold expired (decide and handle it), fair waiting room, 99.9% availability.
Discuss: seat state machine (AVAILABLE -> HELD -> SOLD), atomic hold (conditional update / Redis Lua with TTL / row lock), expiry (TTL vs sweeper), the payment/hold race and idempotent confirm, hot-event contention and sharding by event, virtual queue and rate limiting, what the read path (seat map) can serve stale.
