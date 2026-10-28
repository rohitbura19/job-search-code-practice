package practice.e;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// TODO: @Entity @Table(name = "purchase_order") with a NAMED unique constraint uk_order_no on order_no ("order" is a reserved word);
//       @Id with IDENTITY generation; @Enumerated(STRING) status; snake_case @Column names;
//       lines: @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true) ordered by id.
public class PurchaseOrder {
    private Long id;
    private String orderNo;
    private String customerId;
    private OrderStatus status = OrderStatus.NEW;
    private Instant createdAt;
    private List<OrderLine> lines = new ArrayList<>();

    protected PurchaseOrder() {}

    public PurchaseOrder(String orderNo, String customerId, Instant createdAt) {
        this.orderNo = orderNo;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getOrderNo() { return orderNo; }
    public String getCustomerId() { return customerId; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public List<OrderLine> getLines() { return lines; }

    /** Adds the line and keeps BOTH sides of the association in sync. */
    public void addLine(OrderLine line) { throw new UnsupportedOperationException("TODO"); }

    /** Sum of quantity * unit price over the lines. */
    public long totalPaise() { throw new UnsupportedOperationException("TODO"); }

    /** Only NEW or PAID orders can be cancelled; otherwise IllegalStateTransitionException (to CANCELLED). */
    public void cancel() { throw new UnsupportedOperationException("TODO"); }
}
