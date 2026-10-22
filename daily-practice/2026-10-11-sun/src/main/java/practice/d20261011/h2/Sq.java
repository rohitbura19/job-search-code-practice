package practice.d20261011.h2;

/** Immutable square. withSide returns a NEW Sq. */
public record Sq(int side) implements Shape {
    public int area() { throw new UnsupportedOperationException("TODO"); }
    public int perimeter() { throw new UnsupportedOperationException("TODO"); }
    public Sq withSide(int newSide) { throw new UnsupportedOperationException("TODO"); }
}
