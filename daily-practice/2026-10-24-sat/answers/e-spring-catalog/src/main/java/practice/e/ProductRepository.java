package practice.e;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Derived query + Pageable: Spring adds LIMIT/OFFSET, the sort from the Pageable, and a count query for Page.getTotalElements().
    Page<Product> findByCategoryAndActiveTrue(String category, Pageable pageable);

    boolean existsBySku(String sku);

    @Query("select p.category as category, count(p) as total from Product p where p.active = true group by p.category order by p.category")
    List<CategoryCount> countByCategory();

    // Soft delete as one conditional statement: 1 = deactivated now, 0 = missing or already inactive.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Product p set p.active = false where p.id = :id and p.active = true")
    int deactivate(@Param("id") long id);
}
