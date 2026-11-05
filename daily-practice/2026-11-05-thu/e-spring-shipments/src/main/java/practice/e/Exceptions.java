package practice.e;

/** Given. */
public final class Exceptions {
    private Exceptions() {}

    public static class ShipmentNotFoundException extends RuntimeException {
        public ShipmentNotFoundException(Long id) { super("Shipment " + id + " not found"); }
    }

    public static class DuplicateOrderException extends RuntimeException {
        public DuplicateOrderException(String orderId) { super("Order " + orderId + " already has a shipment"); }
    }
}
