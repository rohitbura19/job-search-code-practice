package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** E - the idempotent save, as a plain unit test (no Spring context, no database). */
class ChargerEventServiceTest {

    static final EventDto DTO = new EventDto("evt-9", "CH-3", ChargerEvent.Status.FAULTED, 3.25, Instant.parse("2026-10-11T08:15:00Z"));

    @Test @DisplayName("save maps every DTO field onto the entity and flushes once")
    void mapsFields() {
        ChargerEventRepository repo = mock(ChargerEventRepository.class);
        new ChargerEventService(repo).save(DTO);
        ArgumentCaptor<ChargerEvent> captor = ArgumentCaptor.forClass(ChargerEvent.class);
        verify(repo, times(1)).saveAndFlush(captor.capture());
        ChargerEvent e = captor.getValue();
        assertEquals("evt-9", e.getEventId());
        assertEquals("CH-3", e.getChargerId());
        assertEquals(ChargerEvent.Status.FAULTED, e.getStatus());
        assertEquals(3.25, e.getKwh(), 0.0);
        assertEquals(Instant.parse("2026-10-11T08:15:00Z"), e.getEventTime());
        assertNull(e.getId(), "the database generates the id");
    }

    @Test @DisplayName("a duplicate delivery (unique constraint violation) is ignored")
    void duplicateIgnored() {
        ChargerEventRepository repo = mock(ChargerEventRepository.class);
        when(repo.saveAndFlush(any(ChargerEvent.class))).thenThrow(new DataIntegrityViolationException("duplicate key eventId"));
        new ChargerEventService(repo).save(DTO);          // must not throw
        verify(repo, times(1)).saveAndFlush(any(ChargerEvent.class));
    }

    @Test @DisplayName("any other failure propagates, so the message is not acknowledged")
    void otherFailuresPropagate() {
        ChargerEventRepository repo = mock(ChargerEventRepository.class);
        when(repo.saveAndFlush(any(ChargerEvent.class))).thenThrow(new IllegalStateException("db down"));
        assertThrows(IllegalStateException.class, () -> new ChargerEventService(repo).save(DTO));
    }

    @Test @DisplayName("save does not use check-then-act (existsByEventId): the unique constraint is the guard")
    void noExistsCheck() {
        ChargerEventRepository repo = mock(ChargerEventRepository.class);
        new ChargerEventService(repo).save(DTO);
        verify(repo, never()).existsByEventId(any());
    }
}
