package practice.e;

import java.time.Instant;

/** List row: no lines (they would be lazy-loaded one query per order). */
public record OrderSummary(String orderNo, OrderStatus status, Instant createdAt) {
    static OrderSummary from(PurchaseOrder o) { return new OrderSummary(o.getOrderNo(), o.getStatus(), o.getCreatedAt()); }
}
