package practice.e;

import org.springframework.kafka.core.KafkaTemplate;

/** TODO: make it a Spring component. Both methods send with the sku as the record key (same sku -> same partition, in order). */
public class StockEventPublisher {
    private final KafkaTemplate<String, String> kafka;
    private final StockProps props;

    public StockEventPublisher(KafkaTemplate<String, String> kafka, StockProps props) { this.kafka = kafka; this.props = props; }

    /** TODO send to topics().reserved(): {"sku":"AAA-1","qty":3} */
    public void reserved(String sku, int qty) { throw new UnsupportedOperationException("TODO"); }

    /** TODO send to topics().lowStock(): {"sku":"AAA-1","available":2} */
    public void lowStock(String sku, int available) { throw new UnsupportedOperationException("TODO"); }
}
