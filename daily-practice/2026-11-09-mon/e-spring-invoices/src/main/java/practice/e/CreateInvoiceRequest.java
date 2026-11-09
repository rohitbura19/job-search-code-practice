package practice.e;

import java.util.List;

/**
 * TODO(E3): Bean Validation.
 *  - customer: not blank, at most 40 chars.
 *  - items: not empty, at most 50 elements, and every element validated (cascade into Item).
 *  - Item.description: not blank, at most 100; Item.quantity: 1..1000; Item.unitPriceCents: strictly positive.
 */
public record CreateInvoiceRequest(String customer, List<Item> items) {
    public record Item(String description, int quantity, long unitPriceCents) {
        public LineItem toLineItem() { return new LineItem(description, quantity, unitPriceCents); }
    }
}
