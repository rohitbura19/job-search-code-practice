package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

@DataJpaTest
@Import({InvoiceService.class, InvoiceServiceTest.Cfg.class})
class InvoiceServiceTest {
    static final Instant NOW = Instant.parse("2026-11-09T12:00:00Z");

    @TestConfiguration
    static class Cfg { @Bean @Primary Clock fixedClock() { return Clock.fixed(NOW, ZoneOffset.UTC); } }

    @Autowired InvoiceService service;
    @Autowired InvoiceRepository repo;
    @Autowired EntityManager em;

    private static CreateInvoiceRequest req(String customer, long... prices) {
        return new CreateInvoiceRequest(customer, java.util.Arrays.stream(prices).mapToObj(p -> new CreateInvoiceRequest.Item("item", 2, p)).toList());
    }

    @Test void statusTransitionTable() {
        assertTrue(InvoiceStatus.DRAFT.canMoveTo(InvoiceStatus.ISSUED));
        assertTrue(InvoiceStatus.DRAFT.canMoveTo(InvoiceStatus.VOID));
        assertFalse(InvoiceStatus.DRAFT.canMoveTo(InvoiceStatus.PAID));
        assertTrue(InvoiceStatus.ISSUED.canMoveTo(InvoiceStatus.PAID));
        assertTrue(InvoiceStatus.ISSUED.canMoveTo(InvoiceStatus.VOID));
        assertFalse(InvoiceStatus.ISSUED.canMoveTo(InvoiceStatus.DRAFT));
        for (InvoiceStatus s : InvoiceStatus.values()) {
            assertFalse(InvoiceStatus.PAID.canMoveTo(s));
            assertFalse(InvoiceStatus.VOID.canMoveTo(s));
        }
    }

    @Test void createStoresDraftWithTotal() {
        Views.InvoiceView v = service.create(req("Acme", 100, 250));
        assertNotNull(v.id());
        assertEquals(InvoiceStatus.DRAFT, v.status());
        assertEquals(700, v.totalCents());
        assertNull(v.issuedAt());
        assertEquals(NOW, repo.findById(v.id()).orElseThrow().getCreatedAt());
    }

    @Test void getUnknownIs404Exception() {
        NotFoundException e = assertThrows(NotFoundException.class, () -> service.get(999L));
        assertEquals("Invoice 999 not found", e.getMessage());
    }

    @Test void lifecycleAndIdempotency() {
        Long id = service.create(req("Acme", 100)).id();
        Views.InvoiceView issued = service.issue(id);
        assertEquals(InvoiceStatus.ISSUED, issued.status());
        assertEquals(NOW, issued.issuedAt());
        assertEquals(InvoiceStatus.ISSUED, service.issue(id).status(), "retry is a no-op");
        assertEquals(InvoiceStatus.PAID, service.pay(id).status());
        assertEquals(InvoiceStatus.PAID, service.pay(id).status());
        assertEquals(NOW, service.get(id).issuedAt());
    }

    @Test void illegalMovesAreConflicts() {
        Long id = service.create(req("Acme", 100)).id();
        ConflictException e = assertThrows(ConflictException.class, () -> service.pay(id));
        assertEquals("Invoice " + id + " cannot move from DRAFT to PAID", e.getMessage());
        service.cancel(id);
        assertThrows(ConflictException.class, () -> service.issue(id));
        assertThrows(NotFoundException.class, () -> service.cancel(12345L));
    }

    @Test void listReturnsSummariesAndTotal() {
        for (int i = 0; i < 3; i++) service.create(req("C" + i, 10));
        Page<Views.InvoiceSummary> p = service.list(InvoiceStatus.DRAFT, PageRequest.of(0, 2));
        assertEquals(2, p.getContent().size());
        assertEquals(3, p.getTotalElements());
    }

    @Test void staleDraftsAreVoidedRelativeToClock() {
        repo.save(new Invoice("Old", List.of(new LineItem("x", 1, 1)), NOW.minus(Duration.ofDays(10))));
        repo.save(new Invoice("Fresh", List.of(new LineItem("x", 1, 1)), NOW.minus(Duration.ofDays(1))));
        em.flush();
        assertEquals(1, service.voidStaleDrafts(Duration.ofDays(7)));
        assertEquals(0, service.voidStaleDrafts(Duration.ofDays(7)));
    }

    @Test void revenueOnlyCountsPaid() {
        Long a = service.create(req("Acme", 100)).id();
        Long b = service.create(req("Acme", 50)).id();
        service.create(req("Zeta", 999));
        service.issue(a); service.pay(a);
        service.issue(b);
        em.flush(); em.clear();
        assertEquals(List.of(new CustomerTotal("Acme", 200)), service.revenue());
    }
}
