package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class SubscriptionRepositoryTest {
    static final Instant T0 = Instant.parse("2026-11-01T00:00:00Z");
    @Autowired SubscriptionRepository subs;
    @Autowired AccountRepository accounts;
    @Autowired EntityManager em;

    Account ann, bob, cy;
    Subscription annOld, annNew, bobPro, cyFree;

    private Subscription sub(Account a, Plan p, SubscriptionStatus st, Instant started, Instant renews) {
        Subscription s = new Subscription(a, p, started, renews);
        s.setStatus(st);
        return subs.save(s);
    }

    @BeforeEach void data() {
        ann = accounts.save(new Account("ann@x.io", T0));
        bob = accounts.save(new Account("bob@x.io", T0));
        cy = accounts.save(new Account("cy@x.io", T0));
        accounts.save(new Account("dee@x.io", T0));                                           // never subscribed
        annOld = sub(ann, Plan.BASIC, SubscriptionStatus.CANCELLED, T0, T0.plusSeconds(100));
        annNew = sub(ann, Plan.PRO, SubscriptionStatus.ACTIVE, T0.plusSeconds(50), T0.plusSeconds(3600));
        bobPro = sub(bob, Plan.PRO, SubscriptionStatus.ACTIVE, T0, T0.plusSeconds(7200));
        cyFree = sub(cy, Plan.FREE, SubscriptionStatus.ACTIVE, T0, T0.plusSeconds(86_400));
        em.flush();
        em.clear();
    }

    @Test void emailIsUniqueAtTheDatabase() {
        assertThrows(DataIntegrityViolationException.class, () -> { accounts.saveAndFlush(new Account("ann@x.io", T0)); });
    }

    @Test void existsByAccountAndStatus() {
        assertTrue(subs.existsByAccountIdAndStatus(ann.getId(), SubscriptionStatus.ACTIVE));
        assertTrue(subs.existsByAccountIdAndStatus(ann.getId(), SubscriptionStatus.CANCELLED));
        assertFalse(subs.existsByAccountIdAndStatus(bob.getId(), SubscriptionStatus.CANCELLED));
        assertFalse(subs.existsByAccountIdAndStatus(9999L, SubscriptionStatus.ACTIVE));
    }

    @Test void byEmailNewestFirstWithAccountLoaded() {
        List<Subscription> l = subs.findByAccountEmailOrderByStartedAtDesc("ann@x.io");
        assertEquals(List.of(annNew.getId(), annOld.getId()), l.stream().map(Subscription::getId).toList());
        em.getEntityManagerFactory().getPersistenceUnitUtil();
        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(l.get(0), "account"), "account must be fetched by the entity graph");
        assertEquals(List.of(), subs.findByAccountEmailOrderByStartedAtDesc("nobody@x.io"));
    }

    @Test void lazyAssociationIsNotLoadedByPlainFindById() {
        Subscription s = subs.findById(bobPro.getId()).orElseThrow();
        assertFalse(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(s, "account"));
    }

    @Test void planCountsAreGroupedAndOrderedByName() {
        sub(bob, Plan.PRO, SubscriptionStatus.ACTIVE, T0, T0.plusSeconds(1));
        sub(cy, Plan.BASIC, SubscriptionStatus.ACTIVE, T0, T0.plusSeconds(1));
        em.flush();
        em.clear();
        List<PlanCount> c = subs.countByPlan(SubscriptionStatus.ACTIVE);
        assertEquals(List.of(Plan.BASIC, Plan.FREE, Plan.PRO), c.stream().map(PlanCount::getPlan).toList());
        assertEquals(List.of(1L, 1L, 3L), c.stream().map(PlanCount::getTotal).toList());
        assertEquals(List.of(Plan.BASIC), subs.countByPlan(SubscriptionStatus.CANCELLED).stream().map(PlanCount::getPlan).toList());
    }

    @Test void dueWindowIsInclusiveAndOrdered() {
        List<Subscription> due = subs.findByStatusAndRenewsAtBetweenOrderByRenewsAtAsc(SubscriptionStatus.ACTIVE, T0.plusSeconds(3600), T0.plusSeconds(7200));
        assertEquals(List.of(annNew.getId(), bobPro.getId()), due.stream().map(Subscription::getId).toList());
        assertEquals(List.of(), subs.findByStatusAndRenewsAtBetweenOrderByRenewsAtAsc(SubscriptionStatus.CANCELLED, T0.plusSeconds(3600), T0.plusSeconds(7200)));
    }

    @Test void churnedAccountsIncludeNeverSubscribedAndCancelledOnly() {
        Subscription s = subs.findById(annNew.getId()).orElseThrow();
        s.setStatus(SubscriptionStatus.CANCELLED);
        em.flush();
        em.clear();
        assertEquals(List.of("ann@x.io", "dee@x.io"), accounts.findChurned().stream().map(Account::getEmail).toList());
    }

    @Test void enumsAreStoredAsNames() {
        Object raw = em.createNativeQuery("select plan from subscriptions where id = " + bobPro.getId()).getSingleResult();
        assertEquals("PRO", raw);
    }
}
