package practice.e;

// TODO: Bean Validation on the components: sku not blank and matching [A-Z0-9-]{3,20}; name not blank, at most 80; category not blank,
//       at most 40; priceCents not null and positive. Why Long (not long) for priceCents?
public record CreateProductRequest(String sku, String name, String category, Long priceCents) {}
