package practice.e;

public enum InvoiceStatus {
    DRAFT, ISSUED, PAID, VOID;

    public boolean canMoveTo(InvoiceStatus next) {
        return switch (this) {
            case DRAFT -> next == ISSUED || next == VOID;
            case ISSUED -> next == PAID || next == VOID;
            case PAID, VOID -> false;
        };
    }
}
