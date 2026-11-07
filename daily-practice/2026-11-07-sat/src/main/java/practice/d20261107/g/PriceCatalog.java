package practice.d20261107.g;

import java.util.OptionalLong;

public interface PriceCatalog {
    /** Current unit price in cents; empty if the sku is unknown or no longer sold. */
    OptionalLong priceCents(String sku);
}
