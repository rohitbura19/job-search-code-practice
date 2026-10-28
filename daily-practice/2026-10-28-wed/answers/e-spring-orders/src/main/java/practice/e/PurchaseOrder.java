package practice.e;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Aggregate root. Lines are only reachable and changed through it (cascade ALL + orphanRemoval). */
@Entity
@Table(name = "purchase_order", uniqueConstraints = @UniqueConstraint(name = "uk_order_no", columnNames = "order_no"))
public class PurchaseOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "order_no", nullable = false, length = 20) private String orderNo;
    @Column(name = "customer_id", nullable = false, length = 40) private String customerId;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 12) private OrderStatus status = OrderStatus.NEW;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) @OrderBy("id") private List<OrderLine> lines = new ArrayList<>();

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

    public void addLine(OrderLine line) {
        lines.add(line);
        line.setOrder(this);
    }

    public long totalPaise() {
        return lines.stream().mapToLong(l -> l.getQuantity() * l.getUnitPricePaise()).sum();
    }

    public void cancel() {
        if (status != OrderStatus.NEW && status != OrderStatus.PAID) throw new IllegalStateTransitionException(orderNo, status, OrderStatus.CANCELLED);
        status = OrderStatus.CANCELLED;
    }
}
