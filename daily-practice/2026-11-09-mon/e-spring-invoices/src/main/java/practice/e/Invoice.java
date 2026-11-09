package practice.e;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO(E1c): make this an entity.
 *  - id: IDENTITY generated. customer: not null, length 40.
 *  - status: stored as its NAME (not the ordinal), not null, length 10.
 *  - items: @ElementCollection of LineItem in table "invoice_line" (join column invoice_id), keeping list order in a column "position".
 *  - createdAt, issuedAt: plain Instant columns (issuedAt nullable).
 */
public class Invoice {
    private Long id;
    private String customer;
    private InvoiceStatus status = InvoiceStatus.DRAFT;
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
