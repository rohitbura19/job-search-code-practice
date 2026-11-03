package practice.e;

import java.time.Clock;
import org.springframework.data.domain.Page;

// TODO: @Service with @Transactional.
//  create(req): InPast if startAt is not after now (Clock); TooLong if the duration exceeds props.maxDuration();
//               Overlap if repository.existsOverlap(...); otherwise save and return the BookingView.
//  upcoming(room, since, page, size): clamp size to props.maxPageSize() (and at least 1), page at least 0; map to BookingView. Read-only transaction.
//  cancel(id): NotFound if missing, otherwise delete.
//  purge(): delete bookings that ended before now; return the count.
public class BookingService {
    public BookingService(BookingRepository repository, BookingProps props, Clock clock) { throw new UnsupportedOperationException("TODO"); }

    public BookingView create(BookingRequest req) { throw new UnsupportedOperationException("TODO"); }

    public Page<BookingView> upcoming(String room, java.time.Instant since, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    public void cancel(Long id) { throw new UnsupportedOperationException("TODO"); }

    public int purge() { throw new UnsupportedOperationException("TODO"); }
}
