package practice.e;

import jakarta.persistence.*;

/** Owning side of the association: the FK column order_id lives here. LAZY because EAGER on @ManyToOne is the JPA default and a trap. */
@Entity
@Table(name = "order_line")
public class OrderLine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "order_id") private PurchaseOrder order;
    @Column(nullable = false, length = 32) private String sku;
    @Column(nullable = false) private int quantity;
    @Column(name = "unit_price_paise", nullable = false) private long unitPricePaise;

    protected OrderLine() {}

    public OrderLine(String sku, int quantity, long unitPricePaise) {
        this.sku = sku;
        this.quantity = quantity;
        this.unitPricePaise = unitPricePaise;
    }

    public Long getId() { return id; }
    public PurchaseOrder getOrder() { return order; }
    void setOrder(PurchaseOrder order) { this.order = order; }
    public String getSku() { return sku; }
    public int getQuantity() { return quantity; }
    public long getUnitPricePaise() { return unitPricePaise; }
}
