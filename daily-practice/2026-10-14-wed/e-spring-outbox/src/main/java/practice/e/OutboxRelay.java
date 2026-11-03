package practice.e;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * TODO - write relay():
 *  - run every 500 ms, after the previous run finished (annotation), inside a transaction (annotation)
 *  - take the batch from repo.lockBatch(props.batchSize())
 *  - for each row: kafka.send(props.topic(), row.getAggregateId(), row.getPayload()), wait at most 5 seconds for the ack,
 *    then set sentAt = clock.instant() (the transaction flushes the UPDATE)
 *  - on the FIRST failure (send failed, or the ack timed out): count it in 'failures' and stop the batch
 *    (skipping would reorder events of the same site); rows after it stay untouched
 *  - if the thread is interrupted: restore the interrupt flag, count a failure and stop
 */
@Component
public class OutboxRelay {
    private final KafkaTemplate<String, String> kafka;
    private final OutboxRepository repo;
    private final OutboxProps props;
    private final Clock clock;
    private final Counter failures;

    public OutboxRelay(KafkaTemplate<String, String> kafka, OutboxRepository repo, OutboxProps props,
                       Clock clock, MeterRegistry registry) {
        this.kafka = kafka; this.repo = repo; this.props = props; this.clock = clock;
        this.failures = Counter.builder("outbox.relay.failures").register(registry);
    }

    public void relay() {
        throw new UnsupportedOperationException("TODO");
    }
}
