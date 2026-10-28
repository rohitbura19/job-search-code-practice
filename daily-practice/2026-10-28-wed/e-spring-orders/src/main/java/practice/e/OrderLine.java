package practice.e;

// TODO: @Entity @Table(name = "order_line"); @Id with IDENTITY generation;
//       the owning side: @ManyToOne(fetch = LAZY, optional = false) @JoinColumn(name = "order_id");
//       sku (not null, length 32), quantity, unitPricePaise (@Column names in snake_case).
public class OrderLine {
    private Long id;
    private PurchaseOrder order;
    private String sku;
    private int quantity;
    private long unitPricePaise;

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
