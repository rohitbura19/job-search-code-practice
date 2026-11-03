package practice.e;

import java.util.List;

/**
 * GIVEN. Stands in for the Spring Data repository (mocked in the tests).
 * On paper: it would extend JpaRepository<OutboxEvent, Long> and lockBatch would be the native
 * "... where sent_at is null order by id limit :n for update skip locked" query.
 */
public interface OutboxRepository {
    OutboxEvent save(OutboxEvent event);

    /** The next n unsent rows in id order. */
    List<OutboxEvent> lockBatch(int n);
}
