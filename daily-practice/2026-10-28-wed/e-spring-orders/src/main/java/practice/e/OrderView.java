package practice.e;

import java.util.List;

public record OrderView(String orderNo, String customerId, OrderStatus status, long totalPaise, List<LineView> lines) {
    static OrderView from(PurchaseOrder o) {
        return new OrderView(o.getOrderNo(), o.getCustomerId(), o.getStatus(), o.totalPaise(), o.getLines().stream().map(LineView::from).toList());
    }
}
