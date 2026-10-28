package practice.e;

public record LineView(String sku, int quantity, long unitPricePaise) {
    static LineView from(OrderLine l) { return new LineView(l.getSku(), l.getQuantity(), l.getUnitPricePaise()); }
}
