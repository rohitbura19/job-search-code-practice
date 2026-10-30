package practice.e;

public record StockView(String sku, int onHand, int reserved, int available) {
    public static StockView from(StockItem i) { return new StockView(i.getSku(), i.getOnHand(), i.getReserved(), i.available()); }
}
