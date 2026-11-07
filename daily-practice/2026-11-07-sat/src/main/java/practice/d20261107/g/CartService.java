package practice.d20261107.g;

import java.time.Clock;
import java.time.Duration;
import java.util.List;

/**
 * In-memory shopping carts, safe for concurrent use (different owners in parallel, same owner serialised).
 * Rules (every violation is an IllegalArgumentException unless stated):
 * - owner and sku non-blank; qty > 0 for addItem; qty >= 0 for setQty (0 removes the line).
 * - unknown sku (catalog empty) is rejected when adding/setting; at view time, lines whose sku vanished are dropped.
 * - one line's qty may never exceed maxQtyPerLine and a cart may have at most maxLines lines: violation -> IllegalStateException
 *   and the cart is left unchanged.
 * - a cart not touched (any mutation) for ttl is empty: view returns an empty cart, a mutation starts a fresh one.
 * - prices are always the CURRENT catalog price at view time. Discounts: sum of all rules, capped at the subtotal.
 * - view of an unknown owner returns an empty CartView (no lines, zeros), never null.
 */
public class CartService {
    public CartService(PriceCatalog catalog, Clock clock, Duration ttl, int maxQtyPerLine, int maxLines, List<DiscountRule> rules) {
        throw new UnsupportedOperationException("TODO");
    }

    public CartView addItem(String owner, String sku, int qty) { throw new UnsupportedOperationException("TODO"); }

    public CartView setQty(String owner, String sku, int qty) { throw new UnsupportedOperationException("TODO"); }

    public CartView remove(String owner, String sku) { throw new UnsupportedOperationException("TODO"); }

    public CartView view(String owner) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Login: move guest lines into the user's cart (qty summed, each capped at maxQtyPerLine, extra lines beyond maxLines are
     * dropped in sku order), delete the guest cart, return the user's view. Atomic w.r.t. concurrent calls on either owner
     * (avoid deadlock: think about lock ordering). Merging into itself or an empty/expired guest cart is a no-op.
     */
    public CartView mergeGuest(String guestId, String userId) { throw new UnsupportedOperationException("TODO"); }
}
