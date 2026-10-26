package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class RefundRepositoryTest {
    static final Instant T0 = Instant.parse("2026-10-26T04:00:00Z");
    @Autowired RefundRepository repo;
    @Autowired TestEntityManager em;

    static Refund refund(String pay, long amt, String key, RefundStatus st, int minutes) {
        return new Refund(pay, amt, null, key, st, T0.plusSeconds(60L * minutes));
    }

    @BeforeEach void seed() {
        repo.save(refund("pay-1", 100, "key-00001", RefundStatus.APPROVED, 0));
        repo.save(refund("pay-1", 900_000, "key-00002", RefundStatus.PENDING, 10));
        repo.save(refund("pay-1", 500, "key-00003", RefundStatus.PENDING, 20));
        repo.save(refund("pay-2", 700, "key-00004", RefundStatus.PENDING, 5));
        repo.save(refund("pay-1", 300, "key-00005", RefundStatus.REJECTED, 30));
        repo.flush();
    }

    @Test void entityGetsIdVersionAndKeepsTheStatusName() {
        Refund r = repo.saveAndFlush(refund("pay-9", 10, "key-00009", RefundStatus.PENDING, 0));
        assertNotNull(r.getId());
        assertEquals(0, r.getVersion());
        Object stored = em.getEntityManager().createNativeQuery("select status from refund where id = ?1").setParameter(1, r.getId()).getSingleResult();
        assertEquals("PENDING", stored, "stored as the name, not the ordinal");
    }

    @Test void idempotencyKeyIsUnique() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(refund("pay-3", 1, "key-00001", RefundStatus.PENDING, 0)));
    }

    @Test void findByKey() {
        assertEquals(900_000, repo.findByIdempotencyKey("key-00002").orElseThrow().getAmountPaise());
        assertTrue(repo.findByIdempotencyKey("nope").isEmpty());
    }

    @Test void pagingAndSortingByPayment() {
        Page<Refund> p0 = repo.findByPaymentId("pay-1", PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
        assertEquals(4, p0.getTotalElements());
        assertEquals(2, p0.getTotalPages());
        assertEquals(List.of("key-00005", "key-00003", "key-00002"), p0.getContent().stream().map(Refund::getIdempotencyKey).toList());
        Page<Refund> p1 = repo.findByPaymentId("pay-1", PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
        assertEquals(List.of("key-00001"), p1.getContent().stream().map(Refund::getIdempotencyKey).toList());
        assertTrue(repo.findByPaymentId("none", PageRequest.of(0, 3)).isEmpty());
    }

    @Test void stalePendingIsOldestFirstAndExcludesDecidedAndFresh() {
        List<Refund> stale = repo.findStalePending(T0.plusSeconds(60 * 15));
        assertEquals(List.of("key-00004", "key-00002"), stale.stream().map(Refund::getIdempotencyKey).toList());
        assertTrue(repo.findStalePending(T0).isEmpty(), "strictly before the cutoff");
    }

    @Test void secondWriterOfTheSameVersionLoses() {
        Long id = repo.findByIdempotencyKey("key-00002").orElseThrow().getId();
        em.clear();
        Refund first = repo.findById(id).orElseThrow();
        em.clear();
        Refund second = repo.findById(id).orElseThrow();
        em.clear();                                                // both copies are detached, each holding version 0
        first.decide(true);
        Refund saved = repo.saveAndFlush(first);                 // merge: the managed copy carries the new version
        assertEquals(1, saved.getVersion(), "the version moved forward");
        second.decide(false);
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> repo.saveAndFlush(second));
    }

    @Test void decideOnlyFromPending() {
        Refund done = repo.findByIdempotencyKey("key-00001").orElseThrow();
        assertThrows(IllegalStateException.class, () -> done.decide(false));
    }
}
