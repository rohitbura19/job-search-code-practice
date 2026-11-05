package practice.e;

import java.time.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShipmentService {
    private final ShipmentRepository shipments;
    private final ProcessedEventRepository processedEvents;
    private final ShipmentPublisher publisher;
    private final Clock clock;
    private final int maxPageSize;

    public ShipmentService(ShipmentRepository shipments, ProcessedEventRepository processedEvents, ShipmentPublisher publisher, Clock clock,
                           @Value("${shipments.max-page-size:50}") int maxPageSize) {
        this.shipments = shipments;
        this.processedEvents = processedEvents;
        this.publisher = publisher;
        this.clock = clock;
        this.maxPageSize = maxPageSize;
    }

    public ShipmentView create(CreateShipmentRequest req) {
        String orderId = req.orderId().strip();
        if (shipments.findByOrderId(orderId).isPresent()) throw new Exceptions.DuplicateOrderException(orderId);
        Shipment saved = shipments.save(new Shipment(orderId, req.address().strip(), "TRK-" + orderId, clock.instant()));
        publisher.publish(saved);
        return ShipmentView.from(saved);
    }

    @Transactional(readOnly = true)
    public ShipmentView get(Long id) {
        return shipments.findById(id).map(ShipmentView::from).orElseThrow(() -> new Exceptions.ShipmentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public ShipmentPage search(ShipmentStatus status, int page, int size) {
        int s = Math.max(1, Math.min(size, maxPageSize));
        int p = Math.max(0, page);
        Page<Shipment> result = shipments.search(status, PageRequest.of(p, s));
        return new ShipmentPage(result.getContent().stream().map(ShipmentView::from).toList(), result.getTotalElements(), p, s);
    }

    public boolean applyCarrierUpdate(CarrierUpdate u) {
        if (processedEvents.existsById(u.eventId())) return false;
        processedEvents.save(new ProcessedEvent(u.eventId(), clock.instant()));
        Shipment shipment = shipments.findByTrackingId(u.trackingId()).orElse(null);
        if (shipment == null) return false;
        if (!shipment.advanceTo(u.status(), clock.instant())) return false;
        publisher.publish(shipment);
        return true;
    }
}
