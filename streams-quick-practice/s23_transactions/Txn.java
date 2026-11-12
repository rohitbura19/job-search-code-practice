package s23_transactions;

import java.time.LocalDate;

/** Bank transaction; cents is signed (negative = money out). */
public record Txn(String account, String category, long cents, LocalDate date) {}
