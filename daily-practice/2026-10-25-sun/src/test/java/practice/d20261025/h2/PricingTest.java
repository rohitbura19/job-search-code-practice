package practice.d20261025.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class PricingTest {
    static Order o(long p) { return new Order("o" + p, p); }

    static class Counting implements Pricing {
        final AtomicInteger calls = new AtomicInteger();
        final Pricing inner = new BasePricing();
        public long priceOf(Order order) { calls.incrementAndGet(); return inner.priceOf(order); }
    }

    @Test void orderValidates() {
        assertThrows(IllegalArgumentException.class, () -> new Order(" ", 1));
        assertThrows(IllegalArgumentException.class, () -> new Order(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new Order("x", -1));
        assertEquals(new Order("x", 5), new Order("x", 5));
    }

    @Test void baseIsTheSubtotal() { assertEquals(12345, new BasePricing().priceOf(o(12345))); }

    @Test void discountRoundsTheDiscountDown() {
        Pricing d = new DiscountPricing(new BasePricing(), 10);
        assertEquals(900, d.priceOf(o(1000)));
        assertEquals(9, d.priceOf(o(10)));
        assertEquals(5, d.priceOf(o(5)), "discount of 0.5 paisa rounds down to 0");
        assertEquals(0, d.priceOf(o(0)));
        assertEquals(1000, new DiscountPricing(new BasePricing(), 0).priceOf(o(1000)));
        assertEquals(0, new DiscountPricing(new BasePricing(), 100).priceOf(o(1000)));
        assertThrows(IllegalArgumentException.class, () -> new DiscountPricing(new BasePricing(), -1));
        assertThrows(IllegalArgumentException.class, () -> new DiscountPricing(new BasePricing(), 101));
        assertThrows(NullPointerException.class, () -> new DiscountPricing(null, 5));
    }

    @Test void taxRoundsHalfUpAndDetectsOverflow() {
        Pricing t = new TaxPricing(new BasePricing(), 1800);
        assertEquals(1180, t.priceOf(o(1000)));
        assertEquals(3, new TaxPricing(new BasePricing(), 5000).priceOf(o(2)), "tax 1.00");
        assertEquals(5, new TaxPricing(new BasePricing(), 5000).priceOf(o(3)), "tax 1.5 -> 2");
        assertEquals(3, new TaxPricing(new BasePricing(), 1000).priceOf(o(3)), "tax 0.3 -> 0");
        assertEquals(11, new TaxPricing(new BasePricing(), 500).priceOf(o(10)), "tax 0.5 -> 1");
        assertThrows(ArithmeticException.class, () -> new TaxPricing(new BasePricing(), 1800).priceOf(o(Long.MAX_VALUE / 2)));
        assertThrows(IllegalArgumentException.class, () -> new TaxPricing(new BasePricing(), -1));
    }

    @Test void capLimitsFromAbove() {
        Pricing c = new CappedPricing(new BasePricing(), 500);
        assertEquals(499, c.priceOf(o(499)));
        assertEquals(500, c.priceOf(o(500)));
        assertEquals(500, c.priceOf(o(10_000)));
        assertEquals(0, new CappedPricing(new BasePricing(), 0).priceOf(o(10)));
        assertThrows(IllegalArgumentException.class, () -> new CappedPricing(new BasePricing(), -1));
    }

    @Test void decoratorsComposeAndOrderMatters() {
        Pricing discountThenTax = new TaxPricing(new DiscountPricing(new BasePricing(), 10), 1800);   // 1000 -> 900 -> 1062
        Pricing taxThenDiscount = new DiscountPricing(new TaxPricing(new BasePricing(), 1800), 10);   // 1000 -> 1180 -> 1062
        assertEquals(1062, discountThenTax.priceOf(o(1000)));
        assertEquals(1063, taxThenDiscount.priceOf(o(1001)) , "1001 -> 1181 (tax 180.18 -> 180) -> discount floor(118.1) = 118 -> 1063");
        Pricing capLast = new CappedPricing(new TaxPricing(new BasePricing(), 1800), 1100);
        Pricing capFirst = new TaxPricing(new CappedPricing(new BasePricing(), 1100), 1800);
        assertEquals(1100, capLast.priceOf(o(1000)));
        assertEquals(1298, capFirst.priceOf(o(5000)));
    }

    @Test void cachingCallsTheInnerOncePerDistinctOrder() {
        Counting inner = new Counting();
        Pricing p = new CachingPricing(new TaxPricing(inner, 1800));
        assertEquals(1180, p.priceOf(o(1000)));
        assertEquals(1180, p.priceOf(new Order("o1000", 1000)), "an equal order hits the cache");
        assertEquals(1, inner.calls.get());
        p.priceOf(o(2000));
        assertEquals(2, inner.calls.get());
        p.priceOf(new Order("other", 1000));
        assertEquals(3, inner.calls.get(), "same subtotal, different id = different order");
    }

    @Test void cachingDoesNotRememberFailures() {
        AtomicInteger calls = new AtomicInteger();
        Pricing flaky = order -> { if (calls.incrementAndGet() == 1) throw new IllegalStateException("down"); return 7; };
        Pricing p = new CachingPricing(flaky);
        assertThrows(IllegalStateException.class, () -> p.priceOf(o(1)));
        assertEquals(7, p.priceOf(o(1)));
        assertEquals(7, p.priceOf(o(1)));
        assertEquals(2, calls.get());
    }

    @Test void cachingIsThreadSafe() throws Exception {
        Counting inner = new Counting();
        Pricing p = new CachingPricing(inner);
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) fs.add(pool.submit(() -> { for (int i = 0; i < 20_000; i++) p.priceOf(o(i % 50)); }));
        for (Future<?> f : fs) f.get(10, TimeUnit.SECONDS);
        pool.shutdown();
        assertEquals(50, inner.calls.get());
    }
}
