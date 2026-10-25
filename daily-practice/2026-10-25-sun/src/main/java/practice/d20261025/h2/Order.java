package practice.d20261025.h2;

public record Order(String id, long subtotalPaise) {
    public Order {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id");
        if (subtotalPaise < 0) throw new IllegalArgumentException("subtotal");
    }
}
