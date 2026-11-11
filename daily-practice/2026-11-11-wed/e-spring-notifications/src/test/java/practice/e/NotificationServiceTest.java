package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;
import practice.e.Exceptions.InvalidTargetException;
import practice.e.Exceptions.NotFoundException;

@DataJpaTest
@Import({NotificationService.class, NotificationServiceTest.Cfg.class})
@TestPropertySource(properties = "inbox.stale-days=7")
class NotificationServiceTest {
    static final Instant NOW = Instant.parse("2026-11-11T08:00:00Z");

    @TestConfiguration
    static class Cfg { @Bean @Primary Clock fixedClock() { return Clock.fixed(NOW, ZoneOffset.UTC); } }

    @Autowired NotificationService service;
    @Autowired NotificationRepository repo;

    @Test void sendEmailNormalisesAndStores() {
        Views.NotificationView v = service.send(new SendRequest(1L, Channel.EMAIL, "  Ann@X.io ", "hello"));
        assertNotNull(v.id());
        assertEquals("ann@x.io", v.target());
        assertEquals(Channel.EMAIL, v.channel());
        assertEquals(NOW, v.createdAt());
        assertFalse(v.read());
        assertInstanceOf(EmailNotification.class, repo.findById(v.id()).orElseThrow());
    }

    @Test void sendSmsAndValidateTargets() {
        assertEquals("+14155550100", service.send(new SendRequest(1L, Channel.SMS, "+14155550100", "hi")).target());
        for (String bad : new String[]{"4155550100", "+0123456789", "+1415", "+14155550100123456", "+1 415 555 0100", "ann@x.io"})
            assertThrows(InvalidTargetException.class, () -> service.send(new SendRequest(1L, Channel.SMS, bad, "hi")), bad);
        for (String bad : new String[]{"ann", "ann@x", "@x.io", "a b@x.io", "a@@x.io", "+14155550100"})
            assertThrows(InvalidTargetException.class, () -> service.send(new SendRequest(1L, Channel.EMAIL, bad, "hi")), bad);
        assertEquals(1, repo.count());
    }

    @Test void listPagesAndFilters() {
        for (int i = 0; i < 5; i++) service.send(new SendRequest(1L, Channel.EMAIL, "ann@x.io", "m" + i));
        service.send(new SendRequest(2L, Channel.EMAIL, "bob@x.io", "other"));
        Views.PageView p = service.list(1L, false, 0, 2);
        assertEquals(2, p.items().size());
        assertEquals(5, p.total());
        assertEquals(0, p.page());
        assertEquals(2, p.size());
        assertTrue(p.items().get(0).id() > p.items().get(1).id());
        service.markRead(p.items().get(0).id(), 1L);
        assertEquals(4, service.list(1L, true, 0, 10).total());
        assertEquals(4, service.unreadCount(1L));
        assertEquals(1, service.unreadCount(2L));
        assertTrue(service.list(3L, false, 0, 10).items().isEmpty());
    }

    @Test void markReadIsScopedAndKeepsFirstReadTime() {
        Views.NotificationView v = service.send(new SendRequest(1L, Channel.SMS, "+14155550100", "hi"));
        assertThrows(NotFoundException.class, () -> service.markRead(v.id(), 2L));
        assertThrows(NotFoundException.class, () -> service.markRead(9999L, 1L));
        assertTrue(service.markRead(v.id(), 1L).read());
        assertEquals(NOW, repo.findById(v.id()).orElseThrow().getReadAt());
        assertTrue(service.markRead(v.id(), 1L).read());
        NotFoundException e = assertThrows(NotFoundException.class, () -> service.markRead(9999L, 1L));
        assertEquals("Notification 9999 not found", e.getMessage());
    }

    @Test void markAllReadReturnsChangedRows() {
        for (int i = 0; i < 3; i++) service.send(new SendRequest(1L, Channel.EMAIL, "ann@x.io", "m" + i));
        service.send(new SendRequest(2L, Channel.EMAIL, "bob@x.io", "other"));
        assertEquals(3, service.markAllRead(1L));
        assertEquals(0, service.markAllRead(1L));
        assertEquals(0, service.unreadCount(1L));
        assertEquals(1, service.unreadCount(2L));
    }

    @Test void staleCutoffComesFromProperty() {
        Notification old = repo.save(new EmailNotification(1L, "a@x.io", "old", NOW.minus(Duration.ofDays(8))));
        repo.save(new EmailNotification(1L, "a@x.io", "recent", NOW.minus(Duration.ofDays(6))));
        repo.save(new SmsNotification(1L, "+14155550100", "old sms", NOW.minus(Duration.ofDays(30))));
        Notification readOld = repo.save(new EmailNotification(1L, "a@x.io", "old but read", NOW.minus(Duration.ofDays(9))));
        readOld.markRead(NOW);
        assertEquals(List.of(old.getId()), service.staleUnreadEmailIds());
    }
}
