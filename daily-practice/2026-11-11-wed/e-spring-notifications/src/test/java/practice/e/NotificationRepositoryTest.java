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

@DataJpaTest
class NotificationRepositoryTest {
    static final Instant T0 = Instant.parse("2026-11-01T00:00:00Z");
    @Autowired NotificationRepository repo;
    @Autowired EntityManager em;

    Notification a1, a2, a3, b1, aSms;

    @BeforeEach void data() {
        a1 = repo.save(new EmailNotification(1L, "ann@x.io", "first", T0));
        a2 = repo.save(new EmailNotification(1L, "ann@x.io", "second", T0.plusSeconds(60)));
        a3 = repo.save(new EmailNotification(1L, "ann@x.io", "same time as second", T0.plusSeconds(60)));
        aSms = repo.save(new SmsNotification(1L, "+14155550100", "sms", T0.plusSeconds(120)));
        b1 = repo.save(new EmailNotification(2L, "bob@x.io", "bob's", T0));
        a1.markRead(T0.plusSeconds(10));
        em.flush();
        em.clear();
    }

    @Test void singleTableWithDiscriminatorColumn() {
        @SuppressWarnings("unchecked")
        List<Object> channels = em.createNativeQuery("select distinct channel from notifications order by channel").getResultList();
        assertEquals(List.of("EMAIL", "SMS"), channels);
        Notification loaded = repo.findById(aSms.getId()).orElseThrow();
        assertInstanceOf(SmsNotification.class, loaded);
        assertEquals("+14155550100", loaded.target());
        assertEquals(Channel.SMS, loaded.channel());
        assertInstanceOf(EmailNotification.class, repo.findById(a1.getId()).orElseThrow());
    }

    @Test void inboxIsNewestFirstWithIdTieBreakAndPaged() {
        Page<Notification> p0 = repo.findByUserIdOrderByCreatedAtDescIdDesc(1L, PageRequest.of(0, 3));
        assertEquals(List.of(aSms.getId(), a3.getId(), a2.getId()), p0.map(Notification::getId).getContent());
        assertEquals(4, p0.getTotalElements());
        Page<Notification> p1 = repo.findByUserIdOrderByCreatedAtDescIdDesc(1L, PageRequest.of(1, 3));
        assertEquals(List.of(a1.getId()), p1.map(Notification::getId).getContent());
        assertEquals(0, repo.findByUserIdOrderByCreatedAtDescIdDesc(99L, PageRequest.of(0, 3)).getTotalElements());
    }

    @Test void unreadFilterAndCount() {
        Page<Notification> unread = repo.findByUserIdAndReadAtIsNullOrderByCreatedAtDescIdDesc(1L, PageRequest.of(0, 10));
        assertEquals(List.of(aSms.getId(), a3.getId(), a2.getId()), unread.map(Notification::getId).getContent());
        assertEquals(3, repo.countByUserIdAndReadAtIsNull(1L));
        assertEquals(1, repo.countByUserIdAndReadAtIsNull(2L));
        assertEquals(0, repo.countByUserIdAndReadAtIsNull(3L));
    }

    @Test void lookupIsScopedToTheOwner() {
        assertTrue(repo.findByIdAndUserId(a1.getId(), 1L).isPresent());
        assertTrue(repo.findByIdAndUserId(a1.getId(), 2L).isEmpty());
    }

    @Test void markAllReadTouchesOnlyUnreadRowsOfThatUser() {
        Instant now = T0.plusSeconds(1000);
        assertEquals(3, repo.markAllRead(1L, now));
        assertEquals(0, repo.markAllRead(1L, now.plusSeconds(1)));
        assertEquals(0, repo.countByUserIdAndReadAtIsNull(1L));
        assertEquals(1, repo.countByUserIdAndReadAtIsNull(2L));
        assertEquals(T0.plusSeconds(10), repo.findById(a1.getId()).orElseThrow().getReadAt(), "already-read rows keep their first read time");
        assertEquals(now, repo.findById(a2.getId()).orElseThrow().getReadAt(), "context was cleared, so this is fresh data");
    }

    @Test void staleUnreadEmailsUsesTypeAndStrictCutoff() {
        List<Notification> stale = repo.staleUnreadEmails(T0.plusSeconds(60));
        assertEquals(List.of(b1.getId()), stale.stream().map(Notification::getId).toList());     // a1 is read; a2/a3 are exactly at the cutoff; SMS excluded
        List<Notification> more = repo.staleUnreadEmails(T0.plusSeconds(1000));
        assertEquals(List.of(b1.getId(), a2.getId(), a3.getId()), more.stream().map(Notification::getId).toList());
        assertTrue(more.stream().allMatch(n -> n instanceof EmailNotification));
    }
}
