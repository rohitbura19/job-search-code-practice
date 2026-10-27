package practice.d20261027.b1;

import java.time.LocalDate;
import java.util.List;

public record Shipment(String id, String warehouse, String carrier, Status status, int weightKg, long costPaise,
                       LocalDate shippedOn, List<String> tags) {
    public enum Status { IN_TRANSIT, DELIVERED, DELAYED, RETURNED }
}
