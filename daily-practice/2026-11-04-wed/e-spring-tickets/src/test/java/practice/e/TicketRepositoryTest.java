package practice.e;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DataJpaTest
class TicketRepositoryTest {
    static final Instant T0 = Instant.parse("2026-11-01T00:00:00Z");
    @Autowired TicketRepository repo;

    Ticket save(String title, Status status, Priority prio, String assignee, int hoursAfterT0) {
        Ticket t = new Ticket(title, prio, assignee, T0.plusSeconds(hoursAfterT0 * 3600L));
        t.setStatus(status);
        return repo.saveAndFlush(t);
    }

    void seed() {
        save("t1", Status.OPEN, Priority.LOW, "ann", 1);
        save("t2", Status.IN_PROGRESS, Priority.HIGH, "ann", 2);
        save("t3", Status.OPEN, Priority.NORMAL, "bob", 3);
        save("t4", Status.CLOSED, Priority.LOW, null, 4);
        save("t5", Status.OPEN, Priority.URGENT, "ann", 5);
    }

    List<String> titles(Slice<Ticket> s) { return s.getContent().stream().map(Ticket::getTitle).toList(); }

    @Test void searchWithoutFiltersIsNewestFirst() {
        seed();
        assertThat(titles(repo.search(null, null, PageRequest.of(0, 10)))).containsExactly("t5", "t4", "t3", "t2", "t1");
    }

    @Test void searchFilters() {
        seed();
        assertThat(titles(repo.search(Status.OPEN, null, PageRequest.of(0, 10)))).containsExactly("t5", "t3", "t1");
        assertThat(titles(repo.search(null, "ann", PageRequest.of(0, 10)))).containsExactly("t5", "t2", "t1");
        assertThat(titles(repo.search(Status.OPEN, "ann", PageRequest.of(0, 10)))).containsExactly("t5", "t1");
        assertThat(titles(repo.search(Status.RESOLVED, null, PageRequest.of(0, 10)))).isEmpty();
        assertThat(titles(repo.search(null, "nobody", PageRequest.of(0, 10)))).isEmpty();
    }

    @Test void sliceKnowsWhetherThereIsANextPage() {
        seed();
        Slice<Ticket> first = repo.search(null, null, PageRequest.of(0, 2));
        assertThat(titles(first)).containsExactly("t5", "t4");
        assertThat(first.hasNext()).isTrue();
        Slice<Ticket> last = repo.search(null, null, PageRequest.of(2, 2));
        assertThat(titles(last)).containsExactly("t1");
        assertThat(last.hasNext()).isFalse();
    }

    @Test void equalTimestampsAreOrderedByIdDescending() {
        save("a", Status.OPEN, Priority.LOW, null, 1);
        save("b", Status.OPEN, Priority.LOW, null, 1);
        save("c", Status.OPEN, Priority.LOW, null, 1);
        assertThat(titles(repo.search(null, null, PageRequest.of(0, 10)))).containsExactly("c", "b", "a");
    }

    @Test void countByStatusProjection() {
        seed();
        Map<Status, Long> counts = repo.countByStatus().stream().collect(Collectors.toMap(StatusCount::getStatus, StatusCount::getTotal));
        assertThat(counts).containsEntry(Status.OPEN, 3L).containsEntry(Status.IN_PROGRESS, 1L).containsEntry(Status.CLOSED, 1L).doesNotContainKey(Status.RESOLVED);
    }

    @Test void escalateStaleChangesOnlyOldOpenNonUrgentTickets() {
        Ticket oldLow = save("old-low", Status.OPEN, Priority.LOW, null, 1);
        save("old-urgent", Status.OPEN, Priority.URGENT, null, 1);
        save("old-in-progress", Status.IN_PROGRESS, Priority.LOW, null, 1);
        save("old-closed", Status.CLOSED, Priority.LOW, null, 1);
        Ticket fresh = save("fresh", Status.OPEN, Priority.LOW, null, 100);
        Instant cutoff = T0.plusSeconds(50 * 3600L);

        assertThat(repo.escalateStale(cutoff)).isEqualTo(1);

        // clearAutomatically: findById must hit the database, not the stale first-level cache
        assertThat(repo.findById(oldLow.getId()).orElseThrow().getPriority()).isEqualTo(Priority.URGENT);
        assertThat(repo.findById(fresh.getId()).orElseThrow().getPriority()).isEqualTo(Priority.LOW);
        Map<String, Priority> byTitle = repo.findAll().stream().collect(Collectors.toMap(Ticket::getTitle, Ticket::getPriority));
        assertThat(byTitle).containsEntry("old-in-progress", Priority.LOW).containsEntry("old-closed", Priority.LOW).containsEntry("old-urgent", Priority.URGENT);
        assertThat(repo.escalateStale(cutoff)).isZero();
    }
}
