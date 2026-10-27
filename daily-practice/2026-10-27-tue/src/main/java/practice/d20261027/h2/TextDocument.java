package practice.d20261027.h2;

/** The receiver. Given; do not change. */
public final class TextDocument {
    private final StringBuilder text = new StringBuilder();

    public String text() { return text.toString(); }

    public int length() { return text.length(); }

    /** Inserts at pos (0..length). Throws IndexOutOfBoundsException otherwise. */
    public void insert(int pos, String s) {
        if (pos < 0 || pos > text.length()) throw new IndexOutOfBoundsException("pos " + pos);
        text.insert(pos, s);
    }

    /** Removes and returns len characters starting at pos. Throws IndexOutOfBoundsException if the range is outside the text. */
    public String delete(int pos, int len) {
        if (pos < 0 || len < 0 || pos + len > text.length()) throw new IndexOutOfBoundsException("range " + pos + "+" + len);
        String removed = text.substring(pos, pos + len);
        text.delete(pos, pos + len);
        return removed;
    }
}
