package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class ProductRepositoryTest {
    @Autowired ProductRepository repo;
    long keepId, goneId;

    @BeforeEach void seed() {
        keepId = repo.save(new Product("TV-1", "Television", "tv", 50_000)).getId();
        repo.save(new Product("TV-2", "Antenna", "tv", 1_500));
        repo.save(new Product("TV-3", "Cable", "tv", 700));
        goneId = repo.save(new Product("TV-4", "Broken set", "tv", 100)).getId();
        repo.save(new Product("PH-1", "Phone", "phone", 30_000));
        repo.flush();
        assertEquals(1, repo.deactivate(goneId));
    }

    @Test void entityGetsIdAndDefaults() {
        Product p = repo.saveAndFlush(new Product("NEW-1", "Thing", "misc", 5));
        assertNotNull(p.getId());
        assertTrue(p.isActive(), "new products start active");
    }

    @Test void skuIsUnique() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(new Product("TV-1", "Other", "tv", 1)));
    }

    @Test void existsBySku() {
        assertTrue(repo.existsBySku("PH-1"));
        assertFalse(repo.existsBySku("nope"));
    }

    @Test void pagingFiltersCategoryAndInactiveAndHonoursSort() {
        Page<Product> first = repo.findByCategoryAndActiveTrue("tv", PageRequest.of(0, 2, Sort.by("name")));
        assertEquals(List.of("Antenna", "Cable"), first.map(Product::getName).getContent());
        assertEquals(3, first.getTotalElements(), "the inactive product is not counted");
        assertEquals(2, first.getTotalPages());
        Page<Product> second = repo.findByCategoryAndActiveTrue("tv", PageRequest.of(1, 2, Sort.by("name")));
        assertEquals(List.of("Television"), second.map(Product::getName).getContent());
        assertTrue(repo.findByCategoryAndActiveTrue("tv", PageRequest.of(5, 2)).isEmpty());
        assertTrue(repo.findByCategoryAndActiveTrue("none", PageRequest.of(0, 2)).isEmpty());
    }

    @Test void summaryCountsActiveProductsPerCategorySortedByCategory() {
        List<CategoryCount> s = repo.countByCategory();
        assertEquals(List.of("phone", "tv"), s.stream().map(CategoryCount::getCategory).toList());
        assertEquals(List.of(1L, 3L), s.stream().map(CategoryCount::getTotal).toList());
    }

    @Test void deactivateReturnsRowsChangedAndIsVisibleToTheNextRead() {
        assertEquals(1, repo.deactivate(keepId));
        assertEquals(0, repo.deactivate(keepId), "already inactive");
        assertEquals(0, repo.deactivate(999_999), "unknown id");
        assertFalse(repo.findById(keepId).orElseThrow().isActive(), "the persistence context was refreshed");
        assertEquals(2, repo.findByCategoryAndActiveTrue("tv", PageRequest.of(0, 10)).getTotalElements());
    }
}
