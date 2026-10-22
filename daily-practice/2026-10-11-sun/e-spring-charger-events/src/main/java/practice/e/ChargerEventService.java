package practice.e;

/**
 * TODO: a Spring @Service. Constructor injection of the repository.
 * save(dto): map the DTO to a ChargerEvent, repo.saveAndFlush(...), and IGNORE a DataIntegrityViolationException
 * (duplicate delivery = idempotent). Any other exception must propagate. Think about @Transactional.
 */
public class ChargerEventService {
    public ChargerEventService(ChargerEventRepository repo) {
        // TODO
    }

    public void save(EventDto dto) {
        throw new UnsupportedOperationException("TODO");
    }
}
