# F1. Coupon / promo-code service of an e-commerce checkout (HLD, 12 min + 5 min review)

Design the service that validates and redeems promo codes at checkout.

- 20 M daily active users, flash sales with 50 k checkout attempts/s, a code may be limited to the first 10 000 redemptions in total and 1 per user.
- Percentage and flat codes, validity window, minimum cart value, cap on discount, per-user and global limits; marketing creates codes in bulk (millions, unique single-use codes too).
- Preview (cart page, high volume, read-only) vs redeem (order placement, must be exact).
- Refunds and cancelled orders give the redemption back. A retried order request must not consume a second redemption.

Cover: requirements and numbers, APIs, data model, the exact-counter problem (hot code, contention), idempotency, abuse (code guessing, sharing), scaling, failure modes (what if the service is down at checkout?), trade-offs. Fill `my-designs/F1-coupon-service.md`.
