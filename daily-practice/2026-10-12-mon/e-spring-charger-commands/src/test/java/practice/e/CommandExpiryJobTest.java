package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/** E - the scheduled expiry job; no scheduler is started, the method is called directly. */
class CommandExpiryJobTest {

    static final Instant NOW = Instant.parse("2026-10-12T10:00:00Z");
    final CommandStore store = mock(CommandStore.class);
    final CommandExpiryJob job = new CommandExpiryJob(store, Clock.fixed(NOW, ZoneOffset.UTC));

    @Test @DisplayName("run() expires PENDING commands older than 'now' (now comes from the clock)")
    void expires() {
        job.run();
        verify(store).expire(CommandStatus.PENDING, CommandStatus.EXPIRED, NOW);
    }

    @Test @DisplayName("run() is @Scheduled to fire every 60 seconds")
    void scheduled() throws Exception {
        Scheduled s = CommandExpiryJob.class.getMethod("run").getAnnotation(Scheduled.class);
        assertNotNull(s);
        assertTrue(s.fixedDelay() == 60_000L || s.fixedRate() == 60_000L || !s.cron().isEmpty()
                || !s.fixedDelayString().isEmpty() || !s.fixedRateString().isEmpty(), "no 60 second schedule found");
    }
}
