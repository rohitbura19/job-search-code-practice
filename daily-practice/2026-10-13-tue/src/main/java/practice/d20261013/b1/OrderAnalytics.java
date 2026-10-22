package practice.d20261013.b1;

import java.util.List;
import java.util.Map;

/** B1 - see questions/B1-order-analytics-lru.md. Test: OrderAnalyticsTest. Use streams/Collectors. */
public final class OrderAnalytics {
    private OrderAnalytics() {}

    /** Top 3 customerIds by total revenue (qty * price); ties broken by customerId ascending. Fewer than 3 customers: return all. */
    public static List<String> top3Customers(List<OrderLine> lines) {
        throw new UnsupportedOperationException("TODO");
    }

    /** city (sorted) -> comma separated distinct SKUs (sorted), e.g. "A1,B2". */
    public static Map<String, String> skusByCity(List<OrderLine> lines) {
        throw new UnsupportedOperationException("TODO");
    }

    /** sku -> number of lines; iteration order = count descending, then sku ascending (the returned map must keep that order). */
    public static Map<String, Long> countBySku(List<OrderLine> lines) {
        throw new UnsupportedOperationException("TODO");
    }

    /** orderId of the first line with qty > 10, or "NONE". Do not call Optional.get(). */
    public static String firstBigOrder(List<OrderLine> lines) {
        throw new UnsupportedOperationException("TODO");
    }
}
