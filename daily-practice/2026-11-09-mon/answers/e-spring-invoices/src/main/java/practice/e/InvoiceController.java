package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService service;

    public InvoiceController(InvoiceService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Views.InvoiceView> create(@Valid @RequestBody CreateInvoiceRequest r) {
        Views.InvoiceView v = service.create(r);
        return ResponseEntity.created(URI.create("/api/invoices/" + v.id())).body(v);
    }

    @GetMapping("/{id}")
    public Views.InvoiceView get(@PathVariable @Positive Long id) { return service.get(id); }

    @GetMapping
    public ResponseEntity<List<Views.InvoiceSummary>> list(@RequestParam InvoiceStatus status,
                                                          @RequestParam(defaultValue = "0") @Min(0) int page,
                                                          @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Page<Views.InvoiceSummary> p = service.list(status, PageRequest.of(page, size, Sort.by("id")));
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(p.getTotalElements())).body(p.getContent());
    }

    @PostMapping("/{id}/issue")
    public Views.InvoiceView issue(@PathVariable @Positive Long id) { return service.issue(id); }

    @PostMapping("/{id}/pay")
    public Views.InvoiceView pay(@PathVariable @Positive Long id) { return service.pay(id); }

    @PostMapping("/{id}/void")
    public Views.InvoiceView cancel(@PathVariable @Positive Long id) { return service.cancel(id); }

    @GetMapping("/revenue")
    public List<CustomerTotal> revenue() { return service.revenue(); }
}
