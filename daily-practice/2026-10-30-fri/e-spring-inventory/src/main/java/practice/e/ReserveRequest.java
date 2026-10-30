package practice.e;

/** TODO validation: sku not blank and matching [A-Z0-9-]{3,32}; qty between 1 and 100. */
public record ReserveRequest(String sku, int qty) {}
