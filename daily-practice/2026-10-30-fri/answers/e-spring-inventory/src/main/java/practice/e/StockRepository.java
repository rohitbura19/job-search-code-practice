package practice.e;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<StockItem, Long> {
    Optional<StockItem> findBySku(String sku);

    @Query("select s from StockItem s where s.onHand - s.reserved <= :threshold order by s.sku")
    Page<StockItem> findLowStock(@Param("threshold") int threshold, Pageable pageable);

    /** Atomic check-and-reserve: the WHERE clause is the check, so two concurrent callers cannot oversell. Returns rows changed (0 or 1). */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update StockItem s set s.reserved = s.reserved + :qty, s.version = s.version + 1 "
            + "where s.sku = :sku and s.onHand - s.reserved >= :qty")
    int reserve(@Param("sku") String sku, @Param("qty") int qty);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update StockItem s set s.onHand = s.onHand + :qty, s.version = s.version + 1 where s.sku = :sku")
    int restock(@Param("sku") String sku, @Param("qty") int qty);

    @Query("select coalesce(sum(s.reserved), 0) from StockItem s")
    long totalReserved();
}
