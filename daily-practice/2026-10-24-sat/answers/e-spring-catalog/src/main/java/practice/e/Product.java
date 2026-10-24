package practice.e;

import jakarta.persistence.*;

@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(name = "uk_product_sku", columnNames = "sku"),
        indexes = @Index(name = "ix_product_category", columnList = "category, name"))
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String sku;
    @Column(nullable = false, length = 80)
    private String name;
    @Column(nullable = false, length = 40)
    private String category;
    @Column(nullable = false)
    private long priceCents;
    @Column(nullable = false)
    private boolean active = true;

    protected Product() {}

    public Product(String sku, String name, String category, long priceCents) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.priceCents = priceCents;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public long getPriceCents() { return priceCents; }
    public boolean isActive() { return active; }
}
