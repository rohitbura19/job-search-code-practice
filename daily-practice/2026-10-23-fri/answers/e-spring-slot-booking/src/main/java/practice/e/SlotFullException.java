package practice.e;

public class SlotFullException extends RuntimeException {
    public SlotFullException(long id) { super("slot " + id + " is full"); }
}
