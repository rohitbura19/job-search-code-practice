package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** E - plain unit test of the idempotent create(); no Spring context, no database. */
class CommandServiceTest {

    static final Instant NOW = Instant.parse("2026-10-12T10:00:00Z");

    final CommandStore store = mock(CommandStore.class);
    final ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);
    final CommandProps props = new CommandProps("charger-commands", Duration.ofMinutes(5));
    final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
    final CommandService service = new CommandService(store, events, props, clock);

    CommandService serviceWithSaveEcho() {
        when(store.save(any(DeviceCommand.class))).thenAnswer(inv -> inv.getArgument(0));
        return service;
    }

    @Test @DisplayName("a new key saves a PENDING command with createdAt = clock and expiresAt = createdAt + ttlSeconds")
    void createNew() {
        when(store.findByIdempotencyKey("key-1")).thenReturn(Optional.empty());
        DeviceCommand c = serviceWithSaveEcho().create("CH1", "key-1", new CommandRequest(CommandType.RESTART, 90));

        ArgumentCaptor<DeviceCommand> saved = ArgumentCaptor.forClass(DeviceCommand.class);
        verify(store).save(saved.capture());
        DeviceCommand s = saved.getValue();
        assertEquals("key-1", s.getIdempotencyKey());
        assertEquals("CH1", s.getChargerId());
        assertEquals(CommandType.RESTART, s.getType());
        assertEquals(CommandStatus.PENDING, s.getStatus());
        assertEquals(NOW, s.getCreatedAt());
        assertEquals(NOW.plusSeconds(90), s.getExpiresAt());
        assertEquals(s.getId(), c.getId());
    }

    @Test @DisplayName("ttlSeconds null -> the configured default TTL (5 minutes)")
    void defaultTtl() {
        when(store.findByIdempotencyKey("key-2")).thenReturn(Optional.empty());
        DeviceCommand c = serviceWithSaveEcho().create("CH1", "key-2", new CommandRequest(CommandType.UNLOCK, null));
        assertEquals(NOW.plus(Duration.ofMinutes(5)), c.getExpiresAt());
    }

    @Test @DisplayName("a new command publishes exactly one CommandCreated event with its id, charger and type")
    void publishesEvent() {
        when(store.findByIdempotencyKey("key-3")).thenReturn(Optional.empty());
        DeviceCommand c = serviceWithSaveEcho().create("CH9", "key-3", new CommandRequest(CommandType.SET_CURRENT_LIMIT, 30));

        ArgumentCaptor<Object> event = ArgumentCaptor.forClass(Object.class);
        verify(events).publishEvent(event.capture());
        assertTrue(event.getValue() instanceof CommandCreated, "event was " + event.getValue());
        CommandCreated e = (CommandCreated) event.getValue();
        assertEquals(c.getId(), e.id());
        assertEquals("CH9", e.chargerId());
        assertEquals(CommandType.SET_CURRENT_LIMIT, e.type());
    }

    @Test @DisplayName("a repeated Idempotency-Key returns the existing command: nothing saved, nothing published")
    void duplicateKey() {
        DeviceCommand existing = new DeviceCommand();
        existing.setIdempotencyKey("key-4");
        when(store.findByIdempotencyKey("key-4")).thenReturn(Optional.of(existing));

        DeviceCommand c = service.create("CH1", "key-4", new CommandRequest(CommandType.RESTART, 10));

        assertSame(existing, c);
        verify(store, never()).save(any());
        verify(events, never()).publishEvent(any(Object.class));
    }
}
