package s10_payments;

import java.time.LocalDate;

/** amountPaise is always positive; only SETTLED payments count as money received. */
public record Payment(String id, String merchant, String method, long amountPaise, Status status, LocalDate date) {}
