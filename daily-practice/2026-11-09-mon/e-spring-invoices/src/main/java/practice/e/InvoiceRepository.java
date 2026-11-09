package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /** TODO(E2a): derived query, invoices with the given status, paged. (nothing to add except making sure the name is right) */
    Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);

    /**
     * TODO(E2b): JPQL with a constructor expression: new practice.e.CustomerTotal(customer, sum(quantity * unitPriceCents)) over
     *  the invoices with the given status (join the items collection), grouped by customer, ordered by customer.
     */
    List<CustomerTotal> totalsByCustomer(InvoiceStatus status);

    /**
     * TODO(E2c): bulk JPQL update: set status VOID on every DRAFT invoice created before the given instant; return the row count.
     *  The persistence context must be flushed first and cleared afterwards, otherwise stale entities are returned.
     */
    int voidStaleDrafts(Instant before);

    // TODO(E2d): derived query without @Query: the 3 newest invoices of a customer (by createdAt, newest first).
    //            Name it findTop3ByCustomerOrderByCreatedAtDesc (return List<Invoice>); the service does not use it, the test does.
}
