package practice.e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.util.ReflectionTestUtils;

class SlotServiceTest {
    static final Instant START = Instant.parse("2999-01-01T10:00:00Z");
    SlotRepository repo;
    SlotService service;

    @BeforeEach void setUp() {
        repo = mock(SlotRepository.class);
        service = new SlotService(repo);
    }

    static Slot slot(long id, int cap, int booked) {
        Slot s = new Slot("st1", START, cap);
        ReflectionTestUtils.setField(s, "id", id);
        ReflectionTestUtils.setField(s, "booked", booked);
        return s;
    }

    @Test void createSavesAndReturnsFullRemaining() {
        when(repo.save(any())).thenAnswer(inv -> { Slot s = inv.getArgument(0); ReflectionTestUtils.setField(s, "id", 9L); return s; });
        SlotView v = service.create(new CreateSlotRequest("st1", START, 4));
        assertEquals(new SlotView(9L, "st1", START, 4, 4), v);
    }

    @Test void reserveUsesTheAtomicUpdateAndNeverSaves() {
        when(repo.reserve(5)).thenReturn(1);
        when(repo.findById(5L)).thenReturn(Optional.of(slot(5, 3, 2)));
        assertEquals(1, service.reserve(5).remaining());
        verify(repo, never()).save(any());
    }

    @Test void zeroRowsAndMissingIdIsNotFound() {
        when(repo.reserve(5)).thenReturn(0);
        when(repo.existsById(5L)).thenReturn(false);
        assertThrows(SlotNotFoundException.class, () -> service.reserve(5));
    }

    @Test void zeroRowsAndExistingIdIsFull() {
        when(repo.reserve(5)).thenReturn(0);
        when(repo.existsById(5L)).thenReturn(true);
        assertThrows(SlotFullException.class, () -> service.reserve(5));
    }

    @Test void availableMapsToViews() {
        when(repo.available("st1", START)).thenReturn(List.of(slot(1, 3, 1)));
        assertEquals(List.of(new SlotView(1L, "st1", START, 3, 2)), service.available("st1", START));
    }

    @Test void annotationsAreInPlace() throws Exception {
        assertTrue(SlotService.class.isAnnotationPresent(Service.class));
        Method reserve = SlotService.class.getMethod("reserve", long.class);
        Method available = SlotService.class.getMethod("available", String.class, Instant.class);
        assertNotNull(reserve.getAnnotation(Transactional.class));
        assertTrue(available.getAnnotation(Transactional.class).readOnly());
    }
}
