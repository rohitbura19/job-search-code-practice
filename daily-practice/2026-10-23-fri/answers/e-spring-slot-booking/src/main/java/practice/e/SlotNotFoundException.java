package practice.e;

public class SlotNotFoundException extends RuntimeException {
    public SlotNotFoundException(long id) { super("slot " + id + " not found"); }
}
