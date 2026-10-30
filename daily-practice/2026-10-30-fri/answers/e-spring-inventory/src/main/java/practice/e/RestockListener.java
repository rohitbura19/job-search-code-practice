package practice.e;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RestockListener {
    private static final Logger log = LoggerFactory.getLogger(RestockListener.class);
    private final StockService service;

    public RestockListener(StockService service) { this.service = service; }

    /** key = sku, value = quantity as text. A poison message is logged and skipped: throwing would make the container retry it forever. */
    @KafkaListener(topics = "${stock.topics.restock}", groupId = "stock-restock")
    public void onMessage(ConsumerRecord<String, String> rec) {
        int qty;
        try {
            qty = Integer.parseInt(rec.value().trim());
        } catch (RuntimeException e) {
            log.warn("skipping malformed restock {} @{}-{}:{}", rec.key(), rec.topic(), rec.partition(), rec.offset());
            return;
        }
        if (rec.key() == null || qty < 1) {
            log.warn("skipping invalid restock key={} qty={}", rec.key(), qty);
            return;
        }
        try {
            service.restock(rec.key(), qty);
        } catch (StockNotFoundException e) {
            log.warn("restock for unknown sku {}", rec.key());
        }
    }
}
