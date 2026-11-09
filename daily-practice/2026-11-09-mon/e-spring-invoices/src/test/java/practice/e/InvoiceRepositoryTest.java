package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
class InvoiceRepositoryTest {
    static final Instant T0 = Instant.parse("2026-11-01T10:00:00Z");
    @Autowired InvoiceRepository repo;
    @Autowired EntityManager em;
    @Autowired JdbcTemplate jdbc;

    Invoice acmeDraft, acmePaid, zetaPaid;

    private Invoice invoice(String customer, InvoiceStatus status, Instant created, LineItem... items) {
        Invoice i = new Invoice(customer, List.of(items), created);
        i.setStatus(status);
        return repo.save(i);
    }

    @BeforeEach void data() {
        acmeDraft = invoice("Acme", InvoiceStatus.DRAFT, T0, new LineItem("pen", 2, 150));
        acmePaid = invoice("Acme", InvoiceStatus.PAID, T0.plusSeconds(60), new LineItem("ink", 3, 1000), new LineItem("pad", 1, 500));
        zetaPaid = invoice("Zeta", InvoiceStatus.PAID, T0.plusSeconds(120), new LineItem("desk", 1, 20_000));
        invoice("Zeta", InvoiceStatus.VOID, T0.plusSeconds(180), new LineItem("lamp", 1, 700));
        em.flush();
        em.clear();
    }

    @Test void mappingUsesCollectionTableOrderColumnAndEnumNames() {
        assertEquals(5, jdbc.queryForObject("select count(*) from invoice_line", Integer.class));
        assertEquals("PAID", jdbc.queryForObject("select status from invoice where id = ?", String.class, acmePaid.getId()));
        assertEquals(List.of(0, 1), jdbc.queryForList("select position from invoice_line where invoice_id = ? order by position", Integer.class, acmePaid.getId()));
        assertEquals(1000L, jdbc.queryForObject("select unit_price_cents from invoice_line where invoice_id = ? and position = 0", Long.class, acmePaid.getId()));
    }

    @Test void itemsKeepOrderAndComputeTotal() {
        Invoice i = repo.findById(acmePaid.getId()).orElseThrow();
        assertEquals(List.of("ink", "pad"), i.getItems().stream().map(LineItem::description).toList());
        assertEquals(3500, i.totalCents());
    }

    @Test void derivedPagedQuery() {
        Page<Invoice> p = repo.findByStatus(InvoiceStatus.PAID, PageRequest.of(0, 1, Sort.by("id")));
        assertEquals(2, p.getTotalElements());
        assertEquals(2, p.getTotalPages());
        assertEquals(acmePaid.getId(), p.getContent().get(0).getId());
        assertEquals(1, repo.findByStatus(InvoiceStatus.PAID, PageRequest.of(1, 1, Sort.by("id"))).getContent().size());
        assertTrue(repo.findByStatus(InvoiceStatus.ISSUED, PageRequest.of(0, 5)).isEmpty());
    }

    @Test void constructorExpressionTotals() {
        assertEquals(List.of(new CustomerTotal("Acme", 3500), new CustomerTotal("Zeta", 20_000)), repo.totalsByCustomer(InvoiceStatus.PAID));
        assertEquals(List.of(new CustomerTotal("Acme", 300)), repo.totalsByCustomer(InvoiceStatus.DRAFT));
        assertTrue(repo.totalsByCustomer(InvoiceStatus.ISSUED).isEmpty());
    }

    @Test void bulkUpdateVoidsOnlyOldDraftsAndRefreshesTheContext() {
        Invoice loaded = repo.findById(acmeDraft.getId()).orElseThrow(); // managed copy that must not stay stale
        assertEquals(InvoiceStatus.DRAFT, loaded.getStatus());
        assertEquals(0, repo.voidStaleDrafts(T0), "strictly before");
        assertEquals(1, repo.voidStaleDrafts(T0.plusSeconds(1)));
        assertEquals(InvoiceStatus.VOID, repo.findById(acmeDraft.getId()).orElseThrow().getStatus());
        assertEquals(InvoiceStatus.PAID, repo.findById(acmePaid.getId()).orElseThrow().getStatus());
        assertEquals(0, repo.voidStaleDrafts(T0.plusSeconds(1000)));
    }

    @Test void top3NewestPerCustomer() {
        invoice("Acme", InvoiceStatus.DRAFT, T0.plusSeconds(300), new LineItem("a", 1, 1));
        invoice("Acme", InvoiceStatus.DRAFT, T0.plusSeconds(400), new LineItem("b", 1, 1));
        List<Invoice> top = repo.findTop3ByCustomerOrderByCreatedAtDesc("Acme");
        assertEquals(3, top.size());
        assertEquals(List.of(T0.plusSeconds(400), T0.plusSeconds(300), T0.plusSeconds(60)), top.stream().map(Invoice::getCreatedAt).toList());
    }
}
