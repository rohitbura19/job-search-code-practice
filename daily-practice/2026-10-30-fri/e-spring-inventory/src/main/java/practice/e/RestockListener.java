package practice.e;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/** TODO: make it a Spring component. */
public class RestockListener {
    private final StockService service;

    public RestockListener(StockService service) { this.service = service; }

    /**
     * TODO: @KafkaListener on topic "${stock.topics.restock}" with groupId "stock-restock". key = sku, value = quantity as text.
     * Valid (key present, integer >= 1) -> service.restock(key, qty). Malformed value (null, blank, text, 0, negative, overflow) or null key
     * -> log and skip, never throw (a retry cannot fix it). StockNotFoundException from the service -> log and skip as well.
     */
    public void onMessage(ConsumerRecord<String, String> rec) { throw new UnsupportedOperationException("TODO"); }
}
