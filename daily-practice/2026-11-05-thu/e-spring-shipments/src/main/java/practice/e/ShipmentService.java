package practice.e;

import java.time.Clock;

// TODO: @Service + @Transactional (readOnly = true where it only reads). Constructor injection; maxPageSize: @Value("${shipments.max-page-size:50}").
//  create(req):  orderId = req.orderId().strip(); if repository.findByOrderId(orderId) exists -> DuplicateOrderException.
//                new Shipment(orderId, req.address().strip(), "TRK-" + orderId, clock.instant()); save; publisher.publish(saved); return ShipmentView.from(saved).
//  get(id):      ShipmentNotFoundException if absent.
//  search(status, page, size): clamp size to 1..maxPageSize, page >= 0; repository.search(status, PageRequest.of(page, size)); map to ShipmentPage(content, total, page, size).
//  applyCarrierUpdate(u): the idempotent consumer.
//     1. if processedEvents.existsById(u.eventId()) -> return false (a redelivery: do nothing).
//     2. processedEvents.save(new ProcessedEvent(u.eventId(), clock.instant())).   (Mark first, same transaction: both commit or both roll back.)
//     3. find by trackingId; unknown tracking id -> return false.
//     4. shipment.advanceTo(u.status(), clock.instant()); if it moved, publisher.publish(shipment) and return true; otherwise (stale / out-of-order) return false.
public class ShipmentService {
    public ShipmentService(ShipmentRepository shipments, ProcessedEventRepository processedEvents, ShipmentPublisher publisher, Clock clock, int maxPageSize) { throw new UnsupportedOperationException("TODO"); }

    public ShipmentView create(CreateShipmentRequest req) { throw new UnsupportedOperationException("TODO"); }

    public ShipmentView get(Long id) { throw new UnsupportedOperationException("TODO"); }

    public ShipmentPage search(ShipmentStatus status, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    public boolean applyCarrierUpdate(CarrierUpdate update) { throw new UnsupportedOperationException("TODO"); }
}
