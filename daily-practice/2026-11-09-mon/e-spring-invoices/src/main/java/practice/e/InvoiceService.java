package practice.e;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TODO(E4): make this a Spring service. The class is read-only transactional by default; every method that writes overrides that.
 *  - create: new DRAFT invoice with createdAt = clock.instant(); returns the view.
 *  - get: view, or NotFoundException("Invoice <id> not found").
 *  - list: page of summaries for a status.
 *  - issue / pay / cancel: move to ISSUED / PAID / VOID and set issuedAt (= clock.instant()) only when issuing. Moving to the CURRENT status is a
 *    no-op that returns the view (idempotent retries, issuedAt untouched). An illegal move: ConflictException("Invoice <id> cannot move from X to Y").
 *  - voidStaleDrafts: voids DRAFTs older than maxAge (relative to the clock) with the bulk update; returns the count.
 *  - revenue: totals per customer of PAID invoices.
 */
public class InvoiceService {
    public InvoiceService(InvoiceRepository repo, Clock clock) {}

    public Views.InvoiceView create(CreateInvoiceRequest r) { throw new UnsupportedOperationException("TODO"); }
    public Views.InvoiceView get(Long id) { throw new UnsupportedOperationException("TODO"); }
    public Page<Views.InvoiceSummary> list(InvoiceStatus status, Pageable pageable) { throw new UnsupportedOperationException("TODO"); }
    public Views.InvoiceView issue(Long id) { throw new UnsupportedOperationException("TODO"); }
    public Views.InvoiceView pay(Long id) { throw new UnsupportedOperationException("TODO"); }
    public Views.InvoiceView cancel(Long id) { throw new UnsupportedOperationException("TODO"); }
    public int voidStaleDrafts(Duration maxAge) { throw new UnsupportedOperationException("TODO"); }
    public List<CustomerTotal> revenue() { throw new UnsupportedOperationException("TODO"); }
}
