package practice.e;

public enum InvoiceStatus {
    DRAFT, ISSUED, PAID, VOID;

    /** TODO(E1a): DRAFT -> ISSUED | VOID; ISSUED -> PAID | VOID; PAID and VOID are final. Use a switch expression (no default branch). */
    public boolean canMoveTo(InvoiceStatus next) { throw new UnsupportedOperationException("TODO"); }
}
