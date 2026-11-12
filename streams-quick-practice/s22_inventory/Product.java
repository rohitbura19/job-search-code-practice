package s22_inventory;

import java.util.List;

public record Product(String name, String category, int stock, long priceCents, List<String> tags) {}
