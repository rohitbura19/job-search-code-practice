package practice.e;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The existsOverlap-then-save sequence is NOT atomic: two concurrent requests can both pass the check. In production lock the room row
 * (PESSIMISTIC_WRITE on a Room entity), use SERIALIZABLE, or add a database exclusion constraint (PostgreSQL: EXCLUDE USING gist (room WITH =, tstzrange(start_at, end_at) WITH &&)).
 */
@Service
@Transactional
public class BookingService {
    private final BookingRepository repository;
    private final BookingProps props;
    private final Clock clock;

    public BookingService(BookingRepository repository, BookingProps props, Clock clock) {
        this.repository = repository; this.props = props; this.clock = clock;
    }

    public BookingView create(BookingRequest req) {
        if (!req.startAt().isAfter(clock.instant())) throw new BookingExceptions.InPast();
        if (Duration.between(req.startAt(), req.endAt()).compareTo(props.maxDuration()) > 0) throw new BookingExceptions.TooLong(props.maxDuration());
        if (repository.existsOverlap(req.room(), req.startAt(), req.endAt())) throw new BookingExceptions.Overlap(req.room());
        return BookingView.from(repository.save(new Booking(req.room(), req.startAt(), req.endAt(), req.bookedBy())));
    }

    @Transactional(readOnly = true)
    public Page<BookingView> upcoming(String room, Instant since, int page, int size) {
        int s = Math.max(1, Math.min(size, props.maxPageSize()));
        return repository.upcoming(room, since, PageRequest.of(Math.max(0, page), s)).map(BookingView::from);
    }

    public void cancel(Long id) {
        if (!repository.existsById(id)) throw new BookingExceptions.NotFound(id);
        repository.deleteById(id);
    }

    public int purge() { return repository.purgeEndedBefore(clock.instant()); }
}
