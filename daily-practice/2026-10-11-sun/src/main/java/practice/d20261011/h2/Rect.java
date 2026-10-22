package practice.d20261011.h2;

/** Immutable rectangle. withW / withH return a NEW Rect. */
public record Rect(int w, int h) implements Shape {
    public int area() { throw new UnsupportedOperationException("TODO"); }
    public int perimeter() { throw new UnsupportedOperationException("TODO"); }
    public Rect withW(int newW) { throw new UnsupportedOperationException("TODO"); }
    public Rect withH(int newH) { throw new UnsupportedOperationException("TODO"); }
}
