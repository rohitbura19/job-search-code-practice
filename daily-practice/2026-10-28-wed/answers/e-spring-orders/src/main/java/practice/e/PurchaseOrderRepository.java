package practice.e;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The entity graph fetches lines with one join instead of one extra query per access (N+1 is avoided by design, not by luck).
 * Never combine a collection fetch join with a Pageable: Hibernate paginates in memory; that is why list() returns summaries
 * without lines. The aggregate runs in the database: one row per customer, not all lines in memory.
 */
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    @EntityGraph(attributePaths = "lines")
    Optional<PurchaseOrder> findByOrderNo(String orderNo);

    boolean existsByOrderNo(String orderNo);

    Page<PurchaseOrder> findByCustomerIdAndStatusIn(String customerId, Collection<OrderStatus> statuses, Pageable pageable);

    @Query("""
            select new practice.e.CustomerTotal(o.customerId, sum(l.quantity * l.unitPricePaise))
            from PurchaseOrder o join o.lines l
            where o.status <> practice.e.OrderStatus.CANCELLED
            group by o.customerId
            order by sum(l.quantity * l.unitPricePaise) desc, o.customerId asc
            """)
    List<CustomerTotal> topCustomers(Pageable limit);
}
