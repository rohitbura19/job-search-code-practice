> No code. Write your answer in `my-designs/F2-flash-sale-reservation.md` (15 min), say it out loud once, compare with `answers/F-hld-references.md` only afterwards.

### F2. Flash-sale inventory reservation (Amazon/Flipkart-style "hold this item for 10 min")
100k concurrent users chasing 1,000 units. No overselling, hold expires after 10 minutes and stock is released, checkout is idempotent.

Think about: where the source of truth for stock lives, atomic decrement (DB conditional update vs Redis/Lua vs queue per SKU), hot-key problem, reservation record with TTL, expiry sweeper vs delayed message, payment callback race, idempotency key, what the user sees under load (queue / waiting room).
