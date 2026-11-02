package practice.d20261102.h2;

/**
 * State pattern: behaviour of insert/select/refund depends on the current state, so each state is its own object
 * (IdleState, CreditState, OutOfServiceState) instead of if/else on a flag. Sketch the transition table first.
 *
 * Rules
 *  - stock(slot, item, priceCents, quantity): add or replace a slot. price > 0, quantity >= 0, else IllegalArgumentException.
 *  - insert(cents): cents > 0 else IllegalArgumentException; adds to credit; IDLE -> HAS_CREDIT. OUT_OF_SERVICE: IllegalStateException.
 *  - select(slot): OUT_OF_SERVICE -> IllegalStateException. Unknown slot -> IllegalArgumentException.
 *      no credit -> IllegalStateException; sold out -> IllegalStateException (credit kept);
 *      credit < price -> IllegalStateException (credit kept);
 *      otherwise quantity--, return Dispensed(item, credit - price), credit = 0, status IDLE.
 *  - refund(): returns the credit (0 if none), credit = 0; IDLE unless OUT_OF_SERVICE. Allowed in every state.
 *  - setOutOfService(true): status OUT_OF_SERVICE, credit kept. setOutOfService(false): IDLE if credit == 0 else HAS_CREDIT.
 */
public final class VendingMachine {
    public enum Status { IDLE, HAS_CREDIT, OUT_OF_SERVICE }

    public record Dispensed(String item, int changeCents) {}

    public VendingMachine() { throw new UnsupportedOperationException("TODO"); }

    public void stock(String slot, String item, int priceCents, int quantity) { throw new UnsupportedOperationException("TODO"); }
    public void insert(int cents) { throw new UnsupportedOperationException("TODO"); }
    public Dispensed select(String slot) { throw new UnsupportedOperationException("TODO"); }
    public int refund() { throw new UnsupportedOperationException("TODO"); }
    public void setOutOfService(boolean outOfService) { throw new UnsupportedOperationException("TODO"); }
    public Status status() { throw new UnsupportedOperationException("TODO"); }
    public int credit() { throw new UnsupportedOperationException("TODO"); }
}
