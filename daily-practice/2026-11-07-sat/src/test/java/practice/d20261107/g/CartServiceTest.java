package practice.d20261107.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CartServiceTest {
    static final class MutableClock extends Clock {
        Instant now = Instant.parse("2026-11-07T00:00:00Z");
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
        void advance(Duration d) { now = now.plus(d); }
    }

    MutableClock clock;
    Map<String, Long> prices;
    CartService svc;

    @BeforeEach void setUp() {
        clock = new MutableClock();
        prices = new ConcurrentHashMap<>(Map.of("pen", 150L, "book", 1200L, "bag", 5000L, "cap", 700L));
        svc = new CartService(sku -> prices.containsKey(sku) ? OptionalLong.of(prices.get(sku)) : OptionalLong.empty(),
                clock, Duration.ofMinutes(30), 5, 3, List.of(new ThresholdPercentRule(5000, 10)));
    }

    @Test void emptyView() {
        CartView v = svc.view("nobody");
        assertTrue(v.lines().isEmpty());
        assertEquals(0, v.totalCents());
        assertEquals(0, v.itemCount());
    }

    @Test void addMergesAndPrices() {
        svc.addItem("u1", "pen", 2);
        CartView v = svc.addItem("u1", "pen", 1);
        assertEquals(List.of(new CartLine("pen", 3, 150)), v.lines());
        assertEquals(450, v.subtotalCents());
        assertEquals(0, v.discountCents());
        assertEquals(3, v.itemCount());
    }

    @Test void linesSortedAndDiscountApplies() {
        svc.addItem("u1", "book", 1);
        CartView v = svc.addItem("u1", "bag", 1);
        assertEquals(List.of("bag", "book"), v.lines().stream().map(CartLine::sku).toList());
        assertEquals(6200, v.subtotalCents());
        assertEquals(620, v.discountCents());
        assertEquals(5580, v.totalCents());
    }

    @Test void setQtyAndRemove() {
        svc.addItem("u1", "pen", 2);
        assertEquals(5, svc.setQty("u1", "pen", 5).itemCount());
        assertTrue(svc.setQty("u1", "pen", 0).lines().isEmpty());
        svc.addItem("u1", "cap", 1);
        assertTrue(svc.remove("u1", "cap").lines().isEmpty());
        assertTrue(svc.remove("u1", "ghost").lines().isEmpty()); // removing a missing line is a no-op
        assertTrue(svc.setQty("u1", "pen", 0).lines().isEmpty()); // so is setQty 0 on a missing line
    }

    @Test void validation() {
        assertThrows(IllegalArgumentException.class, () -> svc.addItem("u1", "pen", 0));
        assertThrows(IllegalArgumentException.class, () -> svc.addItem("u1", "pen", -1));
        assertThrows(IllegalArgumentException.class, () -> svc.addItem("u1", "ghost", 1));
        assertThrows(IllegalArgumentException.class, () -> svc.addItem(" ", "pen", 1));
        assertThrows(IllegalArgumentException.class, () -> svc.addItem("u1", null, 1));
        assertThrows(IllegalArgumentException.class, () -> svc.setQty("u1", "pen", -1));
        assertThrows(IllegalArgumentException.class, () -> svc.setQty("u1", "ghost", 2));
        assertThrows(IllegalArgumentException.class, () -> svc.view(null));
    }

    @Test void limitsLeaveCartUnchanged() {
        svc.addItem("u1", "pen", 4);
        assertThrows(IllegalStateException.class, () -> svc.addItem("u1", "pen", 2));
        assertEquals(4, svc.view("u1").itemCount());
        assertThrows(IllegalStateException.class, () -> svc.setQty("u1", "pen", 6));
        svc.addItem("u1", "book", 1);
        svc.addItem("u1", "cap", 1);
        assertThrows(IllegalStateException.class, () -> svc.addItem("u1", "bag", 1)); // 4th line
        assertEquals(3, svc.view("u1").lines().size());
        assertEquals(5, svc.addItem("u1", "pen", 1).lines().get(2).qty()); // existing line still allowed (sorted: book, cap, pen)
    }

    @Test void ttlExpiryAndSliding() {
        svc.addItem("u1", "pen", 1);
        clock.advance(Duration.ofMinutes(29));
        svc.addItem("u1", "cap", 1);                 // touch resets the clock
        clock.advance(Duration.ofMinutes(29));
        assertEquals(2, svc.view("u1").itemCount());
        clock.advance(Duration.ofMinutes(1));        // 30 min since last touch -> expired (boundary inclusive)
        assertTrue(svc.view("u1").lines().isEmpty());
        assertEquals(1, svc.addItem("u1", "book", 1).itemCount()); // fresh cart, old lines gone
    }

    @Test void viewDoesNotExtendTtl() {
        svc.addItem("u1", "pen", 1);
        clock.advance(Duration.ofMinutes(20));
        svc.view("u1");
        clock.advance(Duration.ofMinutes(10));
        assertTrue(svc.view("u1").lines().isEmpty());
    }

    @Test void currentPricesAndVanishedSkus() {
        svc.addItem("u1", "pen", 2);
        svc.addItem("u1", "cap", 1);
        prices.put("pen", 200L);
        prices.remove("cap");
        CartView v = svc.view("u1");
        assertEquals(List.of(new CartLine("pen", 2, 200)), v.lines());
        assertEquals(400, v.totalCents());
    }

    @Test void discountCappedAtSubtotal() {
        CartService s = new CartService(sku -> OptionalLong.of(100), clock, Duration.ofHours(1), 9, 9,
                List.of(l -> 80, l -> 80, l -> -5));
        CartView v = s.addItem("u", "x", 1);
        assertEquals(100, v.discountCents());
        assertEquals(0, v.totalCents());
    }

    @Test void thresholdRule() {
        ThresholdPercentRule r = new ThresholdPercentRule(1000, 15);
        assertEquals(0, r.discountCents(List.of(new CartLine("a", 1, 999))));
        assertEquals(150, r.discountCents(List.of(new CartLine("a", 1, 1000))));
        assertEquals(0, r.discountCents(List.of()) );
        assertEquals(1, new ThresholdPercentRule(0, 15).discountCents(List.of(new CartLine("a", 1, 7)))); // 105/100 rounds down
        assertThrows(IllegalArgumentException.class, () -> new ThresholdPercentRule(0, 101));
    }

    @Test void mergeGuest() {
        svc.addItem("g", "pen", 3);
        svc.addItem("g", "book", 1);
        svc.addItem("u", "pen", 4);
        CartView v = svc.mergeGuest("g", "u");
        assertEquals("u", v.owner());
        assertEquals(List.of(new CartLine("book", 1, 1200), new CartLine("pen", 5, 150)), v.lines()); // 3+4 capped at 5
        assertTrue(svc.view("g").lines().isEmpty());
        assertEquals(v.lines(), svc.mergeGuest("g", "u").lines()); // guest is gone: no-op
    }

    @Test void mergeGuestRespectsMaxLinesAndSkuOrder() {
        svc.addItem("u", "pen", 1);
        svc.addItem("u", "cap", 1);
        svc.addItem("g", "bag", 1);
        svc.addItem("g", "book", 1);
        CartView v = svc.mergeGuest("g", "u"); // room for exactly one more line: "bag" (sku order) wins
        assertEquals(List.of("bag", "cap", "pen"), v.lines().stream().map(CartLine::sku).toList());
    }

    @Test void mergeIntoSelfAndExpiredGuest() {
        svc.addItem("u", "pen", 1);
        assertEquals(1, svc.mergeGuest("u", "u").itemCount());
        svc.addItem("g", "cap", 1);
        clock.advance(Duration.ofMinutes(31));
        assertTrue(svc.mergeGuest("g", "u").lines().isEmpty()); // both expired
    }

    @Test void concurrentAddsDoNotLoseUpdates() throws Exception {
        CartService big = new CartService(sku -> OptionalLong.of(1), clock, Duration.ofHours(1), 1_000_000, 10, List.of());
        ExecutorService ex = Executors.newFixedThreadPool(8);
        List<Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) fs.add(ex.submit(() -> { for (int i = 0; i < 5000; i++) big.addItem("shared", "pen", 1); }));
        for (Future<?> f : fs) f.get(10, TimeUnit.SECONDS);
        ex.shutdown();
        assertEquals(40_000, big.view("shared").itemCount());
    }

    @Test void concurrentCrossMergesDoNotDeadlock() throws Exception {
        CartService big = new CartService(sku -> OptionalLong.of(1), clock, Duration.ofHours(1), 1_000_000, 10, List.of());
        big.addItem("a", "pen", 1);
        big.addItem("b", "pen", 1);
        ExecutorService ex = Executors.newFixedThreadPool(2);
        Future<?> f1 = ex.submit(() -> { for (int i = 0; i < 20000; i++) { big.mergeGuest("a", "b"); big.addItem("a", "pen", 1); } });
        Future<?> f2 = ex.submit(() -> { for (int i = 0; i < 20000; i++) { big.mergeGuest("b", "a"); big.addItem("b", "pen", 1); } });
        f1.get(10, TimeUnit.SECONDS);
        f2.get(10, TimeUnit.SECONDS);
        ex.shutdown();
    }
}
