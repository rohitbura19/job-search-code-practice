package practice.e;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockItem, Long> {
    Optional<StockItem> findBySku(String sku);

    /** TODO JPQL: items whose (onHand - reserved) <= threshold, ordered by sku. */
    Page<StockItem> findLowStock(int threshold, Pageable pageable);

    /**
     * TODO bulk JPQL update, atomic check-and-reserve in ONE statement: reserved += qty and version += 1 WHERE sku matches AND
     * onHand - reserved >= qty. Returns rows changed (0 or 1). Flush before and clear the persistence context after the update.
     */
    int reserve(String sku, int qty);

    /** TODO bulk JPQL update: onHand += qty, version += 1 for the sku. Returns rows changed. Flush/clear as above. */
    int restock(String sku, int qty);

    /** TODO JPQL aggregate: sum of reserved over all items, 0 (not null) when there are none. */
    long totalReserved();
}
