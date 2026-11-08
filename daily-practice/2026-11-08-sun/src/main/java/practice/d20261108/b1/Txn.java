package practice.d20261108.b1;

import java.time.LocalDate;

/** Bank transaction; cents is signed (negative = money out). */
public record Txn(String account, String category, long cents, LocalDate date) {}
