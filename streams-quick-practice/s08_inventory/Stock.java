package s08_inventory;

import java.time.LocalDate;

public record Stock(String sku, String warehouse, int qty, LocalDate expiry) {}
