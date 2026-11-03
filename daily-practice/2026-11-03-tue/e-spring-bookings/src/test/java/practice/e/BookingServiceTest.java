package practice.e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    static final Instant NOW = Instant.parse("2026-11-03T08:00:00Z");
    @Mock BookingRepository repo;
    BookingService service;

    @BeforeEach void setUp() {
        service = new BookingService(repo, new BookingProps(Duration.ofHours(4), 50), Clock.fixed(NOW, ZoneOffset.UTC));
    }

    BookingRequest req(Instant s, Instant e) { return new BookingRequest("A", s, e, "a@x.com"); }

    @Test void createsWhenFree() {
        Instant s = NOW.plusSeconds(3600), e = NOW.plusSeconds(7200);
        when(repo.existsOverlap("A", s, e)).thenReturn(false);
        when(repo.save(any(Booking.class))).thenAnswer(i -> { Booking b = i.getArgument(0); ReflectionTestUtils.setField(b, "id", 7L); return b; });
        BookingView v = service.create(req(s, e));
        assertThat(v).isEqualTo(new BookingView(7L, "A", s, e, "a@x.com"));
    }

    @Test void rejectsPastAndNowStart() {
        assertThatThrownBy(() -> service.create(req(NOW, NOW.plusSeconds(60)))).isInstanceOf(BookingExceptions.InPast.class);
        assertThatThrownBy(() -> service.create(req(NOW.minusSeconds(5), NOW.plusSeconds(60)))).isInstanceOf(BookingExceptions.InPast.class);
        verify(repo, never()).save(any());
    }

    @Test void rejectsTooLongButAllowsExactlyMax() {
        Instant s = NOW.plusSeconds(60);
        assertThatThrownBy(() -> service.create(req(s, s.plus(Duration.ofHours(4)).plusSeconds(1)))).isInstanceOf(BookingExceptions.TooLong.class);
        when(repo.existsOverlap(eq("A"), any(), any())).thenReturn(false);
        when(repo.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.create(req(s, s.plus(Duration.ofHours(4)))).room()).isEqualTo("A");
    }

    @Test void overlapIsRejectedAndNothingSaved() {
        Instant s = NOW.plusSeconds(3600), e = NOW.plusSeconds(7200);
        when(repo.existsOverlap("A", s, e)).thenReturn(true);
        assertThatThrownBy(() -> service.create(req(s, e))).isInstanceOf(BookingExceptions.Overlap.class);
        verify(repo, never()).save(any());
    }

    @Test void upcomingClampsPageAndSize() {
        when(repo.upcoming(eq("A"), eq(NOW), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        service.upcoming("A", NOW, -3, 1000);
        service.upcoming("A", NOW, 2, 0);
        ArgumentCaptor<Pageable> c = ArgumentCaptor.forClass(Pageable.class);
        verify(repo, org.mockito.Mockito.times(2)).upcoming(eq("A"), eq(NOW), c.capture());
        assertThat(c.getAllValues().get(0)).isEqualTo(PageRequest.of(0, 50));
        assertThat(c.getAllValues().get(1)).isEqualTo(PageRequest.of(2, 1));
    }

    @Test void cancel() {
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.existsById(2L)).thenReturn(false);
        service.cancel(1L);
        verify(repo).deleteById(1L);
        assertThatThrownBy(() -> service.cancel(2L)).isInstanceOf(BookingExceptions.NotFound.class);
        verify(repo, never()).deleteById(2L);
    }

    @Test void purgeUsesTheClock() {
        when(repo.purgeEndedBefore(NOW)).thenReturn(4);
        assertThat(service.purge()).isEqualTo(4);
    }
}
