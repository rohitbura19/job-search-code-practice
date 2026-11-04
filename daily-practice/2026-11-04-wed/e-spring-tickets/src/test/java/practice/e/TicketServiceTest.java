package practice.e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

class TicketServiceTest {
    static final Instant NOW = Instant.parse("2026-11-04T10:00:00Z");
    final TicketRepository repo = mock(TicketRepository.class);
    final TicketService service = new TicketService(repo, Clock.fixed(NOW, ZoneOffset.UTC), Duration.ofDays(3), 50);

    @Test void createStripsTitleStampsTimeAndSaves() {
        when(repo.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));
        TicketView v = service.create(new CreateTicketRequest("  Printer jams  ", Priority.HIGH, "ann@x.com"));
        assertThat(v.title()).isEqualTo("Printer jams");
        assertThat(v.status()).isEqualTo(Status.OPEN);
        assertThat(v.priority()).isEqualTo(Priority.HIGH);
        assertThat(v.assignee()).isEqualTo("ann@x.com");
        assertThat(v.createdAt()).isEqualTo(NOW);
    }

    @Test void getAndMissing() {
        when(repo.findById(1L)).thenReturn(Optional.of(new Ticket("t", Priority.LOW, null, NOW)));
        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThat(service.get(1L).title()).isEqualTo("t");
        assertThatThrownBy(() -> service.get(2L)).isInstanceOf(TicketExceptions.NotFound.class);
    }

    @Test void transitionChangesTheManagedEntityWithoutSave() {
        Ticket t = new Ticket("t", Priority.LOW, null, NOW);
        when(repo.findById(1L)).thenReturn(Optional.of(t));
        TicketView v = service.transition(1L, Status.IN_PROGRESS);
        assertThat(v.status()).isEqualTo(Status.IN_PROGRESS);
        assertThat(t.getStatus()).isEqualTo(Status.IN_PROGRESS);
        verify(repo, never()).save(any());
    }

    @Test void illegalOrMissingTransition() {
        Ticket t = new Ticket("t", Priority.LOW, null, NOW);
        when(repo.findById(1L)).thenReturn(Optional.of(t));
        assertThatThrownBy(() -> service.transition(1L, Status.RESOLVED)).isInstanceOf(TicketExceptions.IllegalTransition.class)
                .hasMessage("cannot move a ticket from OPEN to RESOLVED");
        assertThat(t.getStatus()).isEqualTo(Status.OPEN);
        assertThatThrownBy(() -> service.transition(9L, Status.CLOSED)).isInstanceOf(TicketExceptions.NotFound.class);
    }

    @Test void searchClampsPagingAndMapsHasNext() {
        Ticket t = new Ticket("t", Priority.LOW, null, NOW);
        when(repo.search(any(), any(), any(Pageable.class))).thenReturn(new SliceImpl<>(List.of(t), PageRequest.of(0, 5), true));
        TicketPage page = service.search(Status.OPEN, "ann", -3, 1000);
        assertThat(page.hasNext()).isTrue();
        assertThat(page.content()).hasSize(1);
        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(repo).search(eq(Status.OPEN), eq("ann"), cap.capture());
        assertThat(cap.getValue().getPageNumber()).isZero();
        assertThat(cap.getValue().getPageSize()).isEqualTo(50);
        service.search(null, null, 2, 0);
        verify(repo).search(eq(null), eq(null), eq(PageRequest.of(2, 1)));
    }

    @Test void escalateUsesNowMinusStaleAfter() {
        when(repo.escalateStale(NOW.minus(Duration.ofDays(3)))).thenReturn(4);
        assertThat(service.escalateStale()).isEqualTo(4);
    }

    @Test void annotationsAreInPlace() throws Exception {
        assertThat(TicketService.class.isAnnotationPresent(Service.class)).isTrue();
        assertThat(TicketService.class.isAnnotationPresent(Transactional.class)).isTrue();
        Method search = TicketService.class.getMethod("search", Status.class, String.class, int.class, int.class);
        assertThat(search.getAnnotation(Transactional.class)).isNotNull();
        assertThat(search.getAnnotation(Transactional.class).readOnly()).isTrue();
        var ctor = TicketService.class.getConstructors()[0];
        String[] values = java.util.Arrays.stream(ctor.getParameters()).map(p -> p.getAnnotation(Value.class)).filter(java.util.Objects::nonNull).map(Value::value).toArray(String[]::new);
        assertThat(values).containsExactly("${tickets.stale-after:P3D}", "${tickets.max-page-size:50}");
    }
}
