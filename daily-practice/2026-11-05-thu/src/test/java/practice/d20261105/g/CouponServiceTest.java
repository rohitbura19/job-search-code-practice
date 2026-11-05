package practice.d20261105.g;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import practice.d20261105.g.CouponException.Reason;

class CouponServiceTest {
    static final Instant T = Instant.parse("2026-11-05T10:00:00Z");
    static final Instant FROM = T.minusSeconds(3600), TO = T.plusSeconds(3600);

    static final class MutableClock extends Clock {
        volatile Instant now = T;
        @Override public ZoneOffset getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(java.time.ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
    }

    final MutableClock clock = new MutableClock();
    final CouponService svc = new CouponService(clock);

    static Cart cart(long... unitPrices) {
        List<Cart.Line> lines = new ArrayList<>();
        for (int i = 0; i < unitPrices.length; i++) lines.add(new Cart.Line("sku" + i, unitPrices[i], 1));
        return new Cart(lines);
    }

    static Coupon pct(String code, long pct, long min, long cap, int max, int perUser) {
        return new Coupon(code, Coupon.Type.PERCENT, pct, min, cap, FROM, TO, max, perUser);
    }

    static Reason reason(Runnable r) { return assertThrows(CouponException.class, r::run).reason(); }

    @Test void percentAndFlatAmounts() {
        svc.register(pct("SAVE10", 10, 0, 0, 100, 5));
        svc.register(new Coupon("FIVE", Coupon.Type.FLAT, 500, 0, 0, FROM, TO, 100, 5));
        svc.register(new Coupon("HUGE", Coupon.Type.FLAT, 99_999, 0, 0, FROM, TO, 100, 5));
        assertEquals(new Discount("SAVE10", 1999), svc.preview("SAVE10", "u", cart(19_999)));
        assertEquals(500, svc.preview("FIVE", "u", cart(2000, 100)).amountCents());
        assertEquals(300, svc.preview("HUGE", "u", cart(300)).amountCents());
        assertEquals(0, svc.preview("SAVE10", "u", cart(9)).amountCents());
    }

    @Test void percentIsCappedAndNeverExceedsSubtotal() {
        svc.register(pct("CAP", 50, 0, 1000, 10, 10));
        svc.register(pct("ALL", 100, 0, 0, 10, 10));
        assertEquals(1000, svc.preview("CAP", "u", cart(100_000)).amountCents());
        assertEquals(250, svc.preview("CAP", "u", cart(500)).amountCents());
        assertEquals(777, svc.preview("ALL", "u", cart(777)).amountCents());
        assertEquals(0, svc.preview("ALL", "u", cart()).amountCents());
    }

    @Test void largeAmountsDoNotOverflow() {
        svc.register(pct("BIG", 90, 0, 0, 10, 10));
        long price = 2_000_000_000L;
        assertEquals(price * 3 * 90 / 100, svc.preview("BIG", "u", cart(price, price, price)).amountCents());
    }

    @Test void codeIsCaseInsensitive() {
        svc.register(pct("Save10", 10, 0, 0, 10, 10));
        assertEquals(10, svc.preview("sAvE10", "u", cart(100)).amountCents());
        assertThrows(IllegalArgumentException.class, () -> svc.register(pct("SAVE10", 5, 0, 0, 10, 10)));
    }

    @Test void registerValidatesLimits() {
        assertThrows(IllegalArgumentException.class, () -> svc.register(pct("A", 10, 0, 0, 0, 1)));
        assertThrows(IllegalArgumentException.class, () -> svc.register(pct("B", 10, 0, 0, 1, 0)));
    }

    @Test void rejectionReasonsAndTheirOrder() {
        svc.register(pct("LATE", 10, 0, 0, 10, 10));
        svc.register(new Coupon("SOON", Coupon.Type.FLAT, 100, 0, 0, T.plusSeconds(10), TO, 10, 10));
        svc.register(new Coupon("OLD", Coupon.Type.FLAT, 100, 0, 0, FROM, T, 10, 10));
        svc.register(pct("MIN", 10, 5000, 0, 10, 10));
        assertEquals(Reason.UNKNOWN, reason(() -> svc.preview("NOPE", "u", cart(100))));
        assertEquals(Reason.NOT_STARTED, reason(() -> svc.preview("SOON", "u", cart(100))));
        assertEquals(Reason.EXPIRED, reason(() -> svc.preview("OLD", "u", cart(100))));
        assertEquals(Reason.MIN_NOT_MET, reason(() -> svc.preview("MIN", "u", cart(4999))));
        assertEquals(500, svc.preview("MIN", "u", cart(5000)).amountCents());
        clock.now = TO;
        assertEquals(Reason.EXPIRED, reason(() -> svc.preview("LATE", "u", cart(100))));
        clock.now = T.plusSeconds(10);
        assertEquals(100, svc.preview("SOON", "u", cart(100)).amountCents());
    }

    @Test void totalAndPerUserLimits() {
        svc.register(pct("TWO", 10, 0, 0, 3, 2));
        svc.redeem("TWO", "alice", "o1", cart(1000));
        svc.redeem("TWO", "alice", "o2", cart(1000));
        assertEquals(Reason.USER_LIMIT, reason(() -> svc.redeem("TWO", "alice", "o3", cart(1000))));
        svc.redeem("TWO", "bob", "o4", cart(1000));
        assertEquals(Reason.EXHAUSTED, reason(() -> svc.redeem("TWO", "carol", "o5", cart(1000))));
        assertEquals(Reason.EXHAUSTED, reason(() -> svc.preview("TWO", "carol", cart(1000))));
        assertEquals(Reason.EXHAUSTED, reason(() -> svc.redeem("TWO", "alice", "o6", cart(1000))));
    }

    @Test void failedRedeemConsumesNothing() {
        svc.register(pct("MIN", 10, 5000, 0, 1, 1));
        assertEquals(Reason.MIN_NOT_MET, reason(() -> svc.redeem("MIN", "u", "o1", cart(100))));
        assertEquals(500, svc.redeem("MIN", "u", "o2", cart(5000)).amountCents());
    }

    @Test void redeemIsIdempotentPerOrder() {
        svc.register(pct("ONE", 10, 0, 0, 1, 1));
        Discount first = svc.redeem("ONE", "alice", "o1", cart(1000));
        assertEquals(first, svc.redeem("ONE", "alice", "o1", cart(1000)));
        assertEquals(first, svc.redeem("ONE", "alice", "o1", cart(999_999)));
        clock.now = TO.plusSeconds(1);
        assertEquals(first, svc.redeem("ONE", "alice", "o1", cart(1000)));
        clock.now = T;
        assertEquals(Reason.EXHAUSTED, reason(() -> svc.redeem("ONE", "bob", "o2", cart(1000))));
    }

    @Test void releaseGivesTheRedemptionBackOnce() {
        svc.register(pct("ONE", 10, 0, 0, 1, 1));
        svc.redeem("ONE", "alice", "o1", cart(1000));
        assertEquals(Reason.EXHAUSTED, reason(() -> svc.redeem("ONE", "bob", "o2", cart(1000))));
        svc.release("o1");
        svc.release("o1");
        svc.release("never-existed");
        svc.redeem("ONE", "alice", "o3", cart(1000));
        assertEquals(Reason.EXHAUSTED, reason(() -> svc.redeem("ONE", "bob", "o4", cart(1000))));
        svc.release("o1");
        assertEquals(Reason.EXHAUSTED, reason(() -> svc.redeem("ONE", "bob", "o5", cart(1000))));
    }

    @Test void concurrentRedemptionsNeverExceedTheTotal() throws Exception {
        svc.register(pct("FLASH", 10, 0, 0, 10, 1));
        int threads = 100;
        ExecutorService pool = Executors.newFixedThreadPool(32);
        CountDownLatch go = new CountDownLatch(1);
        AtomicInteger ok = new AtomicInteger();
        List<Future<?>> fs = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            int id = i;
            fs.add(pool.submit(() -> {
                go.await();
                try { svc.redeem("FLASH", "user" + id, "order" + id, cart(1000)); ok.incrementAndGet(); } catch (CouponException e) { assertEquals(Reason.EXHAUSTED, e.reason()); }
                return null;
            }));
        }
        go.countDown();
        for (Future<?> f : fs) f.get();
        pool.shutdown();
        assertEquals(10, ok.get());
    }

    @Test void concurrentSameUserRespectsPerUserLimit() throws Exception {
        svc.register(pct("U", 10, 0, 0, 1000, 3));
        ExecutorService pool = Executors.newFixedThreadPool(16);
        AtomicInteger ok = new AtomicInteger();
        List<Future<?>> fs = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int id = i;
            fs.add(pool.submit(() -> { try { svc.redeem("U", "same", "o" + id, cart(1000)); ok.incrementAndGet(); } catch (CouponException ignored) { } }));
        }
        for (Future<?> f : fs) f.get();
        pool.shutdown();
        assertEquals(3, ok.get());
    }

    @Test void concurrentSameOrderIdConsumesOnce() throws Exception {
        svc.register(pct("IDEM", 10, 0, 0, 5, 5));
        ExecutorService pool = Executors.newFixedThreadPool(16);
        List<Future<Discount>> fs = new ArrayList<>();
        for (int i = 0; i < 50; i++) fs.add(pool.submit(() -> svc.redeem("IDEM", "u", "same-order", cart(1000))));
        for (Future<Discount> f : fs) assertEquals(100, f.get().amountCents());
        pool.shutdown();
        for (int i = 0; i < 4; i++) svc.redeem("IDEM", "u" + i, "x" + i, cart(1000));
        assertEquals(Reason.EXHAUSTED, reason(() -> svc.redeem("IDEM", "z", "last", cart(1000))));
    }
}
