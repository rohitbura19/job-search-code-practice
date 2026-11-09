package practice.e;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record LineItem(
        @Column(nullable = false, length = 100) String description,
        @Column(nullable = false) int quantity,
        @Column(name = "unit_price_cents", nullable = false) long unitPriceCents) {
    public long totalCents() { return quantity * unitPriceCents; }
}
