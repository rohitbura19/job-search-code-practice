package practice.d20261102.h2;

import java.util.HashMap;
import java.util.Map;

/**
 * State pattern: the machine delegates insert/select/refund to the current State object and states swap themselves
 * via transition(...). Adding a state (say MAINTENANCE) is one new class; the machine and the other states stay untouched.
 * The credit and stock are shared context owned by the machine. Not thread-safe; make the public methods synchronized if shared.
 */
public final class VendingMachine {
    public enum Status { IDLE, HAS_CREDIT, OUT_OF_SERVICE }

    public record Dispensed(String item, int changeCents) {}

    private record Slot(String item, int price, int[] qty) {}

    private interface State {
        Status status();
        void insert(int cents);
        Dispensed select(String slot);
    }

    private final Map<String, Slot> slots = new HashMap<>();
    private int credit;
    private State state = new IdleState();

    public void stock(String slot, String item, int priceCents, int quantity) {
        if (priceCents <= 0 || quantity < 0) throw new IllegalArgumentException();
        slots.put(slot, new Slot(item, priceCents, new int[]{quantity}));
    }

    public void insert(int cents) {
        if (cents <= 0) throw new IllegalArgumentException("cents");
        state.insert(cents);
    }

    public Dispensed select(String slot) { return state.select(slot); }

    public int refund() {
        int back = credit;
        credit = 0;
        if (state.status() != Status.OUT_OF_SERVICE) state = new IdleState();
        return back;
    }

    public void setOutOfService(boolean outOfService) {
        state = outOfService ? new OutOfServiceState() : credit == 0 ? new IdleState() : new CreditState();
    }

    public Status status() { return state.status(); }
    public int credit() { return credit; }

    private Dispensed sell(String slotId) {
        Slot s = slots.get(slotId);
        if (s == null) throw new IllegalArgumentException("unknown slot " + slotId);
        if (s.qty()[0] == 0) throw new IllegalStateException("sold out");
        if (credit < s.price()) throw new IllegalStateException("need " + (s.price() - credit) + " more");
        s.qty()[0]--;
        int change = credit - s.price();
        credit = 0;
        state = new IdleState();
        return new Dispensed(s.item(), change);
    }

    private final class IdleState implements State {
        public Status status() { return Status.IDLE; }
        public void insert(int cents) { credit += cents; state = new CreditState(); }
        public Dispensed select(String slot) {
            if (!slots.containsKey(slot)) throw new IllegalArgumentException("unknown slot " + slot);
            throw new IllegalStateException("insert money first");
        }
    }

    private final class CreditState implements State {
        public Status status() { return Status.HAS_CREDIT; }
        public void insert(int cents) { credit += cents; }
        public Dispensed select(String slot) { return sell(slot); }
    }

    private final class OutOfServiceState implements State {
        public Status status() { return Status.OUT_OF_SERVICE; }
        public void insert(int cents) { throw new IllegalStateException("out of service"); }
        public Dispensed select(String slot) { throw new IllegalStateException("out of service"); }
    }
}
