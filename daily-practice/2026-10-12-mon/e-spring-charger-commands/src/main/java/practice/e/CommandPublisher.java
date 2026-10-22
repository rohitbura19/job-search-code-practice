package practice.e;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommandPublisher {

    private final KafkaTemplate<String, CommandMessage> kafka;
    private final CommandProps props;

    /** GIVEN constructor. */
    public CommandPublisher(KafkaTemplate<String, CommandMessage> kafka, CommandProps props) {
        this.kafka = kafka;
        this.props = props;
    }

    /**
     * TODO: runs only AFTER the transaction commits (which annotation + which phase?).
     * Send CommandMessage.from(event) to the topic props.topic() with key = event.chargerId().
     * kafka.send(...) returns a CompletableFuture: log a failure (do not throw out of this method).
     */
    public void on(CommandCreated event) {
        throw new UnsupportedOperationException("TODO");
    }
}
