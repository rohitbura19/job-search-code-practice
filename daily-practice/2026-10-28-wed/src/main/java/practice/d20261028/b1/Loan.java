package practice.d20261028.b1;

import java.time.LocalDate;

/** returned is null while the book is still out. */
public record Loan(String member, String book, String genre, LocalDate borrowed, LocalDate returned) {}
