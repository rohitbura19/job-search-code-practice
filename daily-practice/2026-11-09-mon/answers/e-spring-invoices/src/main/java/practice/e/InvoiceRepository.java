package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);

    @Query("select new practice.e.CustomerTotal(i.customer, sum(li.quantity * li.unitPriceCents)) "
            + "from Invoice i join i.items li where i.status = :status group by i.customer order by i.customer")
    List<CustomerTotal> totalsByCustomer(@Param("status") InvoiceStatus status);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Invoice i set i.status = practice.e.InvoiceStatus.VOID "
            + "where i.status = practice.e.InvoiceStatus.DRAFT and i.createdAt < :before")
    int voidStaleDrafts(@Param("before") Instant before);

    List<Invoice> findTop3ByCustomerOrderByCreatedAtDesc(String customer);
}
