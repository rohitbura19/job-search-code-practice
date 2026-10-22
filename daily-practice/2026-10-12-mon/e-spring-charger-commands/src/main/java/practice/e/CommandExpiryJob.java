package practice.e;

import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class CommandExpiryJob {

    private final CommandStore store;
    private final Clock clock;

    /** GIVEN constructor. */
    public CommandExpiryJob(CommandStore store, Clock clock) {
        this.store = store;
        this.clock = clock;
    }

    /**
     * TODO: runs every minute (annotation with a 60 second delay) and moves PENDING commands whose expiresAt has passed
     * to EXPIRED: store.expire(PENDING, EXPIRED, now) with now taken from the injected clock.
     */
    public void run() {
        throw new UnsupportedOperationException("TODO");
    }
}
