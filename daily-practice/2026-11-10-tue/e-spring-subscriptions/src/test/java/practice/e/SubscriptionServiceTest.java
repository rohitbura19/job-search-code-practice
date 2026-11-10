package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.*;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

@DataJpaTest
@Import({SubscriptionService.class, SubscriptionServiceTest.Cfg.class})
class SubscriptionServiceTest {
    static final Instant NOW = Instant.parse("2026-11-10T08:00:00Z");

    @TestConfiguration
    static class Cfg {
        @Bean @Primary Clock fixedClock() { return Clock.fixed(NOW, ZoneOffset.UTC); }
        @Bean BillingProperties billing() { return new BillingProperties(10); }
    }

    @Autowired SubscriptionService service;
    @Autowired SubscriptionRepository subs;
    @Autowired AccountRepository accounts;
    @Autowired EntityManager em;

    @Test void subscribeCreatesAccountAndComputesRenewal() {
        Views.SubscriptionView v = service.subscribe("  Ann@X.io ", Plan.PRO);
        assertNotNull(v.id());
        assertEquals("ann@x.io", v.email());
        assertEquals(SubscriptionStatus.ACTIVE, v.status());
        assertEquals(NOW, v.startedAt());
        assertEquals(NOW.plus(Duration.ofDays(10)), v.renewsAt());
        assertEquals(1500, v.priceCents());
        assertEquals(1, accounts.count());
    }

    @Test void secondActiveSubscriptionForSameAccountConflicts() {
        service.subscribe("ann@x.io", Plan.BASIC);
        ConflictException e = assertThrows(ConflictException.class, () -> service.subscribe("ANN@x.io", Plan.PRO));
        assertEquals("Account ann@x.io already has an active subscription", e.getMessage());
        assertEquals(1, subs.count());
    }

    @Test void resubscribeAfterCancelReusesTheAccount() {
        Views.SubscriptionView first = service.subscribe("ann@x.io", Plan.BASIC);
        service.cancel(first.id());
        Views.SubscriptionView second = service.subscribe("ann@x.io", Plan.PRO);
        assertNotEquals(first.id(), second.id());
        assertEquals(1, accounts.count());
        assertEquals(2, subs.count());
    }

    @Test void getAndNotFound() {
        Views.SubscriptionView v = service.subscribe("ann@x.io", Plan.FREE);
        assertEquals(v, service.get(v.id()));
        assertEquals("Subscription 999 not found", assertThrows(NotFoundException.class, () -> service.get(999L)).getMessage());
    }

    @Test void forEmailNormalisesAndHandlesUnknown() {
        Views.SubscriptionView first = service.subscribe("ann@x.io", Plan.FREE);
        assertEquals(List.of(first.id()), service.forEmail(" ANN@x.io").stream().map(Views.SubscriptionView::id).toList());
        assertEquals(List.of(), service.forEmail("nobody@x.io"));
    }

    @Test void changePlanRules() {
        Views.SubscriptionView v = service.subscribe("ann@x.io", Plan.FREE);
        assertEquals(Plan.PRO, service.changePlan(v.id(), Plan.PRO).plan());
        assertEquals(Plan.PRO, service.changePlan(v.id(), Plan.PRO).plan());               // same plan: no-op
        em.flush();
        em.clear();
        assertEquals(Plan.PRO, service.get(v.id()).plan());                                  // really persisted
        service.cancel(v.id());
        assertEquals("Subscription " + v.id() + " is not active", assertThrows(ConflictException.class, () -> service.changePlan(v.id(), Plan.BASIC)).getMessage());
        assertThrows(NotFoundException.class, () -> service.changePlan(999L, Plan.BASIC));
    }

    @Test void cancelIsIdempotent() {
        Views.SubscriptionView v = service.subscribe("ann@x.io", Plan.FREE);
        service.cancel(v.id());
        service.cancel(v.id());
        assertEquals(SubscriptionStatus.CANCELLED, service.get(v.id()).status());
        assertThrows(NotFoundException.class, () -> service.cancel(999L));
    }

    @Test void renewalsDueUsesTheClockAndOnlyActive() {
        Account a = accounts.save(new Account("a@x.io", NOW)), b = accounts.save(new Account("b@x.io", NOW)), c = accounts.save(new Account("c@x.io", NOW));
        Account d = accounts.save(new Account("d@x.io", NOW));
        subs.save(new Subscription(a, Plan.BASIC, NOW.minusSeconds(1000), NOW.plus(Duration.ofDays(3))));
        subs.save(new Subscription(b, Plan.BASIC, NOW.minusSeconds(1000), NOW.plus(Duration.ofDays(1))));
        subs.save(new Subscription(c, Plan.BASIC, NOW.minusSeconds(1000), NOW.plus(Duration.ofDays(30))));
        Subscription cancelled = new Subscription(d, Plan.BASIC, NOW.minusSeconds(1000), NOW.plus(Duration.ofDays(2)));
        cancelled.setStatus(SubscriptionStatus.CANCELLED);
        subs.save(cancelled);
        subs.save(new Subscription(a, Plan.PRO, NOW.minusSeconds(2000), NOW.minusSeconds(10)));         // already overdue: not "due in the future window"
        assertEquals(List.of("b@x.io", "a@x.io"), service.renewalsDue(Duration.ofDays(3)).stream().map(Views.SubscriptionView::email).toList());
        assertEquals(List.of("b@x.io"), service.renewalsDue(Duration.ofDays(1)).stream().map(Views.SubscriptionView::email).toList());
    }

    @Test void activeByPlanListsEveryPlan() {
        assertEquals(Map.of(Plan.FREE, 0L, Plan.BASIC, 0L, Plan.PRO, 0L), service.activeByPlan());
        service.subscribe("a@x.io", Plan.PRO);
        service.subscribe("b@x.io", Plan.PRO);
        service.subscribe("c@x.io", Plan.FREE);
        Views.SubscriptionView gone = service.subscribe("d@x.io", Plan.BASIC);
        service.cancel(gone.id());
        Map<Plan, Long> m = service.activeByPlan();
        assertEquals(Map.of(Plan.FREE, 1L, Plan.BASIC, 0L, Plan.PRO, 2L), m);
        assertEquals(List.of(Plan.FREE, Plan.BASIC, Plan.PRO), List.copyOf(m.keySet()));        // EnumMap order
    }
}
