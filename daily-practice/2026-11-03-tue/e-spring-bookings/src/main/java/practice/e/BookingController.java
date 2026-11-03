package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;

// TODO: REST controller under /api/bookings.
//  POST   /api/bookings         validated JSON body -> 201 + Location /api/bookings/{id} + BookingView
//  GET    /api/bookings         query params room (required), since (required ISO instant), page (default 0), size (default 20)
//                               -> 200 with the list as the body and the total in the response header X-Total-Count
//  DELETE /api/bookings/{id}    -> 204
public class BookingController {
    public BookingController(BookingService service) { throw new UnsupportedOperationException("TODO"); }

    public ResponseEntity<BookingView> create(BookingRequest req) { throw new UnsupportedOperationException("TODO"); }

    public ResponseEntity<List<BookingView>> list(String room, Instant since, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    public ResponseEntity<Void> cancel(Long id) { throw new UnsupportedOperationException("TODO"); }
}
