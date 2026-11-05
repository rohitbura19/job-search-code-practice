# G. LLD of F1: the coupon engine (sketch 8 min, code 22 min)

Sketch classes first in `my-designs/G-classes-sketch.md`, then implement `g/CouponService.java`. Given: `Cart`, `Coupon`, `Discount`, `CouponException`. Test: `CouponServiceTest` (amounts, caps, reasons and their order, limits, idempotency per order id, release, 100-thread races).

Rules are in the Javadoc of `CouponService`. Key points: money in cents (`long`), the discount never exceeds the subtotal, `preview` and `redeem` share one validation path, the check-then-consume sequence is atomic per coupon, a repeated `orderId` returns the stored discount, `release` is idempotent.

Think about: lock granularity (global vs per coupon vs lock-free `AtomicInteger` with CAS), why `computeIfAbsent` is the wrong place for the consume, and how this in-memory model maps to Redis (`DECR` with a Lua script) or a DB row with `UPDATE ... WHERE used < max`.
