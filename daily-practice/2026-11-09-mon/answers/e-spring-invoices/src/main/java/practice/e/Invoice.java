package practice.e;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 40)
    private String customer;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10)
    private InvoiceStatus status = InvoiceStatus.DRAFT;
    @ElementCollection
    @CollectionTable(name = "invoice_line", joinColumns = @JoinColumn(name = "invoice_id"))
    @OrderColumn(name = "position")
    private List<LineItem> items = new ArrayList<>();
    private Instant createdAt;
    private Instant issuedAt;

    protected Invoice() {}
    public Invoice(String customer, List<LineItem> items, Instant createdAt) {
        this.customer = customer; this.items = new ArrayList<>(items); this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getCustomer() { return customer; }
    public InvoiceStatus getStatus() { return status; }
    public List<LineItem> getItems() { return items; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getIssuedAt() { return issuedAt; }
    public void setStatus(InvoiceStatus status) { this.status = status; }
    public void setIssuedAt(Instant issuedAt) { this.issuedAt = issuedAt; }

    public long totalCents() { return items.stream().mapToLong(LineItem::totalCents).sum(); }
}
