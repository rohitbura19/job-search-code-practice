package practice.e;

import java.time.Clock;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Class-level @Transactional (writes); the readers override it with readOnly = true. transition() relies on dirty checking: the managed entity is flushed at commit and @Version rejects a stale write. */
@Service
@Transactional
public class TicketService {
    private final TicketRepository repository;
    private final Clock clock;
    private final Duration staleAfter;
    private final int maxPageSize;

    public TicketService(TicketRepository repository, Clock clock,
                         @Value("${tickets.stale-after:P3D}") Duration staleAfter,
                         @Value("${tickets.max-page-size:50}") int maxPageSize) {
        this.repository = repository; this.clock = clock; this.staleAfter = staleAfter; this.maxPageSize = maxPageSize;
    }

    public TicketView create(CreateTicketRequest req) {
        Ticket saved = repository.save(new Ticket(req.title().strip(), req.priority(), req.assignee(), clock.instant()));
        return TicketView.from(saved);
    }

    @Transactional(readOnly = true)
    public TicketView get(Long id) {
        return repository.findById(id).map(TicketView::from).orElseThrow(() -> new TicketExceptions.NotFound(id));
    }

    public TicketView transition(Long id, Status to) {
        Ticket t = repository.findById(id).orElseThrow(() -> new TicketExceptions.NotFound(id));
        if (!t.getStatus().canMoveTo(to)) throw new TicketExceptions.IllegalTransition(t.getStatus(), to);
        t.setStatus(to);
        return TicketView.from(t);
    }

    @Transactional(readOnly = true)
    public TicketPage search(Status status, String assignee, int page, int size) {
        Slice<Ticket> slice = repository.search(status, assignee, PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), maxPageSize)));
        return new TicketPage(slice.getContent().stream().map(TicketView::from).toList(), slice.hasNext());
    }

    public int escalateStale() {
        return repository.escalateStale(clock.instant().minus(staleAfter));
    }
}
