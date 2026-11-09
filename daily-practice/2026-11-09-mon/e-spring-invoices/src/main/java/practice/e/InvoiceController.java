package practice.e;

import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * TODO(E5): REST controller under /api/invoices. Do NOT put @Validated on the class (Spring 6.1 built-in method validation is used).
 *   POST /api/invoices                 @Valid body -> 201 + Location /api/invoices/{id} + InvoiceView
 *   GET  /api/invoices/{id}            id must be positive
 *   GET  /api/invoices?status=&page=0&size=20   status REQUIRED (an unknown value -> 400); page >= 0, size 1..100 (400 otherwise);
 *                                      sorted by id; body = list of InvoiceSummary, header X-Total-Count = total elements
 *   POST /api/invoices/{id}/issue | /pay | /void    -> 200 + InvoiceView (id must be positive)
 *   GET  /api/invoices/revenue         list of CustomerTotal (a literal path next to /{id})
 */
public class InvoiceController {
    public InvoiceController(InvoiceService service) {}

    public ResponseEntity<Views.InvoiceView> create(CreateInvoiceRequest r) { throw new UnsupportedOperationException("TODO"); }
    public Views.InvoiceView get(Long id) { throw new UnsupportedOperationException("TODO"); }
    public ResponseEntity<List<Views.InvoiceSummary>> list(InvoiceStatus status, int page, int size) { throw new UnsupportedOperationException("TODO"); }
    public Views.InvoiceView issue(Long id) { throw new UnsupportedOperationException("TODO"); }
    public Views.InvoiceView pay(Long id) { throw new UnsupportedOperationException("TODO"); }
    public Views.InvoiceView cancel(Long id) { throw new UnsupportedOperationException("TODO"); }
    public List<CustomerTotal> revenue() { throw new UnsupportedOperationException("TODO"); }
}
