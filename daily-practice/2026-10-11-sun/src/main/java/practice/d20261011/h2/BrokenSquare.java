package practice.d20261011.h2;

/** GIVEN - do not edit. */
public class BrokenSquare extends BrokenRectangle {
    @Override public void setW(int w) { this.w = this.h = w; }
    @Override public void setH(int h) { this.w = this.h = h; }
}
