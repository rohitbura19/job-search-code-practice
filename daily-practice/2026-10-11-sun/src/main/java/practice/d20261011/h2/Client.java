package practice.d20261011.h2;

/** GIVEN - do not edit. The client from the question; returns the area instead of asserting it. */
public final class Client {
    private Client() {}
    public static int grow(BrokenRectangle r) {
        r.setW(5);
        r.setH(4);
        return r.area();
    }
}
