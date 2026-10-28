package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class OrderRepositoryTest {
    static final Instant T0 = Instant.parse("2026-10-28T04:00:00Z");
    @Autowired PurchaseOrderRepository repo;
    @Autowired EntityManager em;

    static PurchaseOrder order(String no, String customer, int minute, OrderStatus status, long... qtyAndPrice) {
        PurchaseOrder o = new PurchaseOrder(no, customer, T0.plusSeconds(60L * minute));
        for (int i = 0; i < qtyAndPrice.length; i += 2) o.addLine(new OrderLine("sku-" + i, (int) qtyAndPrice[i], qtyAndPrice[i + 1]));
        if (status == OrderStatus.CANCELLED) o.cancel();
        return o;
    }

    @BeforeEach void seed() {
        repo.save(order("ORD-000001", "asha", 1, OrderStatus.NEW, 2, 500, 1, 1000));      // 2000
        repo.save(order("ORD-000002", "asha", 2, OrderStatus.CANCELLED, 10, 9999));        // cancelled: ignored in totals
        repo.save(order("ORD-000003", "asha", 3, OrderStatus.NEW, 1, 300));                // 300
        repo.save(order("ORD-000004", "ben", 4, OrderStatus.NEW, 3, 1000));                // 3000
        repo.save(order("ORD-000005", "cy", 5, OrderStatus.NEW, 1, 2000));                 // 2000
        repo.saveAndFlush(order("ORD-000006", "ben", 6, OrderStatus.NEW));                 // no lines: absent from the join
        em.clear();
    }

    @Test void entityGraphLoadsLinesInOrder() {
        PurchaseOrder o = repo.findByOrderNo("ORD-000001").orElseThrow();
        PersistenceUnitUtil util = em.getEntityManagerFactory().getPersistenceUnitUtil();
        assertTrue(util.isLoaded(o, "lines"), "lines must be fetched by the entity graph");
        assertEquals(2, o.getLines().size());
        assertEquals(2000, o.totalPaise());
        assertEquals(List.of(2, 1), o.getLines().stream().map(OrderLine::getQuantity).toList());
        assertTrue(repo.findByOrderNo("ORD-999999").isEmpty());
    }

    @Test void existsByOrderNo() {
        assertTrue(repo.existsByOrderNo("ORD-000004"));
        assertFalse(repo.existsByOrderNo("ORD-000099"));
    }

    @Test void duplicateOrderNoViolatesTheUniqueConstraint() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(new PurchaseOrder("ORD-000001", "dup", T0)));
    }

    @Test void statusIsStoredAsText() {
        Object raw = em.createNativeQuery("select status from purchase_order where order_no = 'ORD-000002'").getSingleResult();
        assertEquals("CANCELLED", raw);
    }

    @Test void pagingFiltersByCustomerAndStatus() {
        PageRequest p = PageRequest.of(0, 2, Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")));
        Page<PurchaseOrder> all = repo.findByCustomerIdAndStatusIn("asha", EnumSet.allOf(OrderStatus.class), p);
        assertEquals(3, all.getTotalElements());
        assertEquals(2, all.getTotalPages());
        assertEquals(List.of("ORD-000003", "ORD-000002"), all.map(PurchaseOrder::getOrderNo).getContent());
        Page<PurchaseOrder> cancelled = repo.findByCustomerIdAndStatusIn("asha", Set.of(OrderStatus.CANCELLED), p);
        assertEquals(List.of("ORD-000002"), cancelled.map(PurchaseOrder::getOrderNo).getContent());
        assertTrue(repo.findByCustomerIdAndStatusIn("nobody", EnumSet.allOf(OrderStatus.class), p).isEmpty());
    }

    @Test void topCustomersSumsLinesOfNonCancelledOrders() {
        List<CustomerTotal> top = repo.topCustomers(PageRequest.of(0, 10));
        assertEquals(List.of(new CustomerTotal("ben", 3000), new CustomerTotal("asha", 2300), new CustomerTotal("cy", 2000)), top);
        assertEquals(List.of(new CustomerTotal("ben", 3000)), repo.topCustomers(PageRequest.of(0, 1)));
    }

    @Test void orphanRemovalDeletesRemovedLines() {
        PurchaseOrder o = repo.findByOrderNo("ORD-000001").orElseThrow();
        o.getLines().remove(0);
        repo.flush();
        em.clear();
        assertEquals(1, repo.findByOrderNo("ORD-000001").orElseThrow().getLines().size());
        Number count = (Number) em.createNativeQuery("select count(*) from order_line").getSingleResult();
        assertEquals(5, count.intValue());
    }

    @Test void deletingTheOrderCascadesToLines() {
        repo.delete(repo.findByOrderNo("ORD-000004").orElseThrow());
        repo.flush();
        Number count = (Number) em.createNativeQuery("select count(*) from order_line where sku = 'sku-0' and quantity = 3").getSingleResult();
        assertEquals(0, count.intValue());
    }

    @Test void cancelRules() {
        PurchaseOrder o = new PurchaseOrder("ORD-000100", "x", T0);
        o.cancel();
        assertEquals(OrderStatus.CANCELLED, o.getStatus());
        assertThrows(IllegalStateTransitionException.class, o::cancel);
    }
}
