package practice.d20261011.h2;

/**
 * Diamond of default methods: Walker and Swimmer both define move(). Java refuses to compile a class that inherits both
 * unresolved, so this class must override it. move() must return "walk+swim" using both super defaults (X.super.move()).
 */
public class Amphibian implements Walker, Swimmer {
    @Override public String move() {
        throw new UnsupportedOperationException("TODO");
    }
}
