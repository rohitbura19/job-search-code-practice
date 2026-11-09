package practice.e;

/** Projection target of InvoiceRepository.totalsByCustomer (JPQL constructor expression). */
public record CustomerTotal(String customer, long totalCents) {}
