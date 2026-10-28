package practice.e;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    // TODO: load the order TOGETHER with its lines in one query (no lazy loading later): @EntityGraph.
    Optional<PurchaseOrder> findByOrderNo(String orderNo);

    boolean existsByOrderNo(String orderNo);

    // TODO: derived query: this customer's orders whose status is in the given collection, paged.
    Page<PurchaseOrder> findByCustomerIdAndStatusIn(String customerId, Collection<OrderStatus> statuses, Pageable pageable);

    // TODO: @Query (JPQL, constructor expression `select new practice.e.CustomerTotal(...)`): per customer the sum of
    //       quantity * unitPricePaise over the lines of orders that are NOT cancelled, biggest total first, ties by customerId.
    //       The Pageable only limits the number of rows (top N).
    List<CustomerTotal> topCustomers(Pageable limit);
}
