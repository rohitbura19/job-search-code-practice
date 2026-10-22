package practice.e;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class CommandService {

    private final CommandStore store;
    private final ApplicationEventPublisher events;
    private final CommandProps props;
    private final Clock clock;

    /** GIVEN constructor. */
    public CommandService(CommandStore store, ApplicationEventPublisher events, CommandProps props, Clock clock) {
        this.store = store;
        this.events = events;
        this.props = props;
        this.clock = clock;
    }

    /**
     * TODO (@Transactional in the real app; the unit test calls it directly).
     * 1. If a command with this idempotency key already exists, return THAT one: no save, no event.
     * 2. Otherwise build a DeviceCommand (key, chargerId, type, status PENDING, createdAt = clock.instant(),
     *    expiresAt = createdAt + ttl where ttl = ttlSeconds seconds, or props.defaultTtl() when ttlSeconds is null),
     *    save it, publish ONE CommandCreated event (events.publishEvent(...)) and return it.
     */
    public DeviceCommand create(String chargerId, String idempotencyKey, CommandRequest request) {
        throw new UnsupportedOperationException("TODO");
    }

    /** GIVEN. */
    public Page<DeviceCommand> history(String chargerId, Pageable pageable) {
        return store.history(chargerId, pageable);
    }
}
