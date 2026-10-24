package practice.e;

public record ProductView(long id, String sku, String name, String category, long priceCents) {
    static ProductView of(Product p) { return new ProductView(p.getId(), p.getSku(), p.getName(), p.getCategory(), p.getPriceCents()); }
}
