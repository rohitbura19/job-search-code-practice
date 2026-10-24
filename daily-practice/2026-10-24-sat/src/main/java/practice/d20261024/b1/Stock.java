package practice.d20261024.b1;

import java.time.LocalDate;

public record Stock(String sku, String warehouse, int qty, LocalDate expiry) {}
