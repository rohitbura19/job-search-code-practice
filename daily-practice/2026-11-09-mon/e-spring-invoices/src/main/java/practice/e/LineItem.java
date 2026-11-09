package practice.e;

/**
 * TODO(E1b): make this an embeddable value type (record) stored in the invoice_line collection table.
 *  - description: not null, length 100; quantity: not null; unitPriceCents: column "unit_price_cents", not null.
 */
public record LineItem(String description, int quantity, long unitPriceCents) {
    public long totalCents() { return quantity * unitPriceCents; }
}
