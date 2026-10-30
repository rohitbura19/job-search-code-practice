package practice.e;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

/** No broker: the publisher gets a mock KafkaTemplate, the listener method is called directly. */
class KafkaPartsTest {
    KafkaTemplate<String, String> kafka;
    StockProps props = new StockProps(5, 50, new StockProps.Topics("t.reserved", "t.low", "t.restock"));

    @SuppressWarnings("unchecked")
    @BeforeEach void setUp() {
        kafka = mock(KafkaTemplate.class);
        when(kafka.send(anyString(), anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(null));
    }

    @Test void publisherKeysByskuAndUsesConfiguredTopics() {
        StockEventPublisher p = new StockEventPublisher(kafka, props);
        p.reserved("AAA-1", 3);
        p.lowStock("AAA-1", 2);
        verify(kafka).send("t.reserved", "AAA-1", "{\"sku\":\"AAA-1\",\"qty\":3}");
        verify(kafka).send("t.low", "AAA-1", "{\"sku\":\"AAA-1\",\"available\":2}");
    }

    ConsumerRecord<String, String> rec(String key, String value) { return new ConsumerRecord<>("t.restock", 0, 0L, key, value); }

    @Test void listenerRestocksOnValidMessage() {
        StockService service = mock(StockService.class);
        RestockListener l = new RestockListener(service);
        l.onMessage(rec("AAA-1", "25"));
        l.onMessage(rec("BBB-2", " 4 "));
        verify(service).restock("AAA-1", 25);
        verify(service).restock("BBB-2", 4);
    }

    @Test void listenerSkipsPoisonMessagesWithoutThrowing() {
        StockService service = mock(StockService.class);
        RestockListener l = new RestockListener(service);
        l.onMessage(rec("AAA-1", "abc"));
        l.onMessage(rec("AAA-1", "0"));
        l.onMessage(rec("AAA-1", "-3"));
        l.onMessage(rec("AAA-1", ""));
        l.onMessage(rec("AAA-1", null));
        l.onMessage(rec(null, "5"));
        l.onMessage(rec("AAA-1", "99999999999"));
        verify(service, never()).restock(anyString(), anyInt());
    }

    @Test void listenerSwallowsUnknownSku() {
        StockService service = mock(StockService.class);
        doThrow(new StockNotFoundException("ZZZ-9")).when(service).restock("ZZZ-9", 5);
        new RestockListener(service).onMessage(rec("ZZZ-9", "5"));      // must not propagate: a retry would never succeed
    }

    @Test void listenerIsWiredToTheConfiguredTopicAndGroup() throws Exception {
        KafkaListener ann = RestockListener.class.getMethod("onMessage", ConsumerRecord.class).getAnnotation(KafkaListener.class);
        assertNotNull(ann, "missing @KafkaListener");
        assertArrayEquals(new String[] {"${stock.topics.restock}"}, ann.topics());
        assertEquals("stock-restock", ann.groupId());
    }
}
