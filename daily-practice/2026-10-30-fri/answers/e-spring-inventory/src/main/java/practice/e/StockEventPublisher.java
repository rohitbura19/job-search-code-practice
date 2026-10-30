package practice.e;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockEventPublisher {
    private final KafkaTemplate<String, String> kafka;
    private final StockProps props;

    public StockEventPublisher(KafkaTemplate<String, String> kafka, StockProps props) { this.kafka = kafka; this.props = props; }

    /** Key = sku so all events of one SKU land on one partition, in order. */
    public void reserved(String sku, int qty) {
        kafka.send(props.topics().reserved(), sku, "{\"sku\":\"" + sku + "\",\"qty\":" + qty + "}");
    }

    public void lowStock(String sku, int available) {
        kafka.send(props.topics().lowStock(), sku, "{\"sku\":\"" + sku + "\",\"available\":" + available + "}");
    }
}
