package practice.e;

// TODO validation: sku not blank, at most 32 chars; quantity 1..1000; unitPricePaise positive.
public record LineRequest(String sku, int quantity, long unitPricePaise) {}
