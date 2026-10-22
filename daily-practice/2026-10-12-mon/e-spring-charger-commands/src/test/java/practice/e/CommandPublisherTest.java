package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** E - the Kafka publisher with a mocked KafkaTemplate; no broker. */
class CommandPublisherTest {

    @SuppressWarnings("unchecked")
    final KafkaTemplate<String, CommandMessage> kafka = mock(KafkaTemplate.class);
    final CommandProps props = new CommandProps("charger-commands", Duration.ofMinutes(5));
    final CommandPublisher publisher = new CommandPublisher(kafka, props);
    final CommandCreated event = new CommandCreated(UUID.randomUUID(), "CH7", CommandType.RESTART);

    @Test @DisplayName("sends to the configured topic with key = chargerId and the message built from the event")
    void sends() {
        when(kafka.send(anyString(), anyString(), any(CommandMessage.class))).thenReturn(CompletableFuture.completedFuture(null));

        publisher.on(event);

        ArgumentCaptor<CommandMessage> msg = ArgumentCaptor.forClass(CommandMessage.class);
        verify(kafka).send(eq("charger-commands"), eq("CH7"), msg.capture());
        assertEquals(event.id(), msg.getValue().id());
        assertEquals("CH7", msg.getValue().chargerId());
        assertEquals(CommandType.RESTART, msg.getValue().type());
    }

    @Test @DisplayName("a failed send is logged, never thrown out of the listener")
    void failureDoesNotThrow() {
        CompletableFuture<org.springframework.kafka.support.SendResult<String, CommandMessage>> failed = new CompletableFuture<>();
        failed.completeExceptionally(new IllegalStateException("broker down"));
        when(kafka.send(anyString(), anyString(), any(CommandMessage.class))).thenReturn(failed);

        publisher.on(event);          // must simply return

        verify(kafka).send(eq("charger-commands"), eq("CH7"), any(CommandMessage.class));
    }

    @Test @DisplayName("on(...) is a @TransactionalEventListener with phase AFTER_COMMIT")
    void afterCommit() throws Exception {
        TransactionalEventListener l = CommandPublisher.class.getMethod("on", CommandCreated.class)
                .getAnnotation(TransactionalEventListener.class);
        assertNotNull(l);
        assertSame(TransactionPhase.AFTER_COMMIT, l.phase());
    }
}
