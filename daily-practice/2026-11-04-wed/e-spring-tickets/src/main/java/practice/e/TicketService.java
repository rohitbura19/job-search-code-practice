package practice.e;

import java.time.Clock;
import java.time.Duration;

// TODO: @Service, @Transactional on the class (read-only where it only reads).
//  Constructor injection; the two @Value parameters read properties with defaults: "${tickets.stale-after:P3D}" (Duration) and "${tickets.max-page-size:50}" (int).
//  create(req):   new Ticket(req.title().strip(), priority, assignee, clock.instant()), save, return TicketView.from(saved).
//  get(id):       NotFound if absent.
//  transition(id, to): NotFound if absent; IllegalTransition(from, to) unless from.canMoveTo(to); otherwise set the status and return the view.
//                 No explicit save: the entity is managed, dirty checking flushes it, and @Version guards concurrent changes.
//  search(status, assignee, page, size): clamp size to 1..maxPageSize, page to >= 0; repository.search(..., PageRequest.of(page, size)); map to TicketPage(content, slice.hasNext()).
//  escalateStale(): cutoff = now - staleAfter; return repository.escalateStale(cutoff).
public class TicketService {
    public TicketService(TicketRepository repository, Clock clock, Duration staleAfter, int maxPageSize) { throw new UnsupportedOperationException("TODO"); }

    public TicketView create(CreateTicketRequest req) { throw new UnsupportedOperationException("TODO"); }

    public TicketView get(Long id) { throw new UnsupportedOperationException("TODO"); }

    public TicketView transition(Long id, Status to) { throw new UnsupportedOperationException("TODO"); }

    public TicketPage search(Status status, String assignee, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    public int escalateStale() { throw new UnsupportedOperationException("TODO"); }
}
