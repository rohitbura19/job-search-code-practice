package practice.e;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(name = "stock_item", uniqueConstraints = @UniqueConstraint(name = "uk_stock_sku", columnNames = "sku"))
public class StockItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 32) private String sku;
    @Column(nullable = false) private int onHand;
    @Column(nullable = false) private int reserved;
    @Version private long version;

    protected StockItem() {}

    public StockItem(String sku, int onHand, int reserved) { this.sku = sku; this.onHand = onHand; this.reserved = reserved; }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public int getOnHand() { return onHand; }
    public int getReserved() { return reserved; }
    public long getVersion() { return version; }
    public int available() { return onHand - reserved; }
}
