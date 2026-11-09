package practice.e;

import java.time.Instant;
import java.util.List;

public final class Views {
    private Views() {}

    public record InvoiceView(Long id, String customer, InvoiceStatus status, List<LineItem> items, long totalCents, Instant issuedAt) {
        public static InvoiceView of(Invoice i) {
            return new InvoiceView(i.getId(), i.getCustomer(), i.getStatus(), List.copyOf(i.getItems()), i.totalCents(), i.getIssuedAt());
        }
    }

    public record InvoiceSummary(Long id, String customer, InvoiceStatus status) {
        public static InvoiceSummary of(Invoice i) { return new InvoiceSummary(i.getId(), i.getCustomer(), i.getStatus()); }
    }
}
