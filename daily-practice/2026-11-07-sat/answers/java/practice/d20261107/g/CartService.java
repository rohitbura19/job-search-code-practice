package practice.d20261107.g;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Design: per-owner monitor (ConcurrentHashMap<owner, Object>) guards that owner's cart; carts live in a map guarded by its own
 * short synchronized sections. A mutation copies the cart, applies the change to the copy and only then stores it, so a failed
 * rule check leaves the cart untouched. mergeGuest locks the two owners in sorted order, which rules out deadlock when two
 * threads merge in opposite directions. Expiry is checked lazily on access (no sweeper; add one to bound memory in production).
 * Complexity: O(lines log lines) per operation, O(1) lock work.
 */
public class CartService {
    private final PriceCatalog catalog;
    private final Clock clock;
    private final Duration ttl;
    private final int maxQty, maxLines;
    private final List<DiscountRule> rules;
    private final Map<String, Cart> carts = new HashMap<>();
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    private static final class Cart {
        final TreeMap<String, Integer> qty = new TreeMap<>();
        Instant touched;
    }

    public CartService(PriceCatalog catalog, Clock clock, Duration ttl, int maxQtyPerLine, int maxLines, List<DiscountRule> rules) {
        this.catalog = Objects.requireNonNull(catalog);
        this.clock = Objects.requireNonNull(clock);
        this.ttl = ttl;
        this.maxQty = maxQtyPerLine;
        this.maxLines = maxLines;
        this.rules = List.copyOf(rules);
    }

    public CartView addItem(String owner, String sku, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty");
        return mutate(owner, sku, true, cart -> {
            int next = cart.qty.getOrDefault(sku, 0) + qty;
            put(cart, sku, next);
        });
    }

    public CartView setQty(String owner, String sku, int qty) {
        if (qty < 0) throw new IllegalArgumentException("qty");
        return mutate(owner, sku, qty > 0, cart -> {
            if (qty == 0) cart.qty.remove(sku); else put(cart, sku, qty);
        });
    }

    public CartView remove(String owner, String sku) {
        return mutate(owner, sku, false, cart -> cart.qty.remove(sku));
    }

    private void put(Cart cart, String sku, int qty) {
        if (qty > maxQty) throw new IllegalStateException("max qty per line");
        if (!cart.qty.containsKey(sku) && cart.qty.size() >= maxLines) throw new IllegalStateException("max lines");
        cart.qty.put(sku, qty);
    }

    private CartView mutate(String owner, String sku, boolean mustBeSold, java.util.function.Consumer<Cart> change) {
        check(owner, "owner");
        check(sku, "sku");
        if (mustBeSold && catalog.priceCents(sku).isEmpty()) throw new IllegalArgumentException("unknown sku " + sku);
        synchronized (lock(owner)) {
            Cart cart = live(owner);
            Cart work = copy(cart == null ? new Cart() : cart);
            change.accept(work);                 // throws -> nothing stored, cart unchanged
            work.touched = clock.instant();
            carts.put(owner, work);
            return render(owner, work);
        }
    }

    public CartView view(String owner) {
        check(owner, "owner");
        synchronized (lock(owner)) {
            return render(owner, live(owner));
        }
    }

    public CartView mergeGuest(String guestId, String userId) {
        check(guestId, "guest");
        check(userId, "user");
        if (guestId.equals(userId)) return view(userId);
        String first = guestId.compareTo(userId) < 0 ? guestId : userId;
        String second = first.equals(guestId) ? userId : guestId;
        synchronized (lock(first)) {
            synchronized (lock(second)) {
                Cart guest = live(guestId);
                if (guest == null || guest.qty.isEmpty()) return render(userId, live(userId));
                Cart user = live(userId);
                Cart work = copy(user == null ? new Cart() : user);
                for (Map.Entry<String, Integer> e : guest.qty.entrySet()) {          // sku order
                    int merged = Math.min(maxQty, work.qty.getOrDefault(e.getKey(), 0) + e.getValue());
                    if (!work.qty.containsKey(e.getKey()) && work.qty.size() >= maxLines) continue;
                    work.qty.put(e.getKey(), merged);
                }
                work.touched = clock.instant();
                carts.put(userId, work);
                carts.remove(guestId);
                return render(userId, work);
            }
        }
    }

    // ---- helpers (callers hold the owner lock)
    private Object lock(String owner) { return locks.computeIfAbsent(owner, k -> new Object()); }

    private Cart live(String owner) {
        Cart c;
        synchronized (carts) { c = carts.get(owner); }
        if (c == null) return null;
        if (!clock.instant().isBefore(c.touched.plus(ttl))) { synchronized (carts) { carts.remove(owner); } return null; }
        return c;
    }

    private static Cart copy(Cart c) {
        Cart n = new Cart();
        n.qty.putAll(c.qty);
        n.touched = c.touched;
        return n;
    }

    private CartView render(String owner, Cart cart) {
        List<CartLine> lines = new ArrayList<>();
        if (cart != null)
            for (Map.Entry<String, Integer> e : cart.qty.entrySet()) {
                OptionalLong p = catalog.priceCents(e.getKey());
                if (p.isPresent()) lines.add(new CartLine(e.getKey(), e.getValue(), p.getAsLong()));
            }
        long sub = lines.stream().mapToLong(CartLine::totalCents).sum();
        long discount = Math.min(sub, rules.stream().mapToLong(r -> Math.max(0, r.discountCents(lines))).sum());
        return new CartView(owner, List.copyOf(lines), sub, discount, sub - discount);
    }

    private static void check(String s, String what) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(what);
    }
}
