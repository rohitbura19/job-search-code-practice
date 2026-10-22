package practice.e;

import java.util.Optional;

/**
 * GIVEN - do not edit. In the real application: {@code extends JpaRepository<ChargerEvent, Long>} (which supplies saveAndFlush);
 * here it is a plain interface so the tests can mock it. saveAndFlush throws
 * {@code org.springframework.dao.DataIntegrityViolationException} when the unique eventId is violated.
 */
public interface ChargerEventRepository {
    Optional<ChargerEvent> findFirstByChargerIdOrderByEventTimeDesc(String chargerId);
    boolean existsByEventId(String eventId);
    ChargerEvent saveAndFlush(ChargerEvent event);
}
