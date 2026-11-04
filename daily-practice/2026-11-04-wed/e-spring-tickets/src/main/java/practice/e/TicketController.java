package practice.e;

import org.springframework.http.ResponseEntity;

// TODO: REST controller under /api/tickets.
//  POST   /api/tickets               @Valid @RequestBody CreateTicketRequest -> 201, Location: /api/tickets/{id}, body TicketView
//  GET    /api/tickets/{id}          -> 200 TicketView
//  PATCH  /api/tickets/{id}/status   @Valid @RequestBody StatusChange -> 200 TicketView
//  GET    /api/tickets               optional request params status (enum) and assignee, page (default 0), size (default 20) -> 200 TicketPage
//                                    (an unknown status value such as ?status=FOO is a 400 by default)
public class TicketController {
    public TicketController(TicketService service) { throw new UnsupportedOperationException("TODO"); }

    public ResponseEntity<TicketView> create(CreateTicketRequest req) { throw new UnsupportedOperationException("TODO"); }

    public TicketView get(Long id) { throw new UnsupportedOperationException("TODO"); }

    public TicketView changeStatus(Long id, StatusChange change) { throw new UnsupportedOperationException("TODO"); }

    public TicketPage search(Status status, String assignee, int page, int size) { throw new UnsupportedOperationException("TODO"); }
}
