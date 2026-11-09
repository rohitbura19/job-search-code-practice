package practice.e;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

@Service
@Transactional(readOnly = true)
public class InvoiceService {
    private final InvoiceRepository repo;
    private final Clock clock;

    public InvoiceService(InvoiceRepository repo, Clock clock) { this.repo = repo; this.clock = clock; }

    @Transactional
    public Views.InvoiceView create(CreateInvoiceRequest r) {
        var items = r.items().stream().map(CreateInvoiceRequest.Item::toLineItem).toList();
        return Views.InvoiceView.of(repo.save(new Invoice(r.customer(), items, clock.instant())));
    }

    public Views.InvoiceView get(Long id) { return Views.InvoiceView.of(find(id)); }

    public Page<Views.InvoiceSummary> list(InvoiceStatus status, Pageable pageable) {
        return repo.findByStatus(status, pageable).map(Views.InvoiceSummary::of);
    }

    @Transactional public Views.InvoiceView issue(Long id) { return move(id, InvoiceStatus.ISSUED); }
    @Transactional public Views.InvoiceView pay(Long id) { return move(id, InvoiceStatus.PAID); }
    @Transactional public Views.InvoiceView cancel(Long id) { return move(id, InvoiceStatus.VOID); }

    @Transactional
    public int voidStaleDrafts(Duration maxAge) { return repo.voidStaleDrafts(clock.instant().minus(maxAge)); }

    public List<CustomerTotal> revenue() { return repo.totalsByCustomer(InvoiceStatus.PAID); }

    private Views.InvoiceView move(Long id, InvoiceStatus next) {
        Invoice inv = find(id);
        if (inv.getStatus() == next) return Views.InvoiceView.of(inv);
        if (!inv.getStatus().canMoveTo(next))
            throw new ConflictException("Invoice " + id + " cannot move from " + inv.getStatus() + " to " + next);
        inv.setStatus(next);
        if (next == InvoiceStatus.ISSUED) inv.setIssuedAt(clock.instant());
        return Views.InvoiceView.of(inv);
    }

    private Invoice find(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Invoice " + id + " not found"));
    }
}
