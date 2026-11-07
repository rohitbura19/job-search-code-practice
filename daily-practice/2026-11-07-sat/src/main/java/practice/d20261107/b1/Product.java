package practice.d20261107.b1;

import java.util.List;

public record Product(String name, String category, int stock, long priceCents, List<String> tags) {}
