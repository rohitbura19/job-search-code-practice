package practice.e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class StockRepositoryTest {
    @Autowired StockRepository repo;

    @BeforeEach void seed() {
        repo.save(new StockItem("AAA-1", 10, 8));     // available 2
        repo.save(new StockItem("BBB-2", 10, 5));     // available 5
        repo.save(new StockItem("CCC-3", 10, 4));     // available 6
        repo.save(new StockItem("DDD-4", 100, 0));    // available 100
    }

    @Test void findBySku() {
        assertEquals(10, repo.findBySku("BBB-2").orElseThrow().getOnHand());
        assertTrue(repo.findBySku("nope").isEmpty());
    }

    @Test void skuIsUnique() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(new StockItem("AAA-1", 1, 0)));
    }

    @Test void reserveSucceedsAndBumpsVersion() {
        long v0 = repo.findBySku("AAA-1").orElseThrow().getVersion();
        assertEquals(1, repo.reserve("AAA-1", 2));                       // exactly the available amount
        StockItem i = repo.findBySku("AAA-1").orElseThrow();
        assertEquals(10, i.getReserved());
        assertEquals(0, i.available());
        assertEquals(v0 + 1, i.getVersion());
    }

    @Test void reserveMoreThanAvailableChangesNothing() {
        assertEquals(0, repo.reserve("AAA-1", 3));
        assertEquals(8, repo.findBySku("AAA-1").orElseThrow().getReserved());
        assertEquals(0, repo.reserve("UNKNOWN", 1));
    }

    @Test void restock() {
        assertEquals(1, repo.restock("AAA-1", 5));
        assertEquals(15, repo.findBySku("AAA-1").orElseThrow().getOnHand());
        assertEquals(0, repo.restock("UNKNOWN", 5));
    }

    @Test void lowStockIsPagedAndOrderedBySku() {
        Page<StockItem> p0 = repo.findLowStock(5, PageRequest.of(0, 1));
        assertEquals(2, p0.getTotalElements());
        assertEquals(2, p0.getTotalPages());
        assertEquals("AAA-1", p0.getContent().get(0).getSku());
        assertEquals("BBB-2", repo.findLowStock(5, PageRequest.of(1, 1)).getContent().get(0).getSku());
        assertEquals(4, repo.findLowStock(1000, PageRequest.of(0, 10)).getTotalElements());
        assertEquals(0, repo.findLowStock(1, PageRequest.of(0, 10)).getTotalElements());
    }

    @Test void totalReservedAggregate() {
        assertEquals(17L, repo.totalReserved());
        repo.deleteAll();
        assertEquals(0L, repo.totalReserved());       // coalesce: no rows -> 0, not null
    }
}
